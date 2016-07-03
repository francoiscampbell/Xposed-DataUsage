package io.github.francoiscampbell.xposeddatausage.widget

import android.os.Build
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import io.github.francoiscampbell.xposeddatausage.util.findViewById
import javax.inject.Inject

/**
 * Created by francois on 16-04-10.
 */
class HookedStatusBar @Inject constructor(
        val liparam: XC_LayoutInflated.LayoutInflatedParam
) : DataUsageViewParent {

    override val clock: TextView
        get() = liparam.findViewById("clock") as TextView

    override val notificationArea: ViewGroup
        get() = when (Build.VERSION.SDK_INT) {
            in Build.VERSION_CODES.JELLY_BEAN..Build.VERSION_CODES.KITKAT -> liparam.findViewById("notification_icon_area")
            else -> liparam.findViewById("notification_icon_area_inner")
        } as ViewGroup

    override val systemIconArea: ViewGroup
        get() = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.JELLY_BEAN -> liparam.findViewById("system_icon_area") //TODO find solution https://github.com/android/platform_frameworks_base/blob/jb-release/packages/SystemUI/res/layout/status_bar.xml
            else -> liparam.findViewById("system_icon_area")
        } as ViewGroup
}