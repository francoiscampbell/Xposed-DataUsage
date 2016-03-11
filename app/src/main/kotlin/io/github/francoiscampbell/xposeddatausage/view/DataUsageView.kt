package io.github.francoiscampbell.xposeddatausage.view

import android.content.Context
import android.net.TrafficStats
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.widget.TextView
import de.robv.android.xposed.XposedHelpers
import io.github.francoiscampbell.xposeddatausage.util.ByteFormatter

/**
 * Created by francois on 16-03-11.
 */
class DataUsageView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {
    private val classNetworkTemplate = XposedHelpers.findClass("android.net.NetworkTemplate", null)
    private val classNetworkPolicyManager = XposedHelpers.findClass("android.net.NetworkPolicyManager", null)
    private val classNetworkPolicy = XposedHelpers.findClass("android.net.NetworkPolicy", null)

    private val networkPolicyManager = XposedHelpers.callStaticMethod(classNetworkPolicyManager, "from", context)
    private val networkStatsService = XposedHelpers.callStaticMethod(TrafficStats::class.java, "getStatsService")
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private lateinit var networkTemplateMobile: Any
    private var networkPolicyMobile: Any? = null

    init {
        onConnectionStatusChanged()
    }

    //TODO call this when connectivity changes using broadcast receiver
    private fun onConnectionStatusChanged() {
        // Build a network template matching the current subscriber ID (IMSI) and get the policy that defines the cycle reset day
        // Do this on connectivity change in case the user changes the SIM card without rebooting
        networkTemplateMobile = getNetworkTemplateMobile(telephonyManager.subscriberId)
        networkPolicyMobile = getPolicyForTemplate(networkTemplateMobile)
    }

    private fun getNetworkTemplateMobile(subscriberId: String): Any {
        return XposedHelpers.callStaticMethod(classNetworkTemplate, "buildTemplateMobileAll", subscriberId)
    }

    // Returns the NetworkPolicy that contains the specified NetworkTemplate
    private fun getPolicyForTemplate(networkTemplate: Any): Any? {
        for (networkPolicy in XposedHelpers.callMethod(networkPolicyManager, "getNetworkPolicies") as Array<*>) {
            val networkTemplateForPolicy = XposedHelpers.getObjectField(networkPolicy, "template")
            if (networkTemplateForPolicy == networkTemplate) {
                return networkPolicy
            }
        }
        return null
    }

    fun update() {
        text = ByteFormatter.format(getCurrentCycleBytes(), ByteFormatter.Prefix.MEBI)
    }

    // Use Any because the classes required are not in the Android SDK
    // Acquire the parameters through reflection (the XposedHelpers class is useful for this)
    private fun getCurrentCycleBytes(): Long {
        XposedHelpers.callMethod(networkStatsService, "forceUpdate")

        return XposedHelpers.callMethod(
                networkStatsService,
                "getNetworkTotalBytes",
                arrayOf(classNetworkTemplate, Long::class.java, Long::class.java),
                networkTemplateMobile,
                getLastCycleBoundary(),
                getNextCycleBoundary()) as Long
    }

    private fun getLastCycleBoundary(): Long {
        if (networkPolicyMobile == null) {
            return 0
        }
        val lastCycleBoundary = XposedHelpers.callStaticMethod(
                classNetworkPolicyManager,
                "computeLastCycleBoundary",
                arrayOf(Long::class.java, classNetworkPolicy),
                System.currentTimeMillis(),
                networkPolicyMobile)
        if (lastCycleBoundary == null || lastCycleBoundary !is Long) {
            return 0
        }
        return lastCycleBoundary
    }

    private fun getNextCycleBoundary(): Long {
        if (networkPolicyMobile == null) {
            return 0
        }
        val nextCycleBoundary = XposedHelpers.callStaticMethod(
                classNetworkPolicyManager,
                "computeNextCycleBoundary",
                arrayOf(Long::class.java, classNetworkPolicy),
                System.currentTimeMillis(),
                networkPolicyMobile)
        if (nextCycleBoundary == null || nextCycleBoundary !is Long) {
            return 0
        }
        return nextCycleBoundary
    }
}