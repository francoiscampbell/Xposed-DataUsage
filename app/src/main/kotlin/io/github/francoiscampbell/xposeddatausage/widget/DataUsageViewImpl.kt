package io.github.francoiscampbell.xposeddatausage.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import de.robv.android.xposed.XposedBridge

/**
 * Created by francois on 16-03-11.
 */
class DataUsageViewImpl
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr), DataUsageView {
    private val presenter = DataUsagePresenterImpl(this, context)

    init {
        hide()
        XposedBridge.log("Init Xposed-DataUsage")
    }

    override fun update() {
        text = presenter.getCurrentCycleBytes()
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}