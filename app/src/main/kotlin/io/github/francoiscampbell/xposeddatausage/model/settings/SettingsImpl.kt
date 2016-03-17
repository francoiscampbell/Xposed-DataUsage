package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.IntentFilter
import android.content.res.XModuleResources
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl(private val settingsChangedListener: OnSettingsChangedListener) : Settings {
    private val prefs = XSharedPreferences(Module.PACKAGE_SYSTEM_UI)
    private val res = XModuleResources.createInstance(Module.modulePath, null)
    private val settingsChanged = res.getString(R.string.app_name) + res.getString(R.string.action_settings_changed_suffix)
    private val extraKey = res.getString(R.string.key)

    override val unit: ByteFormatter.UnitFormat
        get() = ByteFormatter.UnitFormat.valueOf(
                prefs.getString(
                        res.getString(R.string.key_units),
                        ByteFormatter.UnitFormat.SMART_SI.name))

    override val decimalPlaces: Int
        get() = prefs.getString(res.getString(R.string.key_decimal_places), "2").toInt()

    init {
        registerSettingsChangeReceiver()
    }

    private fun registerSettingsChangeReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(settingsChanged)
        Module.hookedContext.registerReceiver(intentFilter) { context, intent ->
            handleSettingChanged(intent.getStringExtra(extraKey))
        }
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