package io.github.francoiscampbell.xposeddatausage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.INetworkStatsService
import android.os.IBinder
import android.os.IDataUsageService
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.francoiscampbell.xposeddatausage.di.AppModule
import io.github.francoiscampbell.xposeddatausage.di.DaggerAppComponent
import io.github.francoiscampbell.xposeddatausage.di.DaggerUiComponent
import io.github.francoiscampbell.xposeddatausage.di.UiModule
import io.github.francoiscampbell.xposeddatausage.log.XposedLog

/**
 * Created by francois on 16-03-11.
 */
class Module() : IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    companion object {
        private lateinit var modulePath: String

        private const val PACKAGE_SYSTEM_UI = "com.android.systemui"
        private const val PACKAGE_ANDROID_SYSTEM = "android"
        private const val CLASS_CONTEXT_IMPL = "android.app.ContextImpl"
        private const val CLASS_NETWORK_STATS_SERVICE = "com.android.server.net.NetworkStatsService"
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        //Do this in every app
        XposedHelpers.findAndHookMethod(
                XposedHelpers.findClass(CLASS_CONTEXT_IMPL, lpparam.classLoader),
                "getSystemService",
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (param.args[0] == "DataUsageService") {
                            XposedLog.i("Get DataUsageService")
                            val binder = XposedHelpers.callStaticMethod(
                                    XposedHelpers.findClass("android.os.ServiceManager", lpparam.classLoader),
                                    "getService",
                                    "DataUsageService"
                            ) as IBinder
                            param.result = IDataUsageService.Stub.asInterface(binder)
                        }
                    }
                }
        )

        //Do this only in the system_process
        if (lpparam.packageName != PACKAGE_ANDROID_SYSTEM) return
        XposedBridge.hookAllConstructors(
                XposedHelpers.findClass(CLASS_NETWORK_STATS_SERVICE, lpparam.classLoader),
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        XposedLog.i("Register DataUsageService")
                        val iNetworkStatsService = param.thisObject as INetworkStatsService
                        val context = XposedHelpers.getObjectField(iNetworkStatsService, "mContext") as Context
                        val dataUsageService = DaggerAppComponent.builder()
                                .appModule(AppModule(context, iNetworkStatsService))
                                .build()
                                .dataUsageService()

                        XposedHelpers.callStaticMethod(
                                XposedHelpers.findClass("android.os.ServiceManager", lpparam.classLoader),
                                "addService",
                                "DataUsageService",
                                dataUsageService
                        )
                    }
                }
        )
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