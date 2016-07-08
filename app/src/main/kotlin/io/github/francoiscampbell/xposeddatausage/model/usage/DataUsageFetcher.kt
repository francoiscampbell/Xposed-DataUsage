package io.github.francoiscampbell.xposeddatausage.model.usage

import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager

/**
 * Created by francois on 16-03-15.
 */
interface DataUsageFetcher {
    fun getCurrentCycleBytes(networkType: NetworkManager.NetworkType): DataUsageFetcher.DataUsage

    data class DataUsage(val bytes: Long, val warningBytes: Long = -1, val limitBytes: Long = -1, val progressThroughCycle: Float = 0f)
}