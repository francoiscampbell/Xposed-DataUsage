package io.github.francoiscampbell.xposeddatausage.log

import android.util.Log
import io.github.francoiscampbell.xposeddatausage.BuildConfig

/**
 * Created by francois on 16-03-21.
 */
object XposedLog {
    private val xposedTag = "Xposed"
    private val tag = BuildConfig.APPLICATION_ID.substringAfterLast('.')

    var debugLogging = false

    fun i(message: String) {
        Log.i(xposedTag, "$tag/$message")
    }

    fun d(message: String) {
        if (BuildConfig.DEBUG || debugLogging) {
            Log.i(xposedTag, "$tag/$message")
        }
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG || debugLogging) {
            Log.e(xposedTag, "$tag/$message/${Log.getStackTraceString(throwable)}")
        } else {
            Log.e(xposedTag, "$tag/$message")
        }
    }
}