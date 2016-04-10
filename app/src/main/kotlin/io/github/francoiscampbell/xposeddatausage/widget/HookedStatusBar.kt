package io.github.francoiscampbell.xposeddatausage.widget

import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import io.github.francoiscampbell.xposeddatausage.util.findViewById
import javax.inject.Inject

/**
 * Created by francois on 16-04-10.
 */
class HookedStatusBar @Inject constructor(val liparam: XC_LayoutInflated.LayoutInflatedParam) : DataUsageViewParent {
    override val clock: TextView
        get() = liparam.findViewById("clock") as TextView

    override val notificationArea: ViewGroup
        get() = liparam.findViewById("notification_icon_area_inner") as ViewGroup

    override val systemIconArea: ViewGroup
        get() = liparam.findViewById("system_icon_area") as ViewGroup
}