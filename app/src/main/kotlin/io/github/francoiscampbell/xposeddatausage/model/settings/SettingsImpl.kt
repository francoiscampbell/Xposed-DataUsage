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

    private val settingsUpdatedAction = res.getString(R.string.action_settings_updated)
    private val settingsUpdateRequestAction = res.getString(R.string.action_settings_update_request)

    private val prefsCache = res.getString(R.string.action_settings_update_request) + "_preferences"
    private val prefs = context.getSharedPreferences(prefsCache, Context.MODE_PRIVATE)

    private lateinit var settingsChangedListener: OnSettingsChangedListener

    override fun update(listener: OnSettingsChangedListener) {
        settingsChangedListener = listener
        sendAllSettings()
        registerSettingsReceiver()
    }

    private fun sendAllSettings() {
        prefs.all.filter { it.value is String }.forEach { handleSettingUpdate(it.key, it.value as String) }
    }

    private fun registerSettingsReceiver() {
        context.registerReceiver(IntentFilter(settingsUpdatedAction)) { context, intent ->
            val extras = intent.extras

            val editor = prefs.edit()
            extras.keySet().forEach {
                val newPrefValue = extras.getString(it)
                editor.putString(it, newPrefValue)
                handleSettingUpdate(it, newPrefValue)
            }
            editor.apply()
        }
        context.sendBroadcast(Intent(settingsUpdateRequestAction))
    }

    private fun handleSettingUpdate(key: String, newValue: String): Unit {
        XposedBridge.log("$key changed to $newValue in ${javaClass.simpleName}")
        settingsChangedListener.run {
            when (key) {
                res.getString(R.string.key_units) -> onUnitChanged(ByteFormatter.UnitFormat.valueOf(newValue))
                res.getString(R.string.key_decimal_places) -> onDecimalPlacesChanged(newValue.toInt())
            }
        }
    }
}