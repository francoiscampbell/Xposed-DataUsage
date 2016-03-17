package io.github.francoiscampbell.xposeddatausage.model.settings

import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter

/**
 * Created by francois on 16-03-17.
 */
interface OnSettingsChangedListener {
    fun onUnitChanged(newUnit: ByteFormatter.UnitFormat)
    fun onDecimalPlacesChanged(newDecimalPlaces: Int)
}