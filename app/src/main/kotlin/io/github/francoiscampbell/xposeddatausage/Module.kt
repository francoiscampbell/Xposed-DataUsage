package io.github.francoiscampbell.xposeddatausage

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.francoiscampbell.xposeddatausage.util.findViewById
import io.github.francoiscampbell.xposeddatausage.util.hookLayout
import io.github.francoiscampbell.xposeddatausage.widget.ClockWrapper
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageViewImpl

/**
 * Created by francois on 16-03-11.
 */
class Module : IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    companion object {
        lateinit var hookedContext: Context
        lateinit var modulePath: String
        val PACKAGE_SYSTEM_UI = "com.android.systemui"
    }

    private var dataUsageView: DataUsageViewImpl? = null

    init {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            XposedBridge.log(throwable)
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }

        XposedHelpers.findAndHookMethod("$PACKAGE_SYSTEM_UI.statusbar.policy.Clock", lpparam.classLoader, "updateClock", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                dataUsageView?.update()
            }
        })
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }
        resparam.res.hookLayout(PACKAGE_SYSTEM_UI, "layout", "status_bar") { liparam ->
            hookedContext = liparam.view.context

            val clock = liparam.findViewById("clock") as TextView
            val systemIcons = liparam.findViewById("system_icon_area") as ViewGroup

            dataUsageView = DataUsageViewImpl(hookedContext, ClockWrapper(clock))
            dataUsageView?.apply {
                setTextColor(clock.textColors)
                alpha = clock.alpha
                typeface = clock.typeface
                layoutParams = clock.layoutParams
                gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
            }

            systemIcons.addView(dataUsageView, 0)
        }
    }
}