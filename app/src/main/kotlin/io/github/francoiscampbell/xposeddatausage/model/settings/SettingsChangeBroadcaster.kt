package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.util.getRaw

/**
 * Created by francois on 16-03-17.
 */
class SettingsChangeBroadcaster(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val res = context.resources
    private val settingsChangedAction = res.getString(R.string.action_settings_updated)
    private val settingsUpdateRequestAction = res.getString(R.string.action_settings_update_request)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        context.sendBroadcast(Intent(settingsChangedAction).putExtra(key, sharedPreferences.getRaw(key)))
    }

    fun startBroadcastingChanges() {
        context.sendBroadcast(Intent(settingsUpdateRequestAction)) //trigger push settings to module
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    fun stopBroadcastingChanges() = prefs.unregisterOnSharedPreferenceChangeListener(this)
}