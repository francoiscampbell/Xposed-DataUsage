package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import de.robv.android.xposed.XposedBridge

/**
 * Created by francois on 16-03-11.
 */
class DataUsageViewImpl
@JvmOverloads
constructor(context: Context, private val clockWrapper: ClockWrapper, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr), DataUsageView {
    private val presenter = DataUsagePresenterImpl(this, clockWrapper)

    override var text: String
        get() = getText().toString()
        set(value) {
            setText(value)
        }

    override var visible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = when (value) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

    init {
        visible = false
        trackClockStyleChanges()
        trackColorOverrideChanges()
        XposedBridge.log("Init Xposed-DataUsageView")
    }

    private fun trackClockStyleChanges() {
        val clock = clockWrapper.clock
        clock.viewTreeObserver.addOnDrawListener {
            //            setTextColor(clock.currentTextColor)
            alpha = clock.alpha
            typeface = clock.typeface
            layoutParams = clock.layoutParams
            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
    }

    private fun trackColorOverrideChanges() {
        viewTreeObserver.addOnPreDrawListener {
            setTextColor(clockWrapper.colorOverride ?: clockWrapper.clock.currentTextColor)
            return@addOnPreDrawListener true
        }
    }

    override fun update() {
        presenter.updateBytes()
    }
}