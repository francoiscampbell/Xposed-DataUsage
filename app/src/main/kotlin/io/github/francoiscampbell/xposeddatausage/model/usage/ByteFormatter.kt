package io.github.francoiscampbell.xposeddatausage.model.usage

/**
 * Created by francois on 16-03-11.
 */
class ByteFormatter(var format: UnitFormat = ByteFormatter.UnitFormat.SMART_SI, var decimalPlaces: Int = 2) {
    fun format(bytes: Long): String {
        val trueFormat = when (format) {
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

        return String.format("%.${decimalPlaces}f ${trueFormat.unit}", bytes.toFloat() / trueFormat.divisor)
    }

    enum class UnitFormat(val unit: String, val divisor: Int) {
        SMART_SI("", 1),
        SMART_METRIC("", 1),
        BYTE("B", 1),
        KILO("KB", 1000),
        MEGA("MB", 1000 * 1000),
        GIGA("GB", 1000 * 1000 * 1000),
        KIBI("KiB", 1024),
        MEBI("MiB", 1024 * 1024),
        GIBI("GiB", 1024 * 1024 * 1024),
        PCT_LIMIT("%", 1),
        PCT_WARNING("%", 1)
    }
}