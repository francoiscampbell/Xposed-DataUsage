package io.github.francoiscampbell.xposeddatausage.nonmodule

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.log.XposedLog

/**
 * Created by francois on 16-03-15.
 */
class SettingsFragment : PreferenceFragment() {
    private lateinit var settingsChangeActions: SettingsChangeActions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context.applicationContext
            else -> activity.applicationContext
        }
        settingsChangeActions = SettingsChangeActions(context)

        @Suppress("DEPRECATION")
        preferenceManager.sharedPreferencesMode = Context.MODE_WORLD_READABLE

        addPreferencesFromResource(R.xml.prefs)
        PreferenceManager.setDefaultValues(context, R.xml.prefs, false)
    }

    override fun onResume() {
        super.onResume()

        settingsChangeActions.startListeningForChanges()

        XposedLog.i("Debug logging is ${XposedLog.debugLogging} in SettingsFragment")
    }

    override fun onPause() {
        super.onPause()

        settingsChangeActions.stopListeningForChanges()
    }
}