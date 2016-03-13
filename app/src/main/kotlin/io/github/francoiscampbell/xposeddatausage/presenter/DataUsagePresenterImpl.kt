package io.github.francoiscampbell.xposeddatausage.presenter

import android.content.Context
import android.telephony.TelephonyManager
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.INetworkStatsService
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.NetworkPolicyManager
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.NetworkTemplate

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(private val context: Context) : DataUsagePresenter {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    override fun getCurrentCycleBytes(): Number {
        val subscriberId = telephonyManager.subscriberId ?: return -1
        INetworkStatsService.forceUpdate()

        val template = NetworkTemplate.buildTemplateMobileAll(subscriberId) ?: return -1
        val policy = NetworkPolicyManager.getPolicyForTemplate(template, context) ?: return -1

        val lastCycleBoundary = NetworkPolicyManager.getLastCycleBoundary(policy)
        val nextCycleBoundary = NetworkPolicyManager.getNextCycleBoundary(policy)

        return INetworkStatsService.getNetworkTotalBytes(template, lastCycleBoundary, nextCycleBoundary)
    }
}

