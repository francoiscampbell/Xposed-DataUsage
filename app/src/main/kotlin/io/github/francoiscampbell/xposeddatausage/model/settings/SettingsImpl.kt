package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.XModuleResources
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl : Settings {
    private val context = Module.hookedContext
    private val res = XModuleResources.createInstance(Module.modulePath, null)

    private val settingsUpdated = res.getString(R.string.action_settings_updated)
    private val settingsUpdateRequest = res.getString(R.string.action_settings_update_request)

    private val prefsCache = res.getString(R.string.action_settings_update_request) + "_preferences"
    private val prefs = context.getSharedPreferences(prefsCache, Context.MODE_PRIVATE)

    private lateinit var settingsChangedListener: OnSettingsChangedListener

    override fun update(listener: OnSettingsChangedListener) {
        settingsChangedListener = listener
        sendSettingsToListener(prefs.all.keys)

        registerSettingsReceiver()
    }

    private fun registerSettingsReceiver() {
        context.registerReceiver(IntentFilter(settingsUpdated)) { context, intent ->
            val extras = intent.extras

            //save all prefs first
            val editor = prefs.edit()
            val changedKeys = extras.keySet()
            changedKeys.forEach { editor.putString(it, extras.getString(it)) }
            editor.apply()

            sendSettingsToListener(changedKeys)
        }
        context.sendBroadcast(Intent(settingsUpdateRequest))
        XposedBridge.log("Sending settings request to SettingsBroadcaster")
    }

    private fun sendSettingsToListener(keys: Set<String>) {
        keys.forEach { handleSettingUpdate(it) }
    }

    private fun handleSettingUpdate(key: String): Unit {
        settingsChangedListener.run {
            XposedBridge.log("$key change received in module")
            when (key) {
                res.getString(R.string.key_units) -> onUnitChanged(unit)
                res.getString(R.string.key_decimal_places) -> onDecimalPlacesChanged(decimalPlaces)
            }
        }
    }

    private val unit: ByteFormatter.UnitFormat
        get() {
            val formatName = prefs.getString(res.getString(R.string.key_units), ByteFormatter.UnitFormat.SMART_SI.name)
            return ByteFormatter.UnitFormat.valueOf(formatName)
        }

    private val decimalPlaces: Int
        get() {
            return prefs.getString(res.getString(R.string.key_decimal_places), "2").toInt()
        }
}