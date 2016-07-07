package io.github.francoiscampbell.xposeddatausage.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.XResources
import android.os.Parcelable
import android.view.View
import de.robv.android.xposed.callbacks.XC_LayoutInflated

/**
 * Created by francois on 16-03-13.
 */
fun XC_LayoutInflated.LayoutInflatedParam.findViewById(id: String): View = view.findViewById(res.getIdentifier(id, "id", res.packageName))

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

fun Intent.putAnyExtra(key: String, value: Any?): Intent {
    return when (value) {
        is Boolean -> putExtra(key, value)
        is Float -> putExtra(key, value)
        is Int -> putExtra(key, value)
        is Long -> putExtra(key, value)
        is String -> putExtra(key, value)
        is Array<*> -> {
            if (value.size > 0) {
                when (value[0]) {
                    is Parcelable -> putExtra(key, value)
                    is String -> putExtra(key, value)
                    is CharSequence -> putExtra(key, value)
                    else -> this
                }
            } else {
                this
            }
        }
        else -> this
    }
}