package io.github.francoiscampbell.xposeddatausage.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-15.
 */
class SettingsFragment : PreferenceFragment(), SettingsView {
    companion object {
        fun newInstance(args: Bundle): SettingsFragment {
            val frag = SettingsFragment()
            frag.arguments = args
            return frag
        }

        fun newInstance() = newInstance(Bundle.EMPTY)
    }

    private val presenter = SettingsPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)
    }
}