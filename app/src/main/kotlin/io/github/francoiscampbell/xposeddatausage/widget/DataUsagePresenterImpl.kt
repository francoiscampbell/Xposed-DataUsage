package io.github.francoiscampbell.xposeddatausage.widget

import android.graphics.Color
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.BuildConfig
import io.github.francoiscampbell.xposeddatausage.model.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(private val view: DataUsageView, private val clockWrapper: ClockWrapper) : DataUsagePresenter {
    private val fetcher = DataUsageFetcherImpl()
    private val networkManager = NetworkManagerImpl()

    init {
        showViewIfMobile()
        setConnectivityChangeCallback()
    }

    private fun showViewIfMobile() {
        view.visible = networkManager.isCurrentNetworkMobile
        updateBytes()
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { showViewIfMobile() }
    }

    override fun updateBytes(): Unit {
        if (!view.visible) {
            return
        }

        fetcher.getCurrentCycleBytes { bytes, warningBytes, limitBytes ->
            view.text = ByteFormatter.format(bytes, 2, ByteFormatter.BytePrefix.SMART_SI)
            clockWrapper.colorOverride = when {
                bytes > limitBytes -> Color.RED
                bytes > warningBytes -> Color.YELLOW
                else -> null
            }

            if (BuildConfig.DEBUG) {
                XposedBridge.log("Updating data usage counter to: ${view.text}. warningBytes: $warningBytes, limitBytes: $limitBytes")
                XposedBridge.log("Color override: ${clockWrapper.colorOverride}")
            }
        }
    }
}

