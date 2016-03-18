package io.github.francoiscampbell.xposeddatausage

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import io.github.francoiscampbell.xposeddatausage.util.findViewById
import io.github.francoiscampbell.xposeddatausage.util.hookLayout
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver
import io.github.francoiscampbell.xposeddatausage.widget.ClockWrapper
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageViewImpl

/**
 * Created by francois on 16-03-11.
 */
class Module : IXposedHookZygoteInit, IXposedHookInitPackageResources {
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

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }
        resparam.res.hookLayout(PACKAGE_SYSTEM_UI, "layout", "status_bar") { liparam ->
            hookedContext = liparam.view.context
            hookedContext.registerReceiver(IntentFilter(Intent.ACTION_TIME_TICK)) { context, intent ->
                dataUsageView?.update()
                XposedBridge.log("Update clock")
            }

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