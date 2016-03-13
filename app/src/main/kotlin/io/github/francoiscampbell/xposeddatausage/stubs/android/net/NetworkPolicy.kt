package io.github.francoiscampbell.xposeddatausage.stubs.android.net

import de.robv.android.xposed.XposedHelpers

/**
 * Created by francois on 16-03-13.
 */
object NetworkPolicy {
    val stubClass = XposedHelpers.findClass("android.net.NetworkPolicy", null)
}