package io.github.francoiscampbell.xposeddatausage

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.util.findViewById
import io.github.francoiscampbell.xposeddatausage.util.hookLayout
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver
import io.github.francoiscampbell.xposeddatausage.widget.ClockWrapper
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageViewImpl

/**
 * Created by francois on 16-03-11.
 */
class Module : IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    companion object {
        lateinit var hookedContext: Context
        lateinit var modulePath: String

        private val PACKAGE_SYSTEM_UI = "com.android.systemui"
        private val PACKAGE_ANDROID_SYSTEM = "android"
        private val CLASS_NAME_CONTEXT = "android.app.ContextImpl"
        private val PERMISSION_READ_NETWORK_USAGE_HISTORY = "android.permission.READ_NETWORK_USAGE_HISTORY"
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != PACKAGE_ANDROID_SYSTEM) {
            return
        }


        XposedLog.i("Nuking permission check")

        XposedHelpers.findAndHookMethod(
                CLASS_NAME_CONTEXT,
                lpparam.classLoader,
                "enforce",
                String::class.java, //permission
                Int::class.java, //resultOfCheck
                Boolean::class.java, //selfToo
                Int::class.java, //uid
                String::class.java, //message
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)

                        val requestedPermission = param.args[0]
                        val requestingUid = param.args[3]
                        if (requestedPermission == PERMISSION_READ_NETWORK_USAGE_HISTORY) {
                            param.args[1] = PackageManager.PERMISSION_GRANTED //resultOfCheck
                            XposedLog.i("Granting $requestedPermission to requestingUid $requestingUid")
                        }
                    }
                }
        )
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }

        resparam.res.hookLayout(PACKAGE_SYSTEM_UI, "layout", "status_bar") { liparam ->
            hookedContext = liparam.view.context

            val clock = liparam.findViewById("clock") as TextView
            val systemIcons = liparam.findViewById("system_icon_area") as ViewGroup
            val dataUsageView = DataUsageViewImpl(hookedContext, ClockWrapper(clock))

            systemIcons.addView(dataUsageView, 0)

            hookedContext.registerReceiver(IntentFilter(Intent.ACTION_TIME_TICK)) { context, intent ->
                dataUsageView.update()
            }
        }
    }
}