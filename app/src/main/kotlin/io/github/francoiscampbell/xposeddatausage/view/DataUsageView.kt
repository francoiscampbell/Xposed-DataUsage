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
    private lateinit var networkPolicyMobile: Any

    private var currentCycleStartMillis = 0L
    private var nextCycleStartMillis = 0L

    init {
        onConnectionStatusChanged()
    }

    private fun onConnectionStatusChanged() {
        // Build a network template matching the current subscriber ID (IMSI) and get the policy that defines the cycle reset day
        // Do this on connectivity change in case the user changes the SIM card without rebooting
        networkTemplateMobile = getNetworkTemplateMobile(telephonyManager.subscriberId)
        networkPolicyMobile = getPolicyForTemplate(networkTemplateMobile)
    }

    private fun getNetworkTemplateMobile(subscriberId: String): Any {
        return XposedHelpers.callStaticMethod(classNetworkTemplate, "buildTemplateMobileAll", subscriberId)
    }

    fun update() {
        val cycleStartMillis = getCycleStartMillis(networkPolicyMobile)

        // Get the number of bytes rx/tx in the cycle
        val mobileBytes = getCycleBytes(networkTemplateMobile, cycleStartMillis, System.currentTimeMillis())
        text = ByteFormatter.format(mobileBytes, ByteFormatter.Prefix.MEBI)
    }

    // Use Any because the classes required are not in the Android SDK
    // Acquire the parameters through reflection (the XposedHelpers class is useful for this)
    private fun getCycleBytes(networkTemplate: Any, cycleStartMillis: Long, cycleEndMillis: Long): Long {
        XposedHelpers.callMethod(networkStatsService, "forceUpdate")
        return XposedHelpers.callMethod(
                networkStatsService,
                "getNetworkTotalBytes",
                arrayOf(networkTemplate.javaClass,
                        Long::class.java,
                        Long::class.java),
                networkTemplate,
                cycleStartMillis,
                cycleEndMillis) as Long
    }

    // Returns the NetworkPolicy that contains the specified NetworkTemplate
    private fun getPolicyForTemplate(networkTemplate: Any): Any {
        var networkPolicyMobile: Any? = null
        for (networkPolicy in XposedHelpers.callMethod(networkPolicyManager, "getNetworkPolicies") as Array<*>) {
            val networkTemplateForPolicy = XposedHelpers.getObjectField(networkPolicy, "template")
            if (networkTemplateForPolicy == networkTemplate) {
                networkPolicyMobile = networkPolicy
                break
            }
        }
        return networkPolicyMobile as Any
    }

    private fun getCycleStartMillis(networkPolicyMobile: Any): Long {
        // if we've past a cycle boundary, then the end of the cycle becomes the beginning of the new one and we find the end of the
        // new cycle
        if (System.currentTimeMillis() > nextCycleStartMillis) {
            currentCycleStartMillis = nextCycleStartMillis
            nextCycleStartMillis = XposedHelpers.callStaticMethod(
                    classNetworkPolicyManager,
                    "computeNextCycleBoundary",
                    arrayOf(Long::class.java,
                            classNetworkPolicy),
                    System.currentTimeMillis(),
                    networkPolicyMobile) as Long
        }
        return currentCycleStartMillis
    }
}