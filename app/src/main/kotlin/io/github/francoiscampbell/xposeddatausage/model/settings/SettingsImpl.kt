package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.res.XModuleResources
import android.graphics.Color
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.util.putAny
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver
import io.github.francoiscampbell.xposeddatausage.widget.Alignment
import io.github.francoiscampbell.xposeddatausage.widget.Position
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl
@Inject constructor(
        @Named("app") private val context: Context,
        private val res: XModuleResources,
        private val prefs: SharedPreferences
) : Settings {
    private val settingsUpdatedAction = res.getString(R.string.action_settings_updated)
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
        if (newValue == null) return
        settingsChangedListener.run {
            when (key) {
                res.getString(R.string.pref_only_when_mobile_key) -> onOnlyWhenMobileChanged(newValue as Boolean)
                res.getString(R.string.pref_relative_to_pace_key) -> onRelativeToPaceChanged(newValue as Boolean)
                res.getString(R.string.pref_units_key) -> onUnitChanged(DataUsageFormatter.UnitFormat.valueOf(newValue as String))
                res.getString(R.string.pref_decimal_places_key) -> onDecimalPlacesChanged((newValue as String).toInt())
                res.getString(R.string.pref_two_lines_key) -> onTwoLinesChanged(newValue as Boolean)
                res.getString(R.string.pref_position_key) -> onPositionChanged(Position.valueOf(newValue as String))
                res.getString(R.string.pref_alignment_key) -> onAlignmentChanged(Alignment.valueOf(newValue as String))
                res.getString(R.string.pref_text_size_key) -> onTextSizeChanged((newValue as String).run { if (isEmpty()) 0f else toFloat() })
                res.getString(R.string.pref_use_custom_text_color_key) -> onUseCustomTextColorChanged(newValue as Boolean)
                res.getString(R.string.pref_custom_text_color_key) -> onCustomTextColorChanged(newValue as Int)
                res.getString(R.string.pref_custom_text_color_hex_code_key) -> {
                    val colorHexCode = (newValue as String).replace(Regex("[^A-F0-9]+"), "")
                    val color = try {
                        Color.parseColor("#$colorHexCode")
                    } catch (e: IllegalArgumentException) {
                        Color.WHITE
                    }
                    onCustomTextColorChanged(color)
                }
                res.getString(R.string.pref_use_override_text_color_high_usage_key) -> onUseOverrideTextColorHighUsageChanged(newValue as Boolean)
                res.getString(R.string.pref_debug_logging_key) -> onDebugLoggingChanged(newValue as Boolean)
            }
        }

        XposedLog.i("Debug logging is ${XposedLog.debugLogging} in SettingsImpl")
    }

    override val onlyIfMobile: Boolean
        get() = prefs.getBoolean(res.getString(R.string.pref_only_when_mobile_key), true)
}