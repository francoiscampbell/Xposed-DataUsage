package io.github.francoiscampbell.xposeddatausage.di

import dagger.Module
import dagger.Provides
import io.github.francoiscampbell.xposeddatausage.model.net.NetworkManager
import io.github.francoiscampbell.xposeddatausage.model.settings.Settings
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFormatter
import io.github.francoiscampbell.xposeddatausage.widget.DataUsageView
import org.mockito.Mockito

@Module
class MockAppModule {
    @Provides
    fun provideMockDataUsageView() = Mockito.mock(DataUsageView::class.java)

    @Provides
    fun provideMockDataUsageFetcher() = Mockito.mock(DataUsageFetcher::class.java)

    @Provides
    fun provideMockNetworkManager() = Mockito.mock(NetworkManager::class.java)

    @Provides
    fun provideMockSettings() = Mockito.mock(Settings::class.java)

    @Provides
    fun provideMockDataUsageFormatter() = Mockito.mock(DataUsageFormatter::class.java)
}