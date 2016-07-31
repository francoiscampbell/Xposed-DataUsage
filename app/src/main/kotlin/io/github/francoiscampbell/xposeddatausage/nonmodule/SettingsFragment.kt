package io.github.francoiscampbell.xposeddatausage.nonmodule

import android.app.Activity
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
    private lateinit var interactionListener: OnSettingsFragmentInteractionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context.applicationContext
            else -> activity.applicationContext
        }
        settingsChangeActions = SettingsChangeActions(context)

        addPreferencesFromResource(R.xml.prefs)
        PreferenceManager.setDefaultValues(context, R.xml.prefs, false)

        val backupPref = findPreference(resources.getString(R.string.pref_backup_data_usage_key))
        backupPref.setOnPreferenceClickListener {
            interactionListener.onBackup()
            return@setOnPreferenceClickListener true
        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        if (activity is OnSettingsFragmentInteractionListener) {
            interactionListener = activity
        } else {
            throw ClassCastException("${activity.toString()} must implement OnSettingsFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        settingsChangeActions.startListeningForChanges()

        XposedLog.d("Debug logging is ${XposedLog.debugLogging} in SettingsFragment")
    }

    override fun onPause() {
        super.onPause()

        settingsChangeActions.stopListeningForChanges()
    }

    interface OnSettingsFragmentInteractionListener {
        fun onBackup()
        fun onRestore()
    }
}