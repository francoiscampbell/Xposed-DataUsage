package io.github.francoiscampbell.xposeddatausage.model.usage

import android.graphics.Color

/**
 * Created by francois on 16-03-11.
 */
class DataUsageFormatter(var format: UnitFormat = DataUsageFormatter.UnitFormat.SMART_SI,
                         var decimalPlaces: Int = 2,
                         var relativeToPace: Boolean = false) {
    fun format(dataUsage: DataUsageFetcher.DataUsage) = formatForPace(dataUsage).run {
        val displayFormat = when (format) {
            UnitFormat.SMART_SI -> when {
                bytes > UnitFormat.GIBI.divisor -> UnitFormat.GIBI
                bytes > UnitFormat.MEBI.divisor -> UnitFormat.MEBI
                bytes > UnitFormat.KIBI.divisor -> UnitFormat.KIBI
                else -> UnitFormat.BYTE
            }
            UnitFormat.SMART_METRIC -> when {
                bytes > UnitFormat.GIGA.divisor -> UnitFormat.GIGA
                bytes > UnitFormat.MEGA.divisor -> UnitFormat.MEGA
                bytes > UnitFormat.KILO.divisor -> UnitFormat.KILO
                else -> UnitFormat.BYTE
            }
            else -> format
        }

        val displayValue = when (format) { //infinity allowed when limit or warning is zero
            DataUsageFormatter.UnitFormat.PCT_LIMIT -> if (limitBytes >= 0) bytes.toFloat() / limitBytes else 0f
            DataUsageFormatter.UnitFormat.PCT_WARNING -> if (warningBytes >= 0) bytes.toFloat() / warningBytes else 0f
            else -> bytes.toFloat()
        }

        return@run String.format("%.${decimalPlaces}f ${displayFormat.unit}", displayValue / displayFormat.divisor)
    }

    fun getColor(dataUsage: DataUsageFetcher.DataUsage) = formatForPace(dataUsage).run {
        when {
            bytes > warningBytes && warningBytes >= 0 -> Color.YELLOW
            bytes > limitBytes && limitBytes >= 0 -> Color.RED
            else -> null
        }
    }

    fun formatForPace(dataUsage: DataUsageFetcher.DataUsage) = dataUsage.run {
        when (relativeToPace) {
            true -> when (format) {
                UnitFormat.PCT_WARNING, UnitFormat.PCT_LIMIT -> DataUsageFetcher.DataUsage(
                        bytes,
                        (warningBytes * progressThroughCycle).toLong(),
                        (limitBytes * progressThroughCycle).toLong(),
                        progressThroughCycle)
                else -> DataUsageFetcher.DataUsage(
                        bytes - (limitBytes * progressThroughCycle).toLong(), //bytes over pace limit
                        (warningBytes * progressThroughCycle).toLong(),
                        (limitBytes * progressThroughCycle).toLong(),
                        progressThroughCycle)
            }
            false -> this
        }
    }

    enum class UnitFormat(val unit: String, val divisor: Float) {
        SMART_SI("", 1f),
        SMART_METRIC("", 1f),
        BYTE("B", 1f),
        KILO("KB", 1000f),
        MEGA("MB", 1000 * 1000f),
        GIGA("GB", 1000 * 1000 * 1000f),
        KIBI("KiB", 1024f),
        MEBI("MiB", 1024 * 1024f),
        GIBI("GiB", 1024 * 1024 * 1024f),
        PCT_LIMIT("%%", 0.01f),
        PCT_WARNING("%%", 0.01f)
    }
}