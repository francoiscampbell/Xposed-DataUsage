package io.github.francoiscampbell.xposeddatausage.model.settings

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
class SettingsImpl(private val settingsChangedListener: OnSettingsChangedListener) : Settings {
    private val context = Module.hookedContext
    private val res = XModuleResources.createInstance(Module.modulePath, null)

    private val settingsChanged = res.getString(R.string.action_settings_changed)
    private val settingsRelayRequest = res.getString(R.string.action_settings_relay_request)

    private val extraKey = res.getString(R.string.key)

    private val prefsBooleans = mutableMapOf<String, Boolean>()
    private val prefsFloats = mutableMapOf<String, Float>()
    private val prefsInts = mutableMapOf<String, Int>()
    private val prefsLongs = mutableMapOf<String, Long>()
    private val prefsStrings = mutableMapOf<String, String>()
    private val prefsStringSets = mutableMapOf<String, Set<String>>()

    override val unit: ByteFormatter.UnitFormat
        get() {
            val key = prefsStrings[res.getString(R.string.key_units)] ?: return ByteFormatter.UnitFormat.SMART_SI
            return ByteFormatter.UnitFormat.valueOf(key)
        }

    override val decimalPlaces: Int
        get() {
            val key = prefsStrings[res.getString(R.string.key_decimal_places)]
            return prefsInts[key] ?: 2
        }

    init {
        getRelayedPreferences()
        registerSettingsChangeReceiver()

    }

    private fun getRelayedPreferences() = context.registerReceiver(IntentFilter(settingsRelayRequest)) { context, intent ->
        val extras = intent.extras
        for (key in extras.keySet()) {
            val pref = extras.get(key)
            when (pref) {
                is Boolean -> prefsBooleans[key] = pref
                is Float -> prefsFloats[key] = pref
                is Int -> prefsInts[key] = pref
                is Long -> prefsLongs[key] = pref
                is String -> prefsStrings[key] = pref
                is ArrayList<*> -> {
                    val map = mutableMapOf<Boolean, Any?>()
                    pref.associateByTo(map) { it is String } //all Strings end up in the map under the key 'true'

                    val strings = map[true] as ArrayList<String> //unchecked cast ok, we checked for String-ness
                    prefsStringSets[key] = strings.toSet()
                }
            }
        }
    }

    private fun registerSettingsChangeReceiver() = context.registerReceiver(IntentFilter(settingsChanged)) { context, intent ->
        handleSettingChanged(intent.getStringExtra(extraKey))
    }


    private fun handleSettingChanged(key: String): Unit {
        settingsChangedListener.run {
            XposedBridge.log("$key change received in Module")
            when (key) {
                res.getString(R.string.key_units) -> onUnitChanged(unit)
                res.getString(R.string.key_decimal_places) -> onDecimalPlacesChanged(decimalPlaces)
            }
        }
    }
}