package io.github.francoiscampbell.xposeddatausage

import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.francoiscampbell.xposeddatausage.view.DataUsageView

/**
 * Created by francois on 16-03-11.
 */
class Module : IXposedHookLoadPackage, IXposedHookInitPackageResources {
    companion object {
        private val PACKAGE_SYSTEM_UI = "com.android.systemui"
    }

    private lateinit var dataUsageView: DataUsageView

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != PACKAGE_SYSTEM_UI) {
            return
        }

        XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                dataUsageView.update()
            }
        })
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        throw UnsupportedOperationException()
    }
}