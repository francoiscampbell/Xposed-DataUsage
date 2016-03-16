package io.github.francoiscampbell.xposeddatausage.settings

import android.os.Bundle
import android.preference.PreferenceFragment

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

    private val presenter = SettingsPresenterImpl(activity)


}