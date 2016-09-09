package io.github.francoiscampbell.xposeddatausage.di

import dagger.Component
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageView

/**
 * Created by francois on 2016-09-09.
 */
@Component(modules = arrayOf(UiModule::class))
interface UiComponent {
    fun dataUsageView(): DataUsageView
}