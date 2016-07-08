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

    private var monitoredNetworkTypes: Set<NetworkManager.NetworkType> = setOf()
    private var defaultMonitoredNetworkType = NetworkManager.NetworkType.NONE

    override fun attach(view: DataUsageView) {
        this.view = view

        settings.attach(this)
        setConnectivityChangeCallback()
        update()
    }

    private fun setConnectivityChangeCallback() {
        networkManager.setConnectivityChangeCallback { update() }
    }

    override fun update(): Unit {
        val currentNetworkType = networkManager.currentNetworkType
        val networkToQuery = if (currentNetworkType !in monitoredNetworkTypes) defaultMonitoredNetworkType else currentNetworkType

        if (networkToQuery == NetworkManager.NetworkType.NONE) {
            view.visible = false
            return
        }
        view.visible = true

        try {
            val dataUsage = fetcher.getCurrentCycleBytes(networkToQuery)
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

    override fun onMonitoredNetworkTypesChanged(monitoredNetworkTypes: Set<NetworkManager.NetworkType>) {
        this.monitoredNetworkTypes = monitoredNetworkTypes
        update()
    }

    override fun onDefaultMonitoredNetworkTypeChanged(defaultMonitoredNetworkType: NetworkManager.NetworkType) {
        this.defaultMonitoredNetworkType = defaultMonitoredNetworkType
        update()
    }

    override fun onRelativeToPaceChanged(relativeToPace: Boolean) {
        dataUsageFormatter.relativeToPace = relativeToPace
        update()
    }

    override fun onUnitChanged(newUnit: DataUsageFormatter.UnitFormat) {
        dataUsageFormatter.format = newUnit
        update()
    }

    override fun onDecimalPlacesChanged(newDecimalPlaces: Int) {
        dataUsageFormatter.decimalPlaces = newDecimalPlaces
        update()
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
        update()
    }

    override fun onDebugLoggingChanged(shouldDebugLog: Boolean) {
        XposedLog.debugLogging = shouldDebugLog
    }
}

