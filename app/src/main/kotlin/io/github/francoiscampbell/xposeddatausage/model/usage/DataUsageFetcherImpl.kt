package io.github.francoiscampbell.xposeddatausage.model.usage

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import rx.Single
import javax.inject.Inject

/**
 * Created by francois on 2016-09-09.
 */
class DataUsageFetcherImpl @Inject constructor(
        private val context: Context
) : DataUsageFetcher {
    override fun getCurrentCycleBytes(networkType: NetworkManager.NetworkType): Single<DataUsageFetcher.DataUsage> {
        return Single.create { subscriber ->
            context.sendOrderedBroadcast(
                    Intent(Module.ACTION_GET_DATA_USAGE).putExtra(Module.EXTRA_NETWORK_TYPE, networkType.name),
                    "",
                    object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            val extras = intent.extras
                            val dataUsage = DataUsageFetcher.DataUsage(
                                    extras.getLong(DataUsageFetcher.DataUsage.LONG_CURRENT_BYTES),
                                    extras.getLong(DataUsageFetcher.DataUsage.LONG_WARNING_BYTES),
                                    extras.getLong(DataUsageFetcher.DataUsage.LONG_LIMIT_BYTES),
                                    extras.getFloat(DataUsageFetcher.DataUsage.FLOAT_PROGRESS_THROUGH_CYCLE)
                            )
                            subscriber.onSuccess(dataUsage)
                        }
                    },
                    null,
                    Activity.RESULT_OK,
                    null,
                    null)
        }
    }
}