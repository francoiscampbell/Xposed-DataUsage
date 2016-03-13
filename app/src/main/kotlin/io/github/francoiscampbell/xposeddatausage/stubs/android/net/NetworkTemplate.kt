package io.github.francoiscampbell.xposeddatausage.stubs.android.net

import de.robv.android.xposed.XposedHelpers

/**
 * Created by francois on 16-03-13.
 */
object NetworkTemplate {
    val stubClass = XposedHelpers.findClass("android.net.NetworkTemplate", null)

    fun buildTemplateMobileAll(subscriberId: String) = XposedHelpers.callStaticMethod(stubClass, "buildTemplateMobileAll", subscriberId)
}