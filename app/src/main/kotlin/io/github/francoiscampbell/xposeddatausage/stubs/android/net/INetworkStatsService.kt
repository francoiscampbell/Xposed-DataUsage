package io.github.francoiscampbell.xposeddatausage.stubs.android.net

import android.net.TrafficStats
import de.robv.android.xposed.XposedHelpers

/**
 * Created by francois on 16-03-13.
 */
object INetworkStatsService {
    val stubObject = XposedHelpers.callStaticMethod(TrafficStats::class.java, "getStatsService")

    fun forceUpdate() = XposedHelpers.callMethod(stubObject, "forceUpdate")

    fun getNetworkTotalBytes(template: Any, lastCycleBoundary: Number, nextCycleBoundary: Number): Number = XposedHelpers.callMethod(
            INetworkStatsService.stubObject,
            "getNetworkTotalBytes",
            arrayOf(NetworkTemplate.stubClass, Long::class.java, Long::class.java),
            template,
            lastCycleBoundary,
            nextCycleBoundary) as Number

}