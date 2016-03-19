package io.github.francoiscampbell.xposeddatausage.widget

import android.graphics.Color
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.BuildConfig
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.settings.OnSettingsChangedListener
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl(private val view: DataUsageView, private val clockWrapper: ClockWrapper) : DataUsagePresenter, OnSettingsChangedListener {
    private val fetcher = DataUsageFetcherImpl()

    private val networkManager = NetworkManagerImpl()
    private val settings = SettingsImpl()
    private val dataUsageFormatter = DataUsageFormatter()

    init {
        settings.update(this)
        setConnectivityChangeCallback()
        updateBytes()
    }

    private fun showView(onlyIfMobile: Boolean) {
        val show = !onlyIfMobile || networkManager.isCurrentNetworkMobile
        view.visible = show
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { showView(settings.onlyIfMobile) }
    }

    override fun updateBytes(): Unit {
        if (!view.visible) {
            return
        }

        fetcher.getCurrentCycleBytes({ dataUsage ->
            if (BuildConfig.DEBUG) {
                XposedBridge.log("relativeToPace: ${settings.relativeToPace}")
                XposedBridge.log(dataUsage.toString())
            }
            view.text = dataUsageFormatter.format(dataUsage)
            clockWrapper.colorOverride = dataUsageFormatter.getColor(dataUsage)
        }, { throwable ->
            when (throwable) {
                is IllegalStateException -> view.text = "?"
                is NullPointerException -> {
                    view.text = "ERR"
                    clockWrapper.colorOverride = Color.RED
                }
            }
        })
    }

    override fun onOnlyWhenMobileChanged(onlyWhenMobile: Boolean) {
        showView(onlyWhenMobile)
        updateBytes()
    }

    override fun onRelativeToPaceChanged(relativeToPace: Boolean) {
        dataUsageFormatter.relativeToPace = relativeToPace
        updateBytes()
    }

    override fun onUnitChanged(newUnit: DataUsageFormatter.UnitFormat) {
        dataUsageFormatter.format = newUnit
        updateBytes()
    }

    override fun onDecimalPlacesChanged(newDecimalPlaces: Int) {
        dataUsageFormatter.decimalPlaces = newDecimalPlaces
        updateBytes()
    }
}

