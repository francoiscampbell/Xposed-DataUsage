package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.github.francoiscampbell.xposeddatausage.log.XposedLog

/**
 * Created by francois on 16-03-11.
 */
class DataUsageViewImpl
@JvmOverloads
constructor(context: Context, private val clockWrapper: ClockWrapper, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr), DataUsageView {
    private val presenter = DataUsagePresenterImpl(this, clockWrapper)

    override var bytesText: String
        get() = super.getText().toString()
        set(value) {
            text = when (numLines) {
                1 -> value.replace('\n', ' ')
                else -> value.replace(' ', '\n')
            }
        }

    override var numLines: Int
        get() = minLines
        set(value) {
            setLines(value)
            textSize = pxToSp(clockWrapper.clock.textSize) / value
            bytesText = bytesText //reset text

            requestLayout()
            invalidate()
        }

    override var visible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value == true) View.VISIBLE else View.GONE
        }

    init {
        setupViewParams()
        trackClockStyleChanges()
        trackColorOverrideChanges()
        XposedLog.i("Init Xposed-DataUsageView")
    }

    private fun setupViewParams() {
        val clock = clockWrapper.clock
        setPadding(clock.paddingLeft / 2, clock.paddingTop, clock.paddingLeft / 2, clock.paddingBottom) //clock has no right padding, so use left for this view's right
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        textSize = pxToSp(clock.textSize)

        visible = false
        gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
    }

    private fun trackClockStyleChanges() {
        val clock = clockWrapper.clock
        clock.viewTreeObserver.addOnDrawListener {
            alpha = clock.alpha
            typeface = clock.typeface
        }
    }

    private fun trackColorOverrideChanges() {
        viewTreeObserver.addOnPreDrawListener {
            setTextColor(clockWrapper.colorOverride ?: clockWrapper.clock.currentTextColor)
            return@addOnPreDrawListener true
        }
    }

    override fun getText() = bytesText

    override fun update() {
        presenter.updateBytes()
    }

    private fun pxToSp(px: Float): Float {
        return px / resources.displayMetrics.scaledDensity
    }
}