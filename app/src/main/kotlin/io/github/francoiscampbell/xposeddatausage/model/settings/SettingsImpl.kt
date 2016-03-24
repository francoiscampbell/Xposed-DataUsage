package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.IntentFilter
import android.content.res.XModuleResources
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.util.putAny
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl : Settings {
    private val context = Module.hookedContext
    private val res = XModuleResources.createInstance(Module.modulePath, null)

    private val settingsUpdatedAction = res.getString(R.string.action_settings_updated)

    private val prefsCache = res.getString(R.string.module_prefs_cache_name)
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
    }

    private fun handleSettingUpdate(key: String, newValue: Any?): Unit {
        XposedLog.i("$key is $newValue in ${javaClass.simpleName}")
        newValue ?: return
        settingsChangedListener.run {
            when (key) {
                res.getString(R.string.pref_only_when_mobile_key) -> onOnlyWhenMobileChanged(newValue as Boolean)
                res.getString(R.string.pref_relative_to_pace_key) -> onRelativeToPaceChanged(newValue as Boolean)
                res.getString(R.string.pref_units_key) -> onUnitChanged(DataUsageFormatter.UnitFormat.valueOf(newValue as String))
                res.getString(R.string.pref_decimal_places_key) -> onDecimalPlacesChanged((newValue as String).toInt())
                res.getString(R.string.pref_debug_logging_key) -> onDebugLoggingChanged(newValue as Boolean)
            }
        }

        XposedLog.i("Debug logging is ${XposedLog.debugLogging} in SettingsImpl")
    }

    override val onlyIfMobile: Boolean
        get() = prefs.getBoolean(res.getString(R.string.pref_only_when_mobile_key), true)

    override val relativeToPace: Boolean
        get() = prefs.getBoolean(res.getString(R.string.pref_relative_to_pace_key), false)
}