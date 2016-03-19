package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.XModuleResources
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.BuildConfig
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.util.putAny
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
        prefs.all.forEach { handleSettingUpdate(it.key, it.value) }
    }

    private fun registerSettingsReceiver() {
        context.registerReceiver(IntentFilter(settingsUpdatedAction)) { context, intent ->
            val extras = intent.extras

            val editor = prefs.edit()
            extras.keySet().forEach {
                val newPrefValue = extras.get(it)
                editor.putAny(it, newPrefValue)
                handleSettingUpdate(it, newPrefValue)
            }
            editor.apply()
        }
        context.sendBroadcast(Intent(settingsUpdateRequestAction))
    }

    private fun handleSettingUpdate(key: String, newValue: Any?): Unit {
        if (BuildConfig.DEBUG) {
            XposedBridge.log("$key changed to $newValue in ${javaClass.simpleName}")
        }
        newValue ?: return
        settingsChangedListener.run {
            when (key) {
                res.getString(R.string.pref_only_when_mobile_key) -> onOnlyWhenMobileChanged(newValue as Boolean)
                res.getString(R.string.pref_units_key) -> onUnitChanged(ByteFormatter.UnitFormat.valueOf(newValue as String))
                res.getString(R.string.pref_decimal_places_key) -> onDecimalPlacesChanged((newValue as String).toInt())
            }
        }
    }

    override val onlyIfMobile: Boolean
        get() = prefs.getBoolean(res.getString(R.string.pref_only_when_mobile_key), false)
}