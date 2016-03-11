package io.github.francoiscampbell.xposeddatausage.util

/**
 * Created by francois on 16-03-11.
 */
object ByteFormatter {
    fun format(bytes: Long, prefix: Prefix): String {
        return when (prefix) {
            Prefix.NONE -> {
                "$bytes B"
            }
            Prefix.KILO -> {
                String.format("%.1f KB", bytes / 1000)
            }
            Prefix.MEGA -> {
                String.format("%.1f MB", bytes / 1000 / 1000)
            }
            Prefix.GIGA -> {
                String.format("%.1f GB", bytes / 1000 / 1000 / 1000)
            }
            Prefix.KIBI -> {
                String.format("%.1f KiB", bytes / 1024)
            }
            Prefix.MEBI -> {
                String.format("%.1f MiB", bytes / 1024 / 1024)
            }
            Prefix.GIBI -> {
                String.format("%.1f GiB", bytes / 1024 / 1024 / 1024)
            }
        }
    }

    enum class Prefix {
        NONE, KILO, MEGA, GIGA, KIBI, MEBI, GIBI
    }
}