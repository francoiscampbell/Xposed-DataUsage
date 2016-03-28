package io.github.francoiscampbell.xposeddatausage.model.settings

import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter

/**
 * Created by francois on 16-03-17.
 */
interface OnSettingsChangedListener {
    fun onOnlyWhenMobileChanged(onlyWhenMobile: Boolean)
    fun onRelativeToPaceChanged(relativeToPace: Boolean)
    fun onUnitChanged(newUnit: DataUsageFormatter.UnitFormat)
    fun onDecimalPlacesChanged(newDecimalPlaces: Int)
    fun onTwoLinesChanged(newTwoLines: Boolean)
    fun onDebugLoggingChanged(shouldDebugLog: Boolean)
}