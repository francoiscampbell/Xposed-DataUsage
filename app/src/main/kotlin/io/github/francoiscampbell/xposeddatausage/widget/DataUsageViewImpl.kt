package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.github.francoiscampbell.xposeddatausage.log.XposedLog
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by francois on 16-03-11.
 */
class DataUsageViewImpl @Inject constructor(
        @Named("ui") context: Context,
        val parent: DataUsageViewParent,
        val presenter: DataUsagePresenter
) : DataUsageView {

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
            androidView.textSize = pxToSp(parent.clock.textSize) / lines
            bytesText = bytesText //reset text
        }

    override var visible: Boolean
        get() = androidView.visibility == View.VISIBLE
        set(value) {
            androidView.visibility = if (value == true) View.VISIBLE else View.GONE
        }

    override var colorOverride: Int? = null

    init {
        XposedLog.i("Init Xposed-DataUsageView")

        attachViewToParent()
        setupViewParams()
        trackClockStyleChanges()
        trackColorOverrideChanges()
        presenter.attachView(this)
    }

    private fun attachViewToParent() {
        parent.systemIconArea.addView(androidView, 0)
    }

    private fun setupViewParams() {
        val clock = parent.clock
        androidView.apply {
            setPadding(clock.paddingLeft / 2, clock.paddingTop, clock.paddingLeft / 2, clock.paddingBottom) //clock has no right padding, so use left for this view's right
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
    }

    private fun trackClockStyleChanges() {
        val clock = parent.clock
        clock.viewTreeObserver.addOnPreDrawListener {
            androidView.alpha = clock.alpha
            androidView.typeface = clock.typeface
            return@addOnPreDrawListener true
        }
    }

    private fun trackColorOverrideChanges() {
        androidView.viewTreeObserver.addOnPreDrawListener {
            androidView.setTextColor(colorOverride ?: parent.clock.currentTextColor)
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