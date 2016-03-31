package io.github.francoiscampbell.xposeddatausage.widget

interface DataUsagePresenter {
    fun attachView(view: DataUsageView)
    fun updateBytes(): Unit
}