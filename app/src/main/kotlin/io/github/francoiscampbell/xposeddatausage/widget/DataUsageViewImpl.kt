package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
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
            textSize = textSize //reset text size
            bytesText = bytesText //reset text
        }

    override var visible: Boolean
        get() = androidView.visibility == View.VISIBLE
        set(value) {
            androidView.visibility = if (value == true) View.VISIBLE else View.GONE
        }

    override var position = Position.RIGHT
        set(value) {
            detachViewFromParent()
            when (value) {
                Position.FAR_LEFT -> parent.notificationArea.addView(androidView, 0)
                Position.RIGHT -> parent.systemIconArea.addView(androidView, 0)
                Position.FAR_RIGHT -> parent.systemIconArea.addView(androidView)
            }
            field = value
        }

    override var alignment: Alignment = Alignment.CENTER
        set(value) {
            androidView.gravity = when (value) {
                Alignment.LEFT -> Gravity.LEFT or Gravity.CENTER_VERTICAL
                Alignment.CENTER -> Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                Alignment.RIGHT -> Gravity.RIGHT or Gravity.CENTER_VERTICAL
            }
            field = value
        }

    override var textSize: Float = pxToSp(parent.clock.textSize)
        set(value) {
            val size = when (value) {
                0f -> pxToSp(parent.clock.textSize)
                else -> Math.max(value, 0f)
            }
            androidView.textSize = if (twoLines) size / 2 else size
            field = size
        }

    override var useCustomTextColor: Boolean = false
        set (value) {
            field = value
            androidView.invalidate()
        }

    override var customTextColor = parent.clock.currentTextColor
        set (value) {
            field = value
            androidView.invalidate()
        }

    override var useOverrideTextColorHighUsage: Boolean = true
        set (value) {
            field = value
            androidView.invalidate()
        }

    override var overrideTextColorHighUsage: Int? = null
        set (value) {
            field = value
            androidView.invalidate()
        }

    init {
        XposedLog.i("Init Xposed-DataUsageView")

        setupViewParams()
        trackClockStyleChanges()
        trackColorOverrideChanges()
        presenter.attachView(this)
    }

    private fun detachViewFromParent() = (androidView.parent as ViewGroup?)?.removeView(androidView)

    private fun setupViewParams() {
        val clock = parent.clock
        androidView.apply {
            androidView.setPadding(clock.paddingLeft / 2, clock.paddingTop, clock.paddingLeft / 2, clock.paddingBottom) //clock has no right padding, so use left for this view's right
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        }
    }

    private fun trackClockStyleChanges() {
        parent.clock.apply {
            viewTreeObserver.addOnPreDrawListener {
                androidView.alpha = alpha
                androidView.typeface = typeface
                return@addOnPreDrawListener true
            }
        }
    }

    private fun trackColorOverrideChanges() {
        androidView.viewTreeObserver.addOnPreDrawListener {
            val overrideTextColorHighUsage = overrideTextColorHighUsage
            if (useOverrideTextColorHighUsage && overrideTextColorHighUsage != null) {
                androidView.setTextColor(overrideTextColorHighUsage)
            } else if (useCustomTextColor) {
                androidView.setTextColor(customTextColor)
            } else {
                androidView.setTextColor(parent.clock.currentTextColor)
            }
            return@addOnPreDrawListener true
        }
    }

    override fun update() = presenter.updateBytes()

    private fun pxToSp(px: Float): Float = px / androidView.resources.displayMetrics.scaledDensity
}