package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import io.github.francoiscampbell.xposeddatausage.BuildConfig
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.util.putAnyExtra

/**
 * Created by francois on 16-03-17.
 */
class SettingsRelay() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (BuildConfig.DEBUG) {
            Log.i("Xposed", "Received settings relay request in ${javaClass.simpleName}")
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val res = context.resources

        val responseIntent = Intent(res.getString(R.string.action_settings_updated))
        prefs.all.forEach {
            val prefValue = it.value
            if (BuildConfig.DEBUG) {
                Log.i("Xposed", "$it:$prefValue relayed to module by ${javaClass.simpleName}")
            }
            responseIntent.putAnyExtra(it.key, prefValue)
        }
        context.sendBroadcast(responseIntent)
    }
}