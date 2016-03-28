package io.github.francoiscampbell.xposeddatausage.widget

/**
 * Created by francois on 16-03-14.
 */
interface DataUsageView {
    var bytesText: String
    var numLines: Int
    var visible: Boolean
    fun update()
}