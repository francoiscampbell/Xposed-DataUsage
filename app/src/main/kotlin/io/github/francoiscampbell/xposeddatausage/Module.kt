package io.github.francoiscampbell.xposeddatausage

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.francoiscampbell.xposeddatausage.util.findViewById
import io.github.francoiscampbell.xposeddatausage.view.DataUsageView

/**
 * Created by francois on 16-03-11.
 */
class Module : IXposedHookLoadPackage, IXposedHookInitPackageResources {
    companion object {
        private val PACKAGE_SYSTEM_UI = "com.android.systemui"
    }

    private var dataUsageView: DataUsageView? = null

    init {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            XposedBridge.log(throwable)
        }
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }

        XposedHelpers.findAndHookMethod("$PACKAGE_SYSTEM_UI.statusbar.policy.Clock", lpparam.classLoader, "updateClock", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                dataUsageView?.update()
                if (BuildConfig.DEBUG) {
                    XposedBridge.log("Updating data usage counter to: ${dataUsageView?.text}")
                }
            }
        })
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }

        resparam.res.hookLayout(PACKAGE_SYSTEM_UI, "layout", "status_bar", object : XC_LayoutInflated() {
            override fun handleLayoutInflated(liparam: LayoutInflatedParam) {
                val statusbar = liparam.view as ViewGroup
                val clock = liparam.findViewById("clock") as TextView
                val systemIcons = liparam.findViewById("system_icon_area") as ViewGroup

                dataUsageView = DataUsageView(statusbar.context)
                dataUsageView?.apply {
//                    textSize = clock.textSize
//                    textScaleX = clock.textScaleX
                    layoutParams = clock.layoutParams
                    gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
                }

                systemIcons.addView(dataUsageView, 0)
            }
        })
    }
}