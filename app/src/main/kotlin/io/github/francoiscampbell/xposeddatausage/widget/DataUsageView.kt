package io.github.francoiscampbell.xposeddatausage.widget

/**
 * Created by francois on 16-03-14.
 */
interface DataUsageView {
    var bytesText: String
    fun update()
    fun show()
    fun hide()
}