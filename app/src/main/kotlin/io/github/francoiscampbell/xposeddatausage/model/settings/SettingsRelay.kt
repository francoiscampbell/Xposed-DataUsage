package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-17.
 */
class SettingsRelay() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val res = context.resources

        val responseIntent = Intent(res.getString(R.string.action_settings_updated))
        prefs.all.forEach { responseIntent.putExtra(it.key, it.value.toString()) }

        context.sendBroadcast(responseIntent)
    }
}