package io.github.francoiscampbell.xposeddatausage.settings

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-15.
 */
class SettingsFragment : PreferenceFragment() {
    companion object {
        fun newInstance(args: Bundle): SettingsFragment {
            val frag = SettingsFragment()
            frag.arguments = args
            return frag
        }

        fun newInstance() = newInstance(Bundle.EMPTY)
    }

    private lateinit var settingsBroadcaster: SettingsBroadcaster

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingsBroadcaster = SettingsBroadcaster(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)
        settingsBroadcaster.startBroadcasting()
    }
}