package io.github.francoiscampbell.xposeddatausage.model.usage

import android.content.Context
import android.os.IDataUsageService
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import javax.inject.Inject

/**
 * Created by francois on 2016-09-11.
 * Ui side implementation of DataUsageFetcher
 */
class DataUsageFetcherImpl @Inject constructor(
        private val context: Context
) : DataUsageFetcher {
    val dataUsageService by lazy { context.getSystemService("DataUsageService") as IDataUsageService }

    override fun getCurrentCycleBytes(networkType: NetworkManager.NetworkType): DataUsageFetcher.DataUsage {
        XposedLog.i("Getting bytes")
        return DataUsageFetcher.DataUsage(dataUsageService.getDataUsage(networkType.name))
    }
}