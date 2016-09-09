package io.github.francoiscampbell.xposeddatausage

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.INetworkStatsService
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.francoiscampbell.xposeddatausage.di.AppModule
import io.github.francoiscampbell.xposeddatausage.di.DaggerAppComponent
import io.github.francoiscampbell.xposeddatausage.di.DaggerUiComponent
import io.github.francoiscampbell.xposeddatausage.di.UiModule
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager

/**
 * Created by francois on 16-03-11.
 */
class Module() : IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    companion object {
        private lateinit var modulePath: String

        private const val PACKAGE_SYSTEM_UI = "com.android.systemui"
        private const val PACKAGE_ANDROID_SYSTEM = "android"
        private const val CLASS_NAME_NETWORK_STATS_SERVICE = "com.android.server.net.NetworkStatsService"

        const val ACTION_GET_DATA_USAGE = "io.github.francoiscampbell.xposeddatausage.GET_DATA_USAGE"
        const val EXTRA_NETWORK_TYPE = "NETWORK_TYPE"
        private const val RESULT_DATA_USAGE = "io.github.francoiscampbell.xposeddatausage.DATA_USAGE"
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != PACKAGE_ANDROID_SYSTEM) return

        var receiverRegistered = false
        XposedBridge.hookAllConstructors(
                XposedHelpers.findClass(CLASS_NAME_NETWORK_STATS_SERVICE, lpparam.classLoader),
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (receiverRegistered) return

                        val iNetworkStatsService = param.thisObject as INetworkStatsService
                        val context = XposedHelpers.getObjectField(iNetworkStatsService, "mContext") as Context
                        val dataUsageFetcher = DaggerAppComponent.builder()
                                .appModule(AppModule(context, iNetworkStatsService))
                                .build()
                                .dataUsageFetcher()

                        receiverRegistered = true //This call has to happen before any calls to context.registerReceiver
                        context.registerReceiver(object : BroadcastReceiver() {
                            override fun onReceive(context: Context, intent: Intent) {
                                val networkTypeString = intent.getStringExtra(EXTRA_NETWORK_TYPE)
                                val networkType = NetworkManager.NetworkType.valueOf(networkTypeString, NetworkManager.NetworkType.MOBILE)
                                val dataUsageBundle = dataUsageFetcher.getCurrentCycleBytes(networkType)
                                        .toBlocking() //synchronously in a BroadcastReceiver
                                        .value()
                                        .bundle()
                                setResult(Activity.RESULT_OK, RESULT_DATA_USAGE, dataUsageBundle)
                            }
                        }, IntentFilter(ACTION_GET_DATA_USAGE))
                    }
                })
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != PACKAGE_SYSTEM_UI) return

        resparam.res.hookLayout(PACKAGE_SYSTEM_UI, "layout", "status_bar", object : XC_LayoutInflated() {
            override fun handleLayoutInflated(liparam: LayoutInflatedParam) {
                val hookedContext = liparam.view.context

                val dataUsageView = DaggerUiComponent.builder()
                        .uiModule(UiModule(modulePath, hookedContext, liparam))
                        .build()
                        .dataUsageView()

                hookedContext.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) = dataUsageView.update()
                }, IntentFilter(Intent.ACTION_TIME_TICK))
            }
        })
    }
}