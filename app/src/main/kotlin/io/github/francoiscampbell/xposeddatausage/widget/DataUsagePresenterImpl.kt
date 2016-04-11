package io.github.francoiscampbell.xposeddatausage.widget

import android.graphics.Color
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.settings.OnSettingsChangedListener
import io.github.francoiscampbell.xposeddatausage.model.settings.Settings
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import javax.inject.Inject

/**
 * Created by francois on 16-03-12.
 */
class DataUsagePresenterImpl @Inject constructor(
        private val fetcher: DataUsageFetcher,
        private val networkManager: NetworkManager,
        private val settings: Settings,
        private val dataUsageFormatter: DataUsageFormatter
) : DataUsagePresenter, OnSettingsChangedListener {

    private lateinit var view: DataUsageView

    override fun attachView(view: DataUsageView) {
        this.view = view

        settings.update(this)
        setConnectivityChangeCallback()
        updateBytes()
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { showView(settings.onlyIfMobile) }
    }

    override fun updateBytes(): Unit {
        if (!view.visible) {
            return
        }

        fetcher.getCurrentCycleBytes({ dataUsage ->
            view.bytesText = dataUsageFormatter.format(dataUsage)
            view.colorOverride = dataUsageFormatter.getColor(dataUsage)
        }, { throwable ->
            XposedLog.e("Error updating bytes", throwable)
            when (throwable) {
                is IllegalStateException -> view.bytesText = "?"
                is NullPointerException -> {
                    view.bytesText = "ERR"
                    view.colorOverride = Color.RED
                }
            }
        })
    }

    private fun showView(onlyIfMobile: Boolean) {
        view.visible = !onlyIfMobile || networkManager.isCurrentNetworkMobile
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

    override fun onTwoLinesChanged(newTwoLines: Boolean) {
        view.twoLines = newTwoLines
    }

    override fun onPositionChanged(newPosition: Position) {
        view.position = newPosition
    }

    override fun onAlignmentChanged(newAlignment: Alignment) {
        view.alignment = newAlignment
    }

    override fun onTextSizeChanged(newTextSize: Float) {
        view.textSize = newTextSize
    }

    override fun onDebugLoggingChanged(shouldDebugLog: Boolean) {
        XposedLog.debugLogging = shouldDebugLog
    }
}

