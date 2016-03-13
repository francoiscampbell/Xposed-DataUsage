package io.github.francoiscampbell.xposeddatausage.stubs.android.net

import android.content.Context
import de.robv.android.xposed.XposedHelpers

/**
 * Created by francois on 16-03-13.
 */
object NetworkPolicyManager {
    val stubClass = XposedHelpers.findClass("android.net.NetworkPolicyManager", null)

    fun getPolicyForTemplate(networkTemplate: Any, context: Context): Any? {
        for (networkPolicy in XposedHelpers.callMethod(from(context), "getNetworkPolicies") as Array<*>) {
            val networkTemplateForPolicy = XposedHelpers.getObjectField(networkPolicy, "template")
            if (networkTemplateForPolicy == networkTemplate) {
                return networkPolicy
            }
        }
        return null

    }

    fun from(context: Context) = XposedHelpers.callStaticMethod(stubClass, "from", context)

    fun getLastCycleBoundary(networkPolicyMobile: Any): Number {
        val lastCycleBoundary = XposedHelpers.callStaticMethod(
                NetworkPolicyManager.stubClass,
                "computeLastCycleBoundary",
                arrayOf(Long::class.java, NetworkPolicy.stubClass),
                System.currentTimeMillis(),
                networkPolicyMobile)
        if (lastCycleBoundary == null || lastCycleBoundary !is Number) {
            return 0
        }
        return lastCycleBoundary
    }

    fun getNextCycleBoundary(networkPolicyMobile: Any): Number {
        val nextCycleBoundary = XposedHelpers.callStaticMethod(
                NetworkPolicyManager.stubClass,
                "computeNextCycleBoundary",
                arrayOf(Long::class.java, NetworkPolicy.stubClass),
                System.currentTimeMillis(),
                networkPolicyMobile)
        if (nextCycleBoundary == null || nextCycleBoundary !is Number) {
            return 0
        }
        return nextCycleBoundary
    }
}