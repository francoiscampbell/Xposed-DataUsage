package io.github.francoiscampbell.xposeddatausage

import android.app.Application
import android.content.Context

/**
 * Created by francois on 16-03-16.
 */
class DataUsageApplication() : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = this
    }
}