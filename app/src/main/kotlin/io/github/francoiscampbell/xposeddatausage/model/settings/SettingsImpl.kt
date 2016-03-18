package io.github.francoiscampbell.xposeddatausage.model.settings

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

    private val prefsMap = mutableMapOf<String, String>()

    private lateinit var settingsChangedListener: OnSettingsChangedListener

    private val unit: ByteFormatter.UnitFormat
        get() {
            val formatName = prefsMap[res.getString(R.string.key_units)] ?: return ByteFormatter.UnitFormat.SMART_SI
            return ByteFormatter.UnitFormat.valueOf(formatName)
        }

    private val decimalPlaces: Int
        get() {
            val key = res.getString(R.string.key_decimal_places)
            return prefsMap[key]?.toInt() ?: 2
        }

    override fun update(listener: OnSettingsChangedListener) {
        settingsChangedListener = listener
        registerSettingsReceiver()
    }

    private fun registerSettingsReceiver() {
        context.registerReceiver(IntentFilter(settingsUpdated)) { context, intent ->
            val extras = intent.extras
            extras.keySet().forEach { handleSettingChanged(it, extras.getString(it)) }
        }
        context.sendBroadcast(Intent(settingsUpdateRequest))
        XposedBridge.log("Sending settings request to SettingsBroadcaster")
    }

    private fun handleSettingChanged(key: String, newValue: String): Unit {
        prefsMap[key] = newValue

        settingsChangedListener.run {
            XposedBridge.log("$key change received in module")
            when (key) {
                res.getString(R.string.key_units) -> onUnitChanged(unit)
                res.getString(R.string.key_decimal_places) -> onDecimalPlacesChanged(decimalPlaces)
            }
        }
    }
}