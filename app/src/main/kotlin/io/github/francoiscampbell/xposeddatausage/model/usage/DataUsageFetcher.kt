package io.github.francoiscampbell.xposeddatausage.model.usage

import android.os.Bundle
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import rx.Single

/**
 * Created by francois on 16-03-15.
 */
interface DataUsageFetcher {
    fun getCurrentCycleBytes(networkType: NetworkManager.NetworkType): Single<DataUsage>

    data class DataUsage(val bytes: Long, val warningBytes: Long = -1, val limitBytes: Long = -1, val progressThroughCycle: Float = 0f) {
        companion object {
            private const val LONG_CURRENT_BYTES = "CURRENT_BYTES"
            private const val LONG_WARNING_BYTES = "WARNING_BYTES"
            private const val LONG_LIMIT_BYTES = "LIMIT_BYTES"
            private const val FLOAT_PROGRESS_THROUGH_CYCLE = "PROGRESS_THROUGH_CYCLE"
        }

        fun bundle() = Bundle().apply {
            putLong(LONG_CURRENT_BYTES, bytes)
            putLong(LONG_WARNING_BYTES, warningBytes)
            putLong(LONG_LIMIT_BYTES, limitBytes)
            putFloat(FLOAT_PROGRESS_THROUGH_CYCLE, progressThroughCycle)
        }
    }
}