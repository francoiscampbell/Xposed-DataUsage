package io.github.francoiscampbell.xposeddatausage.settings

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsChangeBroadcaster

/**
 * Created by francois on 16-03-15.
 */
class SettingsFragment : PreferenceFragment() {
    private lateinit var settingsChangeBroadcaster: SettingsChangeBroadcaster

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingsChangeBroadcaster = SettingsChangeBroadcaster(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.prefs)
        settingsChangeBroadcaster.startBroadcastingChanges()
    }

    override fun onPause() {
        settingsChangeBroadcaster.stopBroadcastingChanges()
    }
}