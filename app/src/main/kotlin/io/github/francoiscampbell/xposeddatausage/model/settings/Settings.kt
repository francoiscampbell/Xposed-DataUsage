package io.github.francoiscampbell.xposeddatausage.model.settings

import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter


/**
 * Created by francois on 16-03-15.
 */
interface Settings {
    val unit: ByteFormatter.UnitFormat
    val decimalPlaces: Int
}