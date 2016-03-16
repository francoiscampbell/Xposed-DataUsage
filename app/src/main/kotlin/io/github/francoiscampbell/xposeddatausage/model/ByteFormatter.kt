package io.github.francoiscampbell.xposeddatausage.model

/**
 * Created by francois on 16-03-11.
 */
object ByteFormatter {
    fun format(bytes: Number, decimalPlaces: Int, bytePrefix: BytePrefix): String {
        val floatBytes = bytes.toFloat()

        val trueBytePrefix = when (bytePrefix) {
            BytePrefix.SMART_SI -> when {
                floatBytes > BytePrefix.GIBI.divisor -> BytePrefix.GIBI
                floatBytes > BytePrefix.MEBI.divisor -> BytePrefix.MEBI
                floatBytes > BytePrefix.KIBI.divisor -> BytePrefix.KIBI
                else -> BytePrefix.NONE
            }
            BytePrefix.SMART_METRIC -> when {
                floatBytes > BytePrefix.GIGA.divisor -> BytePrefix.GIGA
                floatBytes > BytePrefix.MEGA.divisor -> BytePrefix.MEGA
                floatBytes > BytePrefix.KILO.divisor -> BytePrefix.KILO
                else -> BytePrefix.NONE
            }
            else -> bytePrefix
        }

        return String.format("%.${decimalPlaces}f ${trueBytePrefix.prefix}B", floatBytes / trueBytePrefix.divisor)
    }

    enum class BytePrefix(val prefix: String, val divisor: Int) {
        SMART_SI("", 1),
        SMART_METRIC("", 1),
        NONE("", 1),
        KILO("k", 1000),
        MEGA("M", 1000 * 1000),
        GIGA("G", 1000 * 1000 * 1000),
        KIBI("Ki", 1024),
        MEBI("Mi", 1024 * 1024),
        GIBI("Gi", 1024 * 1024 * 1024)
    }
}