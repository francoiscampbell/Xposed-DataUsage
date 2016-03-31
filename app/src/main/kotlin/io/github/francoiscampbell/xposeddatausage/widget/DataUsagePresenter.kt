package io.github.francoiscampbell.xposeddatausage.widget

interface DataUsagePresenter {
    val view: DataUsageView
    fun updateBytes(): Unit
}