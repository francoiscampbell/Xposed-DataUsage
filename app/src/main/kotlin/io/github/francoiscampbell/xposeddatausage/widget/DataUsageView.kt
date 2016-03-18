package io.github.francoiscampbell.xposeddatausage.widget

/**
 * Created by francois on 16-03-14.
 */
interface DataUsageView {
    var text: String
    var visible: Boolean
    fun update()
}