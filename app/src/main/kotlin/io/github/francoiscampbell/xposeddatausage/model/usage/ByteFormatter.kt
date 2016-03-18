package io.github.francoiscampbell.xposeddatausage.model.usage

/**
 * Created by francois on 16-03-11.
 */
class ByteFormatter(var format: UnitFormat = ByteFormatter.UnitFormat.SMART_SI, var decimalPlaces: Int = 2) {
    fun format(bytes: Long, warningBytes: Long, limitBytes: Long): String {
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
            ByteFormatter.UnitFormat.PCT_LIMIT -> if (limitBytes >= 0) bytes.toFloat() / limitBytes else 0f
            ByteFormatter.UnitFormat.PCT_WARNING -> if (warningBytes >= 0) bytes.toFloat() / warningBytes else 0f
            else -> bytes.toFloat()
        }

        return String.format("%.${decimalPlaces}f ${displayFormat.unit}", displayValue / displayFormat.divisor)
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