package io.github.francoiscampbell.xposeddatausage.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.XResources
import android.util.Log
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

fun Context.registerReceiver(intentFilter: IntentFilter, receiver: (Context, Intent) -> Unit)
        = registerReceiver(object : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        receiver(p0, p1)
    }
}, intentFilter)

fun Log.x(text: String) = Log.i("Xposed", text)