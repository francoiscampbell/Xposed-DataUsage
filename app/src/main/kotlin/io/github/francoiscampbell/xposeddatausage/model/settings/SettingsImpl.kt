package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.IntentFilter
import android.content.res.XModuleResources
import android.graphics.Color
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver
import io.github.francoiscampbell.xposeddatausage.widget.Alignment
import io.github.francoiscampbell.xposeddatausage.widget.Position
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl @Inject constructor(
        @Named("app") private val context: Context,
        private val res: XModuleResources
) : Settings {
    private val settingsUpdatedAction = res.getString(R.string.action_settings_updated)
    private lateinit var settingsChangedListener: OnSettingsChangedListener

    override fun attach(listener: OnSettingsChangedListener) {
        settingsChangedListener = listener
        registerSettingsReceiver()
    }

    private fun registerSettingsReceiver() {
        context.registerReceiver(IntentFilter(settingsUpdatedAction)) { context, intent ->
            intent.extras?.keySet()?.forEach {
                val newPrefValue = intent.extras.get(it)
                handleSettingUpdate(it, newPrefValue)
            }
        }
    }

    private fun handleSettingUpdate(key: String, newValue: Any?): Unit {
        XposedLog.d("$key is $newValue in ${javaClass.simpleName}")
        if (newValue == null) return
        settingsChangedListener.run {
            @Suppress("UNCHECKED_CAST")
            when (key) {
                res.getString(R.string.pref_monitored_network_default_key) -> onDefaultMonitoredNetworkTypeChanged(NetworkManager.NetworkType.valueOf(newValue as String))
                res.getString(R.string.pref_monitored_network_types_key) -> {
                    when (newValue) {
                        is Array<*> -> onMonitoredNetworkTypesChanged(networkTypeNamesToEnum((newValue as Array<String>).toSet())) //if coming from Intent
                        else -> onMonitoredNetworkTypesChanged(networkTypeNamesToEnum(newValue as Set<String>)) //if coming directly from prefs
                    }
                }
                res.getString(R.string.pref_relative_to_pace_key) -> onRelativeToPaceChanged(newValue as Boolean)
                res.getString(R.string.pref_units_key) -> onUnitChanged(DataUsageFormatter.UnitFormat.valueOf(newValue as String))
                res.getString(R.string.pref_decimal_places_key) -> onDecimalPlacesChanged((newValue as String).toInt())
                res.getString(R.string.pref_two_lines_key) -> onTwoLinesChanged(newValue as Boolean)
                res.getString(R.string.pref_position_key) -> onPositionChanged(Position.valueOf(newValue as String))
                res.getString(R.string.pref_alignment_key) -> onAlignmentChanged(Alignment.valueOf(newValue as String))
                res.getString(R.string.pref_text_size_key) -> onTextSizeChanged((newValue as String).run { if (isEmpty()) 0f else toFloat() })
                res.getString(R.string.pref_use_custom_text_color_key) -> onUseCustomTextColorChanged(newValue as Boolean)
                res.getString(R.string.pref_custom_text_color_key) -> onCustomTextColorChanged(newValue as Int)
                res.getString(R.string.pref_custom_text_color_hex_code_key) -> validateTextColor(newValue as String)
                res.getString(R.string.pref_use_override_text_color_high_usage_key) -> onUseOverrideTextColorHighUsageChanged(newValue as Boolean)
                res.getString(R.string.pref_debug_logging_key) -> onDebugLoggingChanged(newValue as Boolean)
            }
        }
    }

    private fun OnSettingsChangedListener.validateTextColor(newHexCode: String) {
        val colorHexCode = newHexCode.replace(Regex("[^A-F0-9]+"), "")
        val color = try {
            Color.parseColor("#$colorHexCode")
        } catch (e: IllegalArgumentException) {
            Color.WHITE
        }
        onCustomTextColorChanged(color)
    }

    private fun networkTypeNamesToEnum(networkTypeNames: Set<String>): Set<NetworkManager.NetworkType> {
        return networkTypeNames.map { NetworkManager.NetworkType.valueOf(it) }.toSet()
    }
}