package io.github.francoiscampbell.xposeddatausage.util

import android.content.res.XResources
import de.robv.android.xposed.callbacks.XC_LayoutInflated

/**
 * Created by francois on 16-03-13.
 */
fun XC_LayoutInflated.LayoutInflatedParam.findViewById(id: String) = view.findViewById(res.getIdentifier(id, "id", res.packageName))

fun XResources.hookLayout(pkg: String, type: String, name: String, callback: (XC_LayoutInflated.LayoutInflatedParam) -> Unit)
        = hookLayout(pkg, type, name, object : XC_LayoutInflated() {
    override fun handleLayoutInflated(liparam: LayoutInflatedParam) {
        callback(liparam)
    }
})