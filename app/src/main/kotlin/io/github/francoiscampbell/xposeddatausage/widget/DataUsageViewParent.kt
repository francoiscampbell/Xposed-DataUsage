package io.github.francoiscampbell.xposeddatausage.widget

import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by francois on 16-04-10.
 */
interface DataUsageViewParent {
    val clock: TextView
    val notificationArea: ViewGroup
    val systemIconArea: ViewGroup
}