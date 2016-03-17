package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import io.github.francoiscampbell.xposeddatausage.DataUsageApplication
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter

/**
 * Created by francois on 16-03-15.
 */
class SettingsImpl(private val settingsChangedListener: OnSettingsChangedListener) : Settings {
    private val context = DataUsageApplication.context
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val res = context.resources

    private val onSharedPreferencesChangedListener = { sharedPrefs: SharedPreferences, key: String ->
        Log.i("SettingsImpl", "Pref changed: $key")
        settingsChangedListener.run {
            when (key) {
                res.getString(R.string.key_units) -> onUnitChanged(unit)
                res.getString(R.string.key_decimal_places) -> onDecimalPlacesChanged(decimalPlaces)
            }
        }
    }

    init {
        prefs.registerOnSharedPreferenceChangeListener(onSharedPreferencesChangedListener)
    }

    override val unit: ByteFormatter.UnitFormat
        get() = ByteFormatter.UnitFormat.valueOf(
                prefs.getString(
                        res.getString(R.string.key_units),
                        ByteFormatter.UnitFormat.SMART_SI.name))

    override val decimalPlaces: Int
        get() = prefs.getString(res.getString(R.string.key_decimal_places), "2").toInt()
}