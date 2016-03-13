package io.github.francoiscampbell.xposeddatausage.presenter

import android.content.Context
import android.net.TrafficStats
import android.telephony.TelephonyManager
import de.robv.android.xposed.XposedHelpers

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(private val context: Context) : DataUsagePresenter {
    companion object {
        private val classNetworkTemplate = XposedHelpers.findClass("android.net.NetworkTemplate", null)
        private val classNetworkPolicyManager = XposedHelpers.findClass("android.net.NetworkPolicyManager", null)
        private val classNetworkPolicy = XposedHelpers.findClass("android.net.NetworkPolicy", null)

        private val networkStatsService = XposedHelpers.callStaticMethod(TrafficStats::class.java, "getStatsService") //actually INetworkStatsService
    }

    private val networkPolicyManager = XposedHelpers.callStaticMethod(classNetworkPolicyManager, "from", context) //actually NetworkPolicyManager
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    override fun getCurrentCycleBytes(): Number {
        val subscriberId = telephonyManager.subscriberId ?: return -1
        XposedHelpers.callMethod(networkStatsService, "forceUpdate")

        val template = getNetworkTemplateMobile(subscriberId) ?: return -1
        val policy = getPolicyForTemplate(template) ?: return -1

        val lastCycleBoundary = getLastCycleBoundary(policy)
        val nextCycleBoundary = getNextCycleBoundary(policy)

        return XposedHelpers.callMethod(
                networkStatsService,
                "getNetworkTotalBytes",
                arrayOf(classNetworkTemplate, Long::class.java, Long::class.java),
                template,
                lastCycleBoundary,
                nextCycleBoundary) as Number
    }

    fun getNetworkTemplateMobile(subscriberId: String): Any? {
        return XposedHelpers.callStaticMethod(classNetworkTemplate, "buildTemplateMobileAll", subscriberId)
    }

    // Returns the NetworkPolicy that contains the specified NetworkTemplate
    fun getPolicyForTemplate(networkTemplate: Any): Any? {
        for (networkPolicy in XposedHelpers.callMethod(networkPolicyManager, "getNetworkPolicies") as Array<*>) {
            val networkTemplateForPolicy = XposedHelpers.getObjectField(networkPolicy, "template")
            if (networkTemplateForPolicy == networkTemplate) {
                return networkPolicy
            }
        }
        return null
    }

    fun getLastCycleBoundary(networkPolicyMobile: Any): Number {
        val lastCycleBoundary = XposedHelpers.callStaticMethod(
                classNetworkPolicyManager,
                "computeLastCycleBoundary",
                arrayOf(Long::class.java, classNetworkPolicy),
                System.currentTimeMillis(),
                networkPolicyMobile)
        if (lastCycleBoundary == null || lastCycleBoundary !is Number) {
            return 0
        }
        return lastCycleBoundary
    }

    fun getNextCycleBoundary(networkPolicyMobile: Any): Number {
        val nextCycleBoundary = XposedHelpers.callStaticMethod(
                classNetworkPolicyManager,
                "computeNextCycleBoundary",
                arrayOf(Long::class.java, classNetworkPolicy),
                System.currentTimeMillis(),
                networkPolicyMobile)
        if (nextCycleBoundary == null || nextCycleBoundary !is Number) {
            return 0
        }
        return nextCycleBoundary
    }
}

