package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-17.
 */
class SettingsChangeBroadcaster(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val res = context.resources
    private val settingsChangedAction = res.getString(R.string.action_settings_changed)
    private val keyExtra = res.getString(R.string.key)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.i("SettingsBroadcaster", "$key change broadcast")
        context.sendBroadcast(Intent(settingsChangedAction).putExtra(keyExtra, key))
    }

    fun startBroadcastingChanges() = prefs.registerOnSharedPreferenceChangeListener(this)

    fun stopBroadcastingChanges() = prefs.unregisterOnSharedPreferenceChangeListener(this)
}