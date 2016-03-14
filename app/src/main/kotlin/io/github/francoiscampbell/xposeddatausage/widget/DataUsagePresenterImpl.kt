package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.INetworkStatsService
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.NetworkPolicyManager
import io.github.francoiscampbell.xposeddatausage.stubs.android.net.NetworkTemplate
import io.github.francoiscampbell.xposeddatausage.util.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(val view: DataUsageView, val context: Context) : DataUsagePresenter {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    init {
        showViewIfMobile()
        registerConnectivityChangeReceiver()
    }

    private fun showViewIfMobile() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when (connectivityManager.activeNetworkInfo?.type) {
            ConnectivityManager.TYPE_MOBILE,
            ConnectivityManager.TYPE_MOBILE_DUN,
            ConnectivityManager.TYPE_WIMAX -> {
                view.show()
            }
            else -> {
                view.hide()
            }
        }
    }

    private fun registerConnectivityChangeReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(intentFilter) { context, intent ->
            showViewIfMobile()
        }
    }

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

