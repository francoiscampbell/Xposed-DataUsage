package io.github.francoiscampbell.xposeddatausage.di

import android.content.Context
import android.content.res.XModuleResources
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManagerImpl
import io.github.francoiscampbell.xposeddatausage.model.settings.Settings
import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherImpl
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.widget.*

/**
 * Created by francois on 2016-09-09.
 */
@Module
class UiModule(private val xposedModulePath: String,
               private val hookedContext: Context,
               private val liparam: XC_LayoutInflated.LayoutInflatedParam) {
    @Provides
    fun provideContext() = hookedContext

    @Provides
    fun provideXModuleResources(): XModuleResources {
        return XModuleResources.createInstance(xposedModulePath, null)
    }

    @Provides
    fun provideSettings(impl: SettingsImpl): Settings = impl

    @Provides
    fun provideLayoutInflatedParam(): XC_LayoutInflated.LayoutInflatedParam = liparam

    @Provides
    fun provideDataUsageView(impl: DataUsageViewImpl): DataUsageView = impl

    @Provides
    fun provideDataUsageViewParent(hookedStatusBar: HookedStatusBar): DataUsageViewParent = hookedStatusBar

    @Provides
    fun provideDataUsagePresenter(impl: DataUsagePresenterImpl): DataUsagePresenter = impl


    @Provides
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideNetworkManager(impl: NetworkManagerImpl): NetworkManager = impl

    @Provides
    fun provideDataUsageFormatter(): DataUsageFormatter = DataUsageFormatter()

    @Provides
    fun provideDataUsageFetcher(impl: DataUsageFetcherImpl): DataUsageFetcher = impl
}