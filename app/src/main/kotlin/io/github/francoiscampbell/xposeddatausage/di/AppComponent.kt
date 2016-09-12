package io.github.francoiscampbell.xposeddatausage.di

import android.os.DataUsageService
import dagger.Component
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher

/**
 * Created by francois on 16-03-30.
 */
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun dataUsageFetcher(): DataUsageFetcher
    fun dataUsageService(): DataUsageService
}