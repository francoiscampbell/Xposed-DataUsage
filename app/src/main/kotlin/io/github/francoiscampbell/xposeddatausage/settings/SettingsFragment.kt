package io.github.francoiscampbell.xposeddatausage.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsChangeBroadcaster

/**
 * Created by francois on 16-03-15.
 */
class SettingsFragment : PreferenceFragment() {
    private lateinit var settingsChangeBroadcaster: SettingsChangeBroadcaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsChangeBroadcaster = SettingsChangeBroadcaster(context)
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onResume() {
        super.onResume()

        settingsChangeBroadcaster.startBroadcastingChanges()
    }

    override fun onPause() {
        super.onPause()

        settingsChangeBroadcaster.stopBroadcastingChanges()
    }
}