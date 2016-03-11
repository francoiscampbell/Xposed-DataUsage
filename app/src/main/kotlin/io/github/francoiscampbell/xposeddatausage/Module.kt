package io.github.francoiscampbell.xposeddatausage

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by francois on 16-03-11.
 */
class Module : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        throw UnsupportedOperationException()
    }
}