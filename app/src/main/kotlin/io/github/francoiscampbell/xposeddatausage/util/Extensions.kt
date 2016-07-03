package io.github.francoiscampbell.xposeddatausage.util

import android.content.*
import android.content.res.XResources
import android.os.Parcelable
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

fun SharedPreferences.Editor.putAny(key: String, value: Any?): SharedPreferences.Editor {
    return when (value) {
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is String -> putString(key, value)
        else -> this
    }
}

fun SharedPreferences.batchEdit(block: SharedPreferences.(editor: SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    block(editor)
    editor.apply()
}