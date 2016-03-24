package io.github.francoiscampbell.xposeddatausage.model.usage

import android.content.Context
import android.net.*
import android.telephony.TelephonyManager
import de.robv.android.xposed.XposedHelpers
import io.github.francoiscampbell.xposeddatausage.Module

/**
 * Created by francois on 16-03-15.
 */
class DataUsageFetcherImpl() : DataUsageFetcher {
    private val context = Module.hookedContext
    private val statsService = XposedHelpers.callStaticMethod(TrafficStats::class.java, "getStatsService") as INetworkStatsService
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

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
        return NetworkTemplate.buildTemplateMobileAll(subscriberId)
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
