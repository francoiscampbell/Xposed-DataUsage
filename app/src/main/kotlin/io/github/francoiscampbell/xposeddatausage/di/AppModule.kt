package io.github.francoiscampbell.xposeddatausage.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.XModuleResources
import android.net.ConnectivityManager
import android.net.INetworkStatsService
import android.net.TrafficStats
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import de.robv.android.xposed.XposedHelpers
import io.github.francoiscampbell.xposeddatausage.R
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.settings.Settings
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.widget.DataUsagePresenter
import io.github.francoiscampbell.xposeddatausage.widget.DataUsagePresenterImpl
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageView
import javax.inject.Named

/**
 * Created by francois on 16-03-30.
 */
@Module
class AppModule(private val dataUsageView: DataUsageView,
                private val xposedModulePath: String) {
    @Provides
    fun provideDataUsageView(): DataUsageView = dataUsageView

    @Provides
    fun provideDataUsagePresenter(impl: DataUsagePresenterImpl): DataUsagePresenter = impl

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
    fun provideAppContext() = dataUsageView.androidView.context.applicationContext

    @Provides
    fun provideTelephonyManager(@Named("app") context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    fun provideConnectivityManager(@Named("app") context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideXModuleResources(): XModuleResources {
        return XModuleResources.createInstance(xposedModulePath, null)
    }

    @Provides
    fun provideSharedPreferences(res: XModuleResources, @Named("app") context: Context): SharedPreferences {
        val prefsCache = res.getString(R.string.module_prefs_cache_name)
        return context.getSharedPreferences(prefsCache, Context.MODE_PRIVATE)
    }
}