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

    private var currentNetworkType: NetworkManager.NetworkType = NetworkManager.NetworkType.UNKNOWN
    private var monitoredNetworkTypes: Set<NetworkManager.NetworkType> = setOf(NetworkManager.NetworkType.UNKNOWN)
    private var onlyWhenConnected = true

    override fun attach(view: DataUsageView) {
        this.view = view

        settings.attach(this)
        setConnectivityChangeCallback()
        updateBytes()
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { currentNetworkType ->
            this.currentNetworkType = currentNetworkType
            setViewVisibility()
            updateBytes()
        }
    }

    override fun updateBytes(): Unit {
        if (!view.visible) return

        try {
            val dataUsage = fetcher.getCurrentCycleBytes(currentNetworkType)
            view.bytesText = dataUsageFormatter.format(dataUsage)
            view.overrideTextColorHighUsage = dataUsageFormatter.getColor(dataUsage)
        } catch (e: Exception) {
            XposedLog.e("Error updating bytes", e)
            when (e) {
                is IllegalStateException -> view.bytesText = "?"
                is NullPointerException -> {
                    view.bytesText = "ERR"
                    view.overrideTextColorHighUsage = Color.RED
                }
            }
        }
    }

    private fun setViewVisibility() {
        XposedLog.i("onlyWhenConnected: $onlyWhenConnected")
        XposedLog.i("currentNetworkType: $currentNetworkType")
        XposedLog.i("monitoredNetworkTypes: $monitoredNetworkTypes")
        view.visible = !onlyWhenConnected || currentNetworkType in monitoredNetworkTypes
    }

    override fun onMonitoredNetworkTypesChanged(monitoredNetworkTypes: Set<NetworkManager.NetworkType>) {
        this.monitoredNetworkTypes = monitoredNetworkTypes
        setViewVisibility()
        updateBytes()
    }

    override fun onOnlyWhenConnectedChanged(onlyWhenConnected: Boolean) {
        this.onlyWhenConnected = onlyWhenConnected
        setViewVisibility()
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

    override fun onTwoLinesChanged(showOnTwoLines: Boolean) {
        view.twoLines = showOnTwoLines
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

    override fun onUseCustomTextColorChanged(useCustomTextColor: Boolean) {
        view.useCustomTextColor = useCustomTextColor
    }

    override fun onCustomTextColorChanged(newTextColor: Int) {
        view.customTextColor = newTextColor
    }

    override fun onUseOverrideTextColorHighUsageChanged(useOverride: Boolean) {
        view.useOverrideTextColorHighUsage = useOverride
        updateBytes()
    }

    override fun onDebugLoggingChanged(shouldDebugLog: Boolean) {
        XposedLog.debugLogging = shouldDebugLog
    }
}

