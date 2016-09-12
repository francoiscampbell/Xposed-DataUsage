package io.github.francoiscampbell.xposeddatausage.model.usage

import android.content.Context
import android.net.INetworkStatsService
import android.net.NetworkPolicy
import android.net.NetworkPolicyManager
import android.net.NetworkTemplate
import android.os.Build
import android.telephony.TelephonyManager
import android.text.format.DateUtils
import de.robv.android.xposed.XposedHelpers
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import javax.inject.Inject

/**
 * Created by francois on 16-03-15.
 * Module side implementation of DataUsageFetcher
 */
class DataUsageFetcherHookImpl @Inject constructor(
        private val context: Context,
        private val statsService: INetworkStatsService,
        private val telephonyManager: TelephonyManager
) : DataUsageFetcher {

    override fun getCurrentCycleBytes(networkType: NetworkManager.NetworkType): DataUsageFetcher.DataUsage {
        XposedLog.i("Starting fetch")
        statsService.forceUpdate()
        XposedLog.i("Forced update")

        val template = getCurrentNetworkTemplate(networkType) ?: throw NullPointerException("no template for network type: $networkType")
        XposedLog.i("template: $template")
        val policy = getPolicyForTemplate(template)
        XposedLog.i("policy: $policy")

        val currentTime = System.currentTimeMillis()
        var nextCycleBoundary = currentTime
        var lastCycleBoundary = currentTime - 4 * DateUtils.WEEK_IN_MILLIS

        var warningBytes = -1L
        var limitBytes = -1L

        if (policy != null) {
            nextCycleBoundary = NetworkPolicyManager.computeNextCycleBoundary(currentTime, policy)
            lastCycleBoundary = NetworkPolicyManager.computeLastCycleBoundary(currentTime, policy)

            warningBytes = policy.warningBytes
            limitBytes = policy.limitBytes
        }

        val bytes = statsService.getNetworkTotalBytes(template, lastCycleBoundary, nextCycleBoundary)
        val progressThroughCycle = (currentTime - lastCycleBoundary).toFloat() / (nextCycleBoundary - lastCycleBoundary)

        return DataUsageFetcher.DataUsage(bytes, warningBytes, limitBytes, progressThroughCycle)
    }

    private fun getCurrentNetworkTemplate(networkType: NetworkManager.NetworkType): NetworkTemplate? {
        return when (networkType) {
            NetworkManager.NetworkType.MOBILE -> getCurrentNetworkTemplateMobile()
            NetworkManager.NetworkType.WIFI -> getCurrentNetworkTemplateWifi()
            NetworkManager.NetworkType.NONE -> null
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
