package io.github.francoiscampbell.xposeddatausage.model.settings

import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.widget.Alignment
import io.github.francoiscampbell.xposeddatausage.widget.Position

/**
 * Created by francois on 16-03-17.
 */
interface OnSettingsChangedListener {
    fun onOnlyWhenMobileChanged(onlyWhenMobile: Boolean)
    fun onRelativeToPaceChanged(relativeToPace: Boolean)
    fun onUnitChanged(newUnit: DataUsageFormatter.UnitFormat)
    fun onDecimalPlacesChanged(newDecimalPlaces: Int)
    fun onTwoLinesChanged(newTwoLines: Boolean)
    fun onPositionChanged(newPosition: Position)
    fun onAlignmentChanged(newAlignment: Alignment)
    fun onTextSizeChanged(newTextSize: Float)
    fun onDebugLoggingChanged(shouldDebugLog: Boolean)
}