package io.github.francoiscampbell.xposeddatausage.di

import android.content.Context
import android.net.INetworkStatsService
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcherHookImpl

/**
 * Created by francois on 16-03-30.
 */
@Module
open class AppModule(private val hookedContext: Context,
                     private val iNetworkStatsService: INetworkStatsService) {

    @Provides
    fun provideContext() = hookedContext

    @Provides
    fun provideINetworkStatsService() = iNetworkStatsService

    @Provides
    fun provideTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    fun provideDataUsageFetcher(impl: DataUsageFetcherHookImpl): DataUsageFetcher = impl
}