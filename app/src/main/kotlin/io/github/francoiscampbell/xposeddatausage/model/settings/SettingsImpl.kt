package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Intent
import android.content.IntentFilter
import android.content.res.XModuleResources
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver
import java.util.*

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl : Settings {
    private val context = Module.hookedContext

    private val res = XModuleResources.createInstance(Module.modulePath, null)
    private val settingsUpdated = res.getString(R.string.action_settings_updated)

    private val settingsUpdateRequest = res.getString(R.string.action_settings_update_request)
    private val prefsBooleans = mutableMapOf<String, Boolean>()

    private val prefsFloats = mutableMapOf<String, Float>()
    private val prefsInts = mutableMapOf<String, Int>()
    private val prefsLongs = mutableMapOf<String, Long>()
    private val prefsStrings = mutableMapOf<String, String>()
    private val prefsStringSets = mutableMapOf<String, Set<String>>()

    private lateinit var settingsChangedListener: OnSettingsChangedListener

    private val unit: ByteFormatter.UnitFormat
        get() {
            val key = prefsStrings[res.getString(R.string.key_units)] ?: return ByteFormatter.UnitFormat.SMART_SI
            return ByteFormatter.UnitFormat.valueOf(key)
        }

    private val decimalPlaces: Int
        get() {
            val key = prefsStrings[res.getString(R.string.key_decimal_places)]
            return prefsInts[key] ?: 2
        }

    override fun update(listener: OnSettingsChangedListener) {
        settingsChangedListener = listener
        registerSettingsReceiver()
    }

    private fun registerSettingsReceiver() {
        context.registerReceiver(IntentFilter(settingsUpdated)) { context, intent ->
            val extras = intent.extras
            extras.keySet().forEach { key -> handleSettingChanged(key, extras.get(key)) }
        }
        context.sendBroadcast(Intent(settingsUpdateRequest))
    }

    private fun handleSettingChanged(key: String, newValue: Any?): Unit {
        saveNewValue(key, newValue)

        settingsChangedListener.run {
            XposedBridge.log("$key change received in Module")
            when (key) {
                res.getString(R.string.key_units) -> onUnitChanged(unit)
                res.getString(R.string.key_decimal_places) -> onDecimalPlacesChanged(decimalPlaces)
            }
        }
    }

    private fun saveNewValue(key: String, newValue: Any?) {
        when (newValue) {
            is Boolean -> prefsBooleans[key] = newValue
            is Float -> prefsFloats[key] = newValue
            is Int -> prefsInts[key] = newValue
            is Long -> prefsLongs[key] = newValue
            is String -> prefsStrings[key] = newValue
            is ArrayList<*> -> prefsStringSets[key] = newValue.toSet() as Set<String> //we know this will succeed
        }
    }
}