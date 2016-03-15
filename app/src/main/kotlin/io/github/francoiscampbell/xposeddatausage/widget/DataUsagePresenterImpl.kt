package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.github.francoiscampbell.xposeddatausage.util.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(val view: DataUsageView, val context: Context) : DataUsagePresenter {

    private val fetcher = DataUsageFetcherImpl(context)

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

    override fun update(): Unit {
        fetcher.getCurrentCycleBytes { bytes, warningBytes, limitBytes ->
            view.bytesText = ByteFormatter.format(bytes, 2, ByteFormatter.BytePrefix.SMART_SI)
        }
    }

    //    fun getRequiredTextColor(bytes: Long, defaultColor: Int): Int {
    //        return when {
    //            bytes > limitBytes -> Color.RED
    //            bytes > warningBytes -> Color.YELLOW
    //            else -> defaultColor
    //        }
    //    }
}

