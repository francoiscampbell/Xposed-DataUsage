package io.github.francoiscampbell.xposeddatausage.model.usage

import android.content.Context
import android.net.INetworkStatsService
import android.net.NetworkPolicy
import android.net.NetworkPolicyManager
import android.net.NetworkTemplate
import android.os.Build
import android.telephony.TelephonyManager
import de.robv.android.xposed.XposedHelpers
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
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

    override fun getCurrentCycleBytes(networkType: NetworkManager.NetworkType): DataUsageFetcher.DataUsage {
        statsService.forceUpdate()

        val template = getCurrentNetworkTemplate(networkType) ?: throw NullPointerException("getCurrentNetworkTemplate() returned null")
        val policy = getPolicyForTemplate(template) ?: throw NullPointerException("getPolicyForTemplate() returned null")

        val currentTime = System.currentTimeMillis()
        val lastCycleBoundary = NetworkPolicyManager.computeLastCycleBoundary(currentTime, policy)
        val nextCycleBoundary = NetworkPolicyManager.computeNextCycleBoundary(currentTime, policy)

        val bytes = statsService.getNetworkTotalBytes(template, lastCycleBoundary, nextCycleBoundary)
        val progressThroughCycle = (currentTime - lastCycleBoundary).toFloat() / (nextCycleBoundary - lastCycleBoundary)

        return DataUsageFetcher.DataUsage(bytes, policy.warningBytes, policy.limitBytes, progressThroughCycle)
    }

    private fun getCurrentNetworkTemplate(networkType: NetworkManager.NetworkType): NetworkTemplate? {
        return when (networkType) {
            NetworkManager.NetworkType.MOBILE -> getCurrentNetworkTemplateMobile()
            NetworkManager.NetworkType.WIFI -> getCurrentNetworkTemplateWifi()
            NetworkManager.NetworkType.UNKNOWN -> null
        }
    }

    private fun getCurrentNetworkTemplateMobile(): NetworkTemplate? {
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

    private fun getCurrentNetworkTemplateWifi(): NetworkTemplate = NetworkTemplate.buildTemplateWifi()

    private fun getPolicyForTemplate(networkTemplate: NetworkTemplate): NetworkPolicy? {
        for (networkPolicy in NetworkPolicyManager.from(context).networkPolicies) {
            if (networkPolicy.template == networkTemplate) {
                return networkPolicy
            }
        }
        return null
    }
}
