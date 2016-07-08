package io.github.francoiscampbell.xposeddatausage.widget

interface DataUsagePresenter {
    fun attach(view: DataUsageView)
    fun update(): Unit
}