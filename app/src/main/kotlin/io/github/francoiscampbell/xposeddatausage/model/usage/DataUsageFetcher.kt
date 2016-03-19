package io.github.francoiscampbell.xposeddatausage.model.usage

/**
 * Created by francois on 16-03-15.
 */
interface DataUsageFetcher {
    fun getCurrentCycleBytes(callback: (Long, Long, Long) -> Unit, onError: (Throwable) -> Unit)
}