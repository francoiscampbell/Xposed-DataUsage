package io.github.francoiscampbell.xposeddatausage.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.INetworkStatsService
import android.net.TrafficStats
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.settings.Settings
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.widget.*
import javax.inject.Named

/**
 * Created by francois on 16-03-30.
 */
@Module
open class AppModule(private val hookedContext: Context,
                     private val liparam: XC_LayoutInflated.LayoutInflatedParam) {
    @Provides
    @Named("ui")
    fun provideUiContext() = hookedContext

    @Provides
    fun provideDataUsageView(impl: DataUsageViewImpl): DataUsageView = impl

    @Provides
    fun provideDataUsageViewParent(hookedStatusBar: HookedStatusBar): DataUsageViewParent = hookedStatusBar

    @Provides
    fun provideDataUsagePresenter(impl: DataUsagePresenterImpl): DataUsagePresenter = impl

    @Provides
    fun provideLayoutInflatedParam(): XC_LayoutInflated.LayoutInflatedParam = liparam

    @Provides
    fun provideDataUsageFormatter(): DataUsageFormatter = DataUsageFormatter()

    @Provides
    fun provideDataUsageFetcher(impl: DataUsageFetcherImpl): DataUsageFetcher = impl

    @Provides
    fun provideNetworkManager(impl: NetworkManagerImpl): NetworkManager = impl

    @Provides
    fun provideSettings(impl: SettingsImpl): Settings = impl

    @Provides
    fun provideINetworkStatsService(): INetworkStatsService {
        return XposedHelpers.callStaticMethod(TrafficStats::class.java, "getStatsService") as INetworkStatsService
    }

    @Provides
    @Named("app")
    fun provideAppContext() = hookedContext.applicationContext

    @Provides
    fun provideTelephonyManager(@Named("app") context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    fun provideConnectivityManager(@Named("app") context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}