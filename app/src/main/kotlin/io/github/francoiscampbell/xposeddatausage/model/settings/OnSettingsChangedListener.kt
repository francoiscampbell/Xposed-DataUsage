package io.github.francoiscampbell.xposeddatausage.model.settings

import android.support.annotation.ColorInt
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.widget.Alignment
import io.github.francoiscampbell.xposeddatausage.widget.Position

/**
 * Created by francois on 16-03-17.
 */
interface OnSettingsChangedListener {
    fun onDefaultMonitoredNetworkTypeChanged(defaultMonitoredNetworkType: NetworkManager.NetworkType)
    fun onMonitoredNetworkTypesChanged(monitoredNetworkTypes: Set<NetworkManager.NetworkType>)
    fun onRelativeToPaceChanged(relativeToPace: Boolean)
    fun onUnitChanged(newUnit: DataUsageFormatter.UnitFormat)
    fun onDecimalPlacesChanged(newDecimalPlaces: Int)
    fun onTwoLinesChanged(showOnTwoLines: Boolean)
    fun onPositionChanged(newPosition: Position)
    fun onAlignmentChanged(newAlignment: Alignment)
    fun onTextSizeChanged(newTextSize: Float)
    fun onUseCustomTextColorChanged(useCustomTextColor: Boolean)
    fun onCustomTextColorChanged(@ColorInt newTextColor: Int)
    fun onUseOverrideTextColorHighUsageChanged(useOverride: Boolean)
    fun onDebugLoggingChanged(shouldDebugLog: Boolean)
}