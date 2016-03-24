package io.github.francoiscampbell.xposeddatausage

import android.app.Application
import android.os.Process
import io.github.francoiscampbell.xposeddatausage.log.XposedLog

/**
 * Created by francois on 16-03-19.
 */
class DataUsageApplication : Application() {
    companion object {
        init {
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                XposedLog.e("Thread ${thread.name} threw uncaught exception", throwable)
                Process.killProcess(Process.myPid())
            }
        }
    }
}