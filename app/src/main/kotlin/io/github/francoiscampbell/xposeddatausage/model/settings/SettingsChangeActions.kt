package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.settings.SettingsActivity
import io.github.francoiscampbell.xposeddatausage.util.putAnyExtra

/**
 * Created by francois on 16-03-17.
 */
class SettingsChangeActions(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val res = context.resources
    private val settingsUpdatedAction = res.getString(R.string.action_settings_updated)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val newPrefValue = sharedPreferences.all[key]
        XposedLog.i("$key changed to $newPrefValue in ${javaClass.simpleName}")

        if (isPrefForApp(key)) {
            handleAppPrefChange(key, newPrefValue)
        } else {
            context.sendBroadcast(Intent(settingsUpdatedAction).putAnyExtra(key, newPrefValue))
        }
    }

    fun startListeningForChanges() {
        XposedLog.i("startBroadcastingChanges")
        prefs.all.forEach { onSharedPreferenceChanged(prefs, it.key) } //push settings to module when opening the settings app
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    fun stopListeningForChanges() {
        XposedLog.i("stopBroadcastingChanges")
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun isPrefForApp(key: String) = key.startsWith("app_")

    private fun handleAppPrefChange(key: String, newValue: Any?) {
        when (key) {
            res.getString(R.string.pref_app_show_in_launcher_key) -> onShowInLauncherChanged(newValue as Boolean)
        }
    }

    private fun onShowInLauncherChanged(showInLauncher: Boolean) {
        val newStatus = when (showInLauncher) {
            true -> PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            false -> PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }

        val componentName = ComponentName(context, SettingsActivity::class.java.canonicalName + "-Alias")
        context.packageManager.setComponentEnabledSetting(componentName, newStatus, PackageManager.DONT_KILL_APP)
    }
}