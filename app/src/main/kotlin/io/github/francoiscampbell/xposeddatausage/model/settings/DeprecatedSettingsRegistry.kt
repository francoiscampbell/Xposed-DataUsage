package io.github.francoiscampbell.xposeddatausage.model.settings

import android.content.SharedPreferences
import android.content.res.Resources
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.util.batchEdit
import io.github.francoiscampbell.xposeddatausage.util.putAny
import javax.inject.Inject

/**
 * Created by francois on 16-05-17.
 */
class DeprecatedSettingsRegistry
@Inject constructor(
        private val res: Resources,
        private val prefs: SharedPreferences) {

    /**
     * Use String literals instead of resources in case the key changes in a new release
     */
    fun getDeprecationReplacement(key: String, value: Any?): DeprecatedSettingReplacement {
        return when (key) {
            "units" -> getReplacementForUnits(key, value as String)
            else -> DeprecatedSettingReplacement(key, emptyMap())
        }
    }

    fun updateDeprecatedSettings() {
        prefs.batchEdit { editor ->
            all.map { getDeprecationReplacement(it.key, it.value) }
                    .filter { it.replacementSettings.isNotEmpty() }
                    .forEach { deprecationReplacement ->
                        if (deprecationReplacement.oldKey !in deprecationReplacement.replacementSettings.keys) {
                            editor.remove(deprecationReplacement.oldKey)
                        }
                        deprecationReplacement.replacementSettings.forEach { replacementSetting ->
                            editor.putAny(replacementSetting.key, replacementSetting.value)
                        }
                    }
        }
    }

    private fun getReplacementForUnits(oldKey: String, value: String): DeprecatedSettingReplacement {
        val unitsKey = res.getString(R.string.pref_units_key) //The new key may be different
        val binShowKey = res.getString(R.string.pref_bin_show_key)
        val relWarningKey = res.getString(R.string.pref_relative_to_warning_key)

        val replacements = when (value) {
            "SMART_SI" -> mapOf(unitsKey to "SMART", binShowKey to true)
            "KIBI" -> mapOf(unitsKey to "KILO", binShowKey to true)
            "MEBI" -> mapOf(unitsKey to "MEGA", binShowKey to true)
            "GIBI" -> mapOf(unitsKey to "GIGA", binShowKey to true)
            "PCT_LIMIT" -> mapOf(unitsKey to "PCT")
            "PCT_WARNING" -> mapOf(unitsKey to "PCT", relWarningKey to true)
            else -> emptyMap() //No replacements for others
        }

        return DeprecatedSettingReplacement(oldKey, replacements)
    }

    data class DeprecatedSettingReplacement(val oldKey: String, val replacementSettings: Map<String, Any?>)
}