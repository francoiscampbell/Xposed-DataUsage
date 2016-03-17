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

    override fun getCurrentCycleBytes(callback: (Long, Long, Long) -> Unit) {
        statsService.forceUpdate()

        val template = NetworkTemplate.buildTemplateMobileAll(telephonyManager.subscriberId) ?: return
        val policy = getPolicyForTemplate(template, context) ?: return

        val currentTime = System.currentTimeMillis()
        val lastCycleBoundary = NetworkPolicyManager.computeLastCycleBoundary(currentTime, policy)
        val nextCycleBoundary = NetworkPolicyManager.computeNextCycleBoundary(currentTime, policy)

        val bytes = statsService.getNetworkTotalBytes(template, lastCycleBoundary, nextCycleBoundary)

        callback(bytes, policy.warningBytes, policy.limitBytes)
    }

    private fun getPolicyForTemplate(networkTemplate: NetworkTemplate, context: Context): NetworkPolicy? {
        for (networkPolicy in NetworkPolicyManager.from(context).networkPolicies) {
            if (networkPolicy.template == networkTemplate) {
                return networkPolicy
            }
        }
        return null
    }
}
