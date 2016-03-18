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

        val responseIntent = Intent(res.getString(R.string.action_settings_relay_response))
        responseIntent.apply {
            for ((key, pref) in prefs.all) {
                when (pref) {
                    is Boolean -> putExtra(key, pref)
                    is Float -> putExtra(key, pref)
                    is Int -> putExtra(key, pref)
                    is Long -> putExtra(key, pref)
                    is String -> putExtra(key, pref)
                    is Set<*> -> putExtra(key, arrayListOf(pref as Set<String>)) //we know this will succeed
                }
            }
        }


        context.sendBroadcast(responseIntent)
    }
}