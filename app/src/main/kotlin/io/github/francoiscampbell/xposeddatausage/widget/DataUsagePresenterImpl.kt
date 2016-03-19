package io.github.francoiscampbell.xposeddatausage.widget

import android.graphics.Color
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
        showView(settings.onlyIfMobile)
        setConnectivityChangeCallback()

        updateBytes()
    }

    private fun showView(onlyIfMobile: Boolean) {
        view.visible = !onlyIfMobile || networkManager.isCurrentNetworkMobile
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { showView(settings.onlyIfMobile) }
    }

    override fun updateBytes(): Unit {
        if (!view.visible) {
            return
        }

        fetcher.getCurrentCycleBytes { bytes, warningBytes, limitBytes ->
            view.text = byteFormatter.format(bytes, warningBytes, limitBytes)
            clockWrapper.colorOverride = when {
                bytes > limitBytes && limitBytes > 0 -> Color.RED
                bytes > warningBytes && warningBytes > 0 -> Color.YELLOW
                else -> null
            }
        }
    }

    override fun onOnlyWhenMobileChanged(onlyWhenMobile: Boolean) {
        showView(onlyWhenMobile)
        updateBytes()
    }

    override fun onUnitChanged(newUnit: ByteFormatter.UnitFormat) {
        byteFormatter.format = newUnit
        updateBytes()
    }

    override fun onDecimalPlacesChanged(newDecimalPlaces: Int) {
        byteFormatter.decimalPlaces = newDecimalPlaces
        updateBytes()
    }
}

