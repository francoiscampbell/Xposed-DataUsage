package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import io.github.francoiscampbell.xposeddatausage.model.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(val view: DataUsageView, val context: Context) : DataUsagePresenter {

    private val fetcher = DataUsageFetcherImpl()
    private val networkManager = NetworkManagerImpl()

    init {
        showViewIfMobile()
        setConnectivityChangeCallback()
    }

    private fun showViewIfMobile() {
        if (networkManager.isCurrentNetworkMobile) {
            view.show()
        } else {
            view.hide()
        }
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { showViewIfMobile() }
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

