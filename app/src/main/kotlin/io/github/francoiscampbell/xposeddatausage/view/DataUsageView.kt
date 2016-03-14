package io.github.francoiscampbell.xposeddatausage.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import de.robv.android.xposed.XposedBridge
import io.github.francoiscampbell.xposeddatausage.presenter.DataUsagePresenterImpl
import io.github.francoiscampbell.xposeddatausage.util.ByteFormatter

/**
 * Created by francois on 16-03-11.
 */
class DataUsageView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {
    private val presenter = DataUsagePresenterImpl(context)

    init {
        XposedBridge.log("Init Xposed-DataUsage")
    }

    fun update() {
        text = ByteFormatter.format(presenter.getCurrentCycleBytes(), 2, ByteFormatter.BytePrefix.SMART_SI)
    }
}