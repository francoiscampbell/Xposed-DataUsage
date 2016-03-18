package io.github.francoiscampbell.xposeddatausage.widget

import android.graphics.Color
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.settings.OnSettingsChangedListener
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.ByteFormatter
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(private val view: DataUsageView, private val clockWrapper: ClockWrapper) : DataUsagePresenter, OnSettingsChangedListener {
    private val fetcher = DataUsageFetcherImpl()

    private val networkManager = NetworkManagerImpl()
    private val settings = SettingsImpl()
    private val byteFormatter = ByteFormatter()

    init {
        settings.update(this)
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
            view.text = byteFormatter.format(bytes)
            clockWrapper.colorOverride = when {
                bytes > limitBytes && limitBytes > 0 -> Color.RED
                bytes > warningBytes && warningBytes > 0 -> Color.YELLOW
                else -> null
            }
        }
    }

    override fun onUnitChanged(newUnit: ByteFormatter.UnitFormat) {
        byteFormatter.unit = newUnit
        XposedBridge.log("newUnit: $newUnit")
    }

    override fun onDecimalPlacesChanged(newDecimalPlaces: Int) {
        byteFormatter.decimalPlaces = newDecimalPlaces
        XposedBridge.log("newDecimalPlaces: $newDecimalPlaces")
    }
}

