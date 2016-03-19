package io.github.francoiscampbell.xposeddatausage.model.usage

/**
 * Created by francois on 16-03-15.
 */
interface DataUsageFetcher {
    fun getCurrentCycleBytes(callback: (DataUsageFetcher.DataUsage) -> Unit, onError: (Throwable) -> Unit)

    data class DataUsage(val bytes: Long, val warningBytes: Long, val limitBytes: Long, val progressThroughCycle: Float)
}