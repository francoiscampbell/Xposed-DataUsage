package io.github.francoiscampbell.xposeddatausage.di

import dagger.Component
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageView

/**
 * Created by francois on 16-03-30.
 */
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
        fun dataUsageView(): DataUsageView
}