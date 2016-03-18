package io.github.francoiscampbell.xposeddatausage.model.usage

/**
 * Created by francois on 16-03-11.
 */
class ByteFormatter(var unit: UnitFormat = ByteFormatter.UnitFormat.SMART_SI, var decimalPlaces: Int = 2) {
    fun format(bytes: Number): String {
        val floatBytes = bytes.toFloat()

        val trueBytePrefix = when (unit) {
            UnitFormat.SMART_SI -> when {
                floatBytes > UnitFormat.GIBI.divisor -> UnitFormat.GIBI
                floatBytes > UnitFormat.MEBI.divisor -> UnitFormat.MEBI
                floatBytes > UnitFormat.KIBI.divisor -> UnitFormat.KIBI
                else -> UnitFormat.BYTE
            }
            UnitFormat.SMART_METRIC -> when {
                floatBytes > UnitFormat.GIGA.divisor -> UnitFormat.GIGA
                floatBytes > UnitFormat.MEGA.divisor -> UnitFormat.MEGA
                floatBytes > UnitFormat.KILO.divisor -> UnitFormat.KILO
                else -> UnitFormat.BYTE
            }
            else -> unit
        }

        return String.format("%.${decimalPlaces}f ${trueBytePrefix.unit}", floatBytes / trueBytePrefix.divisor)
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