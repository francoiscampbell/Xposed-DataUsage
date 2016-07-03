package io.github.francoiscampbell.xposeddatausage.model.usage

import android.graphics.Color

/**
 * Created by francois on 16-03-11.
 */
class DataUsageFormatter(var format: UnitFormat = UnitFormat.SMART,
                         var decimalPlaces: Int = 2,
                         var relativeToPace: Boolean = false,
                         var binaryCalc: Boolean = false) {

    private val BINARY_TO_DECIMAL_BYTES = 1024f / 1000f

    fun format(dataUsage: DataUsageFetcher.DataUsage): String {
        return formatForPace(dataUsage).run {
            val absBytes = Math.abs(bytes)
            val displayFormat = when (format) {
                UnitFormat.SMART -> when {
                    absBytes > UnitFormat.GIGA.divisor -> UnitFormat.GIGA
                    absBytes > UnitFormat.MEGA.divisor -> UnitFormat.MEGA
                    absBytes > UnitFormat.KILO.divisor -> UnitFormat.KILO
                    else -> UnitFormat.BYTE
                }
                else -> format
            }

            val displayValue = when (format) {
                DataUsageFormatter.UnitFormat.PCT -> if (limitBytes > 0) bytes.toFloat() / limitBytes else 0f //TODO: proper logic
//                DataUsageFormatter.UnitFormat.PCT -> if (warningBytes > 0) bytes.toFloat() / warningBytes else 0f
                else -> bytes.toFloat()
            }

            return@run String.format("%.${decimalPlaces}f ${displayFormat.unit}", displayValue / displayFormat.divisor)
        }
    }

    fun getColor(dataUsage: DataUsageFetcher.DataUsage): Int? {
        return formatForPace(dataUsage).run {
            when {
                bytes > limitBytes && limitBytes >= 0 -> Color.RED
                bytes > warningBytes && warningBytes >= 0 -> Color.YELLOW
                else -> null
            }
        }
    }

    fun formatForPace(dataUsage: DataUsageFetcher.DataUsage): DataUsageFetcher.DataUsage {
        if (!relativeToPace) return dataUsage
        return dataUsage.run {
            when (format) {
                UnitFormat.PCT -> DataUsageFetcher.DataUsage(
                        bytes - (warningBytes * progressThroughCycle).toLong(), //bytes over pace warning
                        (warningBytes * progressThroughCycle).toLong(), //warning scaled to current time
                        (limitBytes * progressThroughCycle).toLong(), //limit scaled to current time
                        progressThroughCycle)
                else -> DataUsageFetcher.DataUsage(
                        bytes - (limitBytes * progressThroughCycle).toLong(), //bytes over pace limit
                        (warningBytes * progressThroughCycle).toLong(),
                        (limitBytes * progressThroughCycle).toLong(),
                        progressThroughCycle)
            }
        }
    }

    enum class UnitFormat(val unit: String, val divisor: Float) {
        SMART("", 1f),
        BYTE("B", 1f),
        KILO("KB", 1000f),
        MEGA("MB", 1000 * 1000f),
        GIGA("GB", 1000 * 1000 * 1000f),
        KIBI("KiB", 1024f),
        MEBI("MiB", 1024 * 1024f),
        GIBI("GiB", 1024 * 1024 * 1024f),
        PCT("%%", 0.01f),
    }
}