package io.github.francoiscampbell.xposeddatausage.presenter

import android.content.Context
import android.telephony.TelephonyManager
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.INetworkStatsService
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.NetworkPolicyManager
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.NetworkTemplate
import io.github.francoiscampbell.xposeddatausage.util.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.view.DataUsageView

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(val view: DataUsageView, val context: Context) : DataUsagePresenter {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    override fun getCurrentCycleBytes(): String {
        val subscriberId = telephonyManager.subscriberId ?: return ""
        INetworkStatsService.forceUpdate()

        val template = NetworkTemplate.buildTemplateMobileAll(subscriberId) ?: return ""
        val policy = NetworkPolicyManager.getPolicyForTemplate(template, context) ?: return ""

        val lastCycleBoundary = NetworkPolicyManager.getLastCycleBoundary(policy)
        val nextCycleBoundary = NetworkPolicyManager.getNextCycleBoundary(policy)
        val bytes = INetworkStatsService.getNetworkTotalBytes(template, lastCycleBoundary, nextCycleBoundary)

        return ByteFormatter.format(bytes, 2, ByteFormatter.BytePrefix.SMART_SI)
    }
}

