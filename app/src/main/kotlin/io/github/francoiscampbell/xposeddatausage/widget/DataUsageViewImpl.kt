package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import javax.inject.Inject

/**
 * Created by francois on 16-03-11.
 */
class DataUsageViewImpl(
        context: Context,
        val clockWrapper: ClockWrapper) : DataUsageView {

    @Inject lateinit var presenter: DataUsagePresenter

    override val androidView = TextView(context)

    override var bytesText: String
        get() = androidView.text.toString()
        set(value) {
            androidView.text = if (twoLines) value.replace(' ', '\n') else value.replace('\n', ' ')
        }

    override var twoLines: Boolean
        get() = androidView.minLines == 2
        set(value) {
            val lines = if (value) 2 else 1
            androidView.setLines(lines)
            androidView.textSize = pxToSp(clockWrapper.clock.textSize) / lines
            bytesText = bytesText //reset text
        }

    override var visible: Boolean
        get() = androidView.visibility == View.VISIBLE
        set(value) {
            androidView.visibility = if (value == true) View.VISIBLE else View.GONE
        }

    override var colorOverride: Int? = null

    init {
        setupViewParams()
        trackClockStyleChanges()
        trackColorOverrideChanges()
        XposedLog.i("Init Xposed-DataUsageView")
    }

    private fun setupViewParams() {
        val clock = clockWrapper.clock
        androidView.apply {
            setPadding(clock.paddingLeft / 2, clock.paddingTop, clock.paddingLeft / 2, clock.paddingBottom) //clock has no right padding, so use left for this view's right
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
    }

    private fun trackClockStyleChanges() {
        val clock = clockWrapper.clock
        clock.viewTreeObserver.addOnPreDrawListener {
            androidView.alpha = clock.alpha
            androidView.typeface = clock.typeface
            return@addOnPreDrawListener true
        }
    }

    private fun trackColorOverrideChanges() {
        androidView.viewTreeObserver.addOnPreDrawListener {
            androidView.setTextColor(colorOverride ?: clockWrapper.clock.currentTextColor)
            return@addOnPreDrawListener true
        }
    }

    override fun update() {
        presenter.updateBytes()
    }

    private fun pxToSp(px: Float): Float {
        return px / androidView.resources.displayMetrics.scaledDensity
    }
}