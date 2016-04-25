package io.github.francoiscampbell.xposeddatausage.model.usage

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by francois on 16-04-25.
 */
class DataUsageFormatterTest {
    val dataUsageFormatter = DataUsageFormatter()

    @Test
    fun testZeroBytesZeroDecimals() {
        val dataUsage = DataUsageFetcher.DataUsage(0)
        dataUsageFormatter.decimalPlaces = 0
        dataUsageFormatter.relativeToPace = false

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.BYTE
        assertEquals("0 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KILO
        assertEquals("0 KB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEGA
        assertEquals("0 MB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIGA
        assertEquals("0 GB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_METRIC
        assertEquals("0 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KIBI
        assertEquals("0 KiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEBI
        assertEquals("0 MiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIBI
        assertEquals("0 GiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_SI
        assertEquals("0 B", dataUsageFormatter.format(dataUsage))

    }

    @Test
    fun testZeroBytesOneDecimal() {
        val dataUsage = DataUsageFetcher.DataUsage(0)
        dataUsageFormatter.decimalPlaces = 1
        dataUsageFormatter.relativeToPace = false

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.BYTE
        assertEquals("0.0 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KILO
        assertEquals("0.0 KB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEGA
        assertEquals("0.0 MB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIGA
        assertEquals("0.0 GB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_METRIC
        assertEquals("0.0 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KIBI
        assertEquals("0.0 KiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEBI
        assertEquals("0.0 MiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIBI
        assertEquals("0.0 GiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_SI
        assertEquals("0.0 B", dataUsageFormatter.format(dataUsage))
    }

    @Test
    fun testZeroBytesTwoDecimals() {
        val dataUsage = DataUsageFetcher.DataUsage(0)
        dataUsageFormatter.decimalPlaces = 2
        dataUsageFormatter.relativeToPace = false

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.BYTE
        assertEquals("0.00 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KILO
        assertEquals("0.00 KB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEGA
        assertEquals("0.00 MB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIGA
        assertEquals("0.00 GB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_METRIC
        assertEquals("0.00 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KIBI
        assertEquals("0.00 KiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEBI
        assertEquals("0.00 MiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIBI
        assertEquals("0.00 GiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_SI
        assertEquals("0.00 B", dataUsageFormatter.format(dataUsage))
    }

    @Test
    fun testZeroBytesThreeDecimals() {
        val dataUsage = DataUsageFetcher.DataUsage(0)
        dataUsageFormatter.decimalPlaces = 3
        dataUsageFormatter.relativeToPace = false

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.BYTE
        assertEquals("0.000 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KILO
        assertEquals("0.000 KB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEGA
        assertEquals("0.000 MB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIGA
        assertEquals("0.000 GB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_METRIC
        assertEquals("0.000 B", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.KIBI
        assertEquals("0.000 KiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.MEBI
        assertEquals("0.000 MiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.GIBI
        assertEquals("0.000 GiB", dataUsageFormatter.format(dataUsage))

        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.SMART_SI
        assertEquals("0.000 B", dataUsageFormatter.format(dataUsage))
    }

    @Test
    fun testBytesAbsolute() {
        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.BYTE
        dataUsageFormatter.relativeToPace = false

        var dataUsage = DataUsageFetcher.DataUsage(0)
        dataUsageFormatter.decimalPlaces = 0
        assertEquals("0 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 1
        assertEquals("0.0 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 2
        assertEquals("0.00 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 3
        assertEquals("0.000 B", dataUsageFormatter.format(dataUsage))


        dataUsage = DataUsageFetcher.DataUsage(250000)
        dataUsageFormatter.decimalPlaces = 0
        assertEquals("250000 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 1
        assertEquals("250000.0 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 2
        assertEquals("250000.00 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 3
        assertEquals("250000.000 B", dataUsageFormatter.format(dataUsage))
    }

    @Test
    fun testBytesRelativeAtBeginning() {
        dataUsageFormatter.format = DataUsageFormatter.UnitFormat.BYTE
        dataUsageFormatter.relativeToPace = true

        var dataUsage = DataUsageFetcher.DataUsage(0)
        dataUsageFormatter.decimalPlaces = 0
        assertEquals("0 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 1
        assertEquals("0.0 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 2
        assertEquals("0.00 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 3
        assertEquals("0.000 B", dataUsageFormatter.format(dataUsage))


        dataUsage = DataUsageFetcher.DataUsage(250000)
        dataUsageFormatter.decimalPlaces = 0
        assertEquals("250000 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 1
        assertEquals("250000.0 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 2
        assertEquals("250000.00 B", dataUsageFormatter.format(dataUsage))
        dataUsageFormatter.decimalPlaces = 3
        assertEquals("250000.000 B", dataUsageFormatter.format(dataUsage))
    }
}
