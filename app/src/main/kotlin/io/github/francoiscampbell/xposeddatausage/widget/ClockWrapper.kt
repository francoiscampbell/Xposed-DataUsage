package io.github.francoiscampbell.xposeddatausage.widget

import android.widget.TextView

/**
 * Created by francois on 16-03-16.
 */
class ClockWrapper(val clock: TextView) {
    var colorOverride: Int = clock.currentTextColor
}