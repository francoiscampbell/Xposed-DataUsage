package io.github.francoiscampbell.xposeddatausage.di

import dagger.Component
import io.github.francoiscampbell.xposeddatausage.widget.DataUsagePresenter
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageViewImpl

/**
 * Created by francois on 16-03-30.
 */
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    //    fun dataUsageView(): DataUsageView
    //    fun clock(): TextView
    fun dataUsagePresenter(): DataUsagePresenter
    //    fun dataUsageFetcher(): DataUsageFetcher
    //    fun networkManager(): NetworkManager
    //    fun settings(): Settings
    //    fun dataUsageFormatter(): DataUsageFormatter
    //    fun iNetworkStatsService(): INetworkStatsService
    //    fun telephonyManager(): TelephonyManager
    //    fun connectivityManager(): ConnectivityManager
    //    fun xModuleResources(): XModuleResources
    //    fun sharedPreferences(): SharedPreferences

    fun inject(dataUsageView: DataUsageViewImpl)
}