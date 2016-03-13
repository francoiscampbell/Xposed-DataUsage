package io.github.francoiscampbell.xposeddatausage.util

/**
 * Created by francois on 16-03-11.
 */
object ByteFormatter {
    fun format(bytes: Number, decimalPlaces: Int, prefix: Prefix): String {
        val floatBytes = bytes.toFloat()
        return when (prefix) {
            Prefix.NONE -> {
                "$floatBytes B"
            }
            Prefix.KILO -> {
                String.format("%.${decimalPlaces}f KB", floatBytes / 1000)
            }
            Prefix.MEGA -> {
                String.format("%.${decimalPlaces}f MB", floatBytes / 1000 / 1000)
            }
            Prefix.GIGA -> {
                String.format("%.${decimalPlaces}f GB", floatBytes / 1000 / 1000 / 1000)
            }
            Prefix.KIBI -> {
                String.format("%.${decimalPlaces}f KiB", floatBytes / 1024)
            }
            Prefix.MEBI -> {
                String.format("%.${decimalPlaces}f MiB", floatBytes / 1024 / 1024)
            }
            Prefix.GIBI -> {
                String.format("%.${decimalPlaces}f GiB", floatBytes / 1024 / 1024 / 1024)
            }
        }
    }

    enum class Prefix {
        NONE, KILO, MEGA, GIGA, KIBI, MEBI, GIBI
    }
}