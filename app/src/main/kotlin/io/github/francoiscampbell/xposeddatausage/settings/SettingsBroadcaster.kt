package io.github.francoiscampbell.xposeddatausage.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-17.
 */
class SettingsBroadcaster(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val res = context.resources
    private val settingChanged = res.getString(R.string.app_name) + res.getString(R.string.action_settings_changed_suffix)
    private val extraKey = res.getString(R.string.key)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.i("SettingsBroadcaster", "$key change broadcast")
        context.sendBroadcast(Intent(settingChanged).putExtra(extraKey, key))
    }

    fun startBroadcasting() {
        prefs.registerOnSharedPreferenceChangeListener(this)
    }
}