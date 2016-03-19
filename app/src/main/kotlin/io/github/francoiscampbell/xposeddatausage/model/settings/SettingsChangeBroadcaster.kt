package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import io.github.francoiscampbell.xposeddatausage.BuildConfig
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-17.
 */
class SettingsChangeBroadcaster(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val res = context.resources
    private val settingsUpdatedAction = res.getString(R.string.action_settings_updated)
    private val settingsUpdateRequestAction = res.getString(R.string.action_settings_update_request)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val newPrefValue = sharedPreferences.getString(key, "")
        if (BuildConfig.DEBUG) {
            Log.i("Xposed", "$key changed to $newPrefValue in ${javaClass.simpleName}")
        }
        context.sendBroadcast(Intent(settingsUpdatedAction).putExtra(key, newPrefValue))
    }

    fun startBroadcastingChanges() {
        if (BuildConfig.DEBUG) {
            Log.i("Xposed", "startBroadcastingChanges")
        }
        context.sendBroadcast(Intent(settingsUpdateRequestAction)) //trigger push settings to module
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    fun stopBroadcastingChanges() {
        if (BuildConfig.DEBUG) {
            Log.i("Xposed", "stopBroadcastingChanges")
        }
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}