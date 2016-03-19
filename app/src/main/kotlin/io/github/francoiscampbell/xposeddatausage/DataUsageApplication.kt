package io.github.francoiscampbell.xposeddatausage

import android.app.Application
import android.os.Process
import android.util.Log

/**
 * Created by francois on 16-03-19.
 */
class DataUsageApplication : Application() {
    companion object {
        init {
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                Log.e("Xposed", "Thread ${thread.name} threw uncaught exception", throwable)
                Process.killProcess(Process.myPid());
            }
        }
    }
}