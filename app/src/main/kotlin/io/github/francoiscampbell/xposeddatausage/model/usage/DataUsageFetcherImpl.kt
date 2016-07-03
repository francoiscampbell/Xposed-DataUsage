package io.github.francoiscampbell.xposeddatausage.model.usage

import android.content.Context
import android.net.INetworkStatsService
import android.net.NetworkPolicy
import android.net.NetworkPolicyManager
import android.net.NetworkTemplate
import android.os.Build
import android.telephony.TelephonyManager
import de.robv.android.xposed.XposedHelpers
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by francois on 16-03-15.
 */
class DataUsageFetcherImpl @Inject constructor(
        @Named("app") private val context: Context,
        private val statsService: INetworkStatsService,
        private val telephonyManager: TelephonyManager
) : DataUsageFetcher {

    override fun getCurrentCycleBytes(callback: (DataUsageFetcher.DataUsage) -> Unit, onError: (Throwable) -> Unit) {
        try {
            statsService.forceUpdate()
        } catch (e: IllegalStateException) {
            onError(e)
            return
        }

        val template = getCurrentNetworkTemplate()
        if (template == null) {
            onError(NullPointerException("getCurrentNetworkTemplate() returned null"))
            return
        }

        val policy = getPolicyForTemplate(template)
        if (policy == null) {
            onError(NullPointerException("getPolicyForTemplate() returned null"))
            return
        }

        val currentTime = System.currentTimeMillis()
        val lastCycleBoundary = NetworkPolicyManager.computeLastCycleBoundary(currentTime, policy)
        val nextCycleBoundary = NetworkPolicyManager.computeNextCycleBoundary(currentTime, policy)

        val bytes = statsService.getNetworkTotalBytes(template, lastCycleBoundary, nextCycleBoundary)
        val progressThroughCycle = (currentTime - lastCycleBoundary).toFloat() / (nextCycleBoundary - lastCycleBoundary)

        callback(DataUsageFetcher.DataUsage(bytes, policy.warningBytes, policy.limitBytes, progressThroughCycle))
    }

    private fun getCurrentNetworkTemplate(): NetworkTemplate? {
        val subscriberId = telephonyManager.subscriberId ?: return null
        val template = NetworkTemplate.buildTemplateMobileAll(subscriberId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mergedSubscriberIds = XposedHelpers.callMethod(telephonyManager, "getMergedSubscriberIds")
            if (mergedSubscriberIds != null) {
                @Suppress("UNCHECKED_CAST")
                return NetworkTemplate.normalize(template, mergedSubscriberIds as Array<String>)
            }
        }
        return template
    }

    private fun getPolicyForTemplate(networkTemplate: NetworkTemplate): NetworkPolicy? {
        for (networkPolicy in NetworkPolicyManager.from(context).networkPolicies) {
            if (networkPolicy.template == networkTemplate) {
                return networkPolicy
            }
        }
        return null
    }
}
