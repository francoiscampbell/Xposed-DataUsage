package io.github.francoiscampbell.xposeddatausage

import dagger.Component
import io.github.francoiscampbell.xposeddatausage.di.AppComponent
import io.github.francoiscampbell.xposeddatausage.di.MockAppModule
import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher
import io.github.francoiscampbell.xposeddatausage.widget.DataUsagePresenterImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class DataUsagePresenterImplTest() {
    private val mockAppComponent = DaggerDataUsagePresenterImplTest_MockAppComponent.create()
    private val dataUsagePresenterImpl = mockAppComponent.dataUsagePresenterImpl()
    private val dataUsageView = mockAppComponent.dataUsageView()
    private val dataUsageFetcher = mockAppComponent.dataUsageFetcher()

    @Before
    fun setUp() {
        dataUsagePresenterImpl.attachView(dataUsageView)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }
}

@Component(modules = arrayOf(MockAppModule::class))
interface DataUsagePresenterImplTest_MockAppComponent : AppComponent {
    fun dataUsagePresenterImpl(): DataUsagePresenterImpl
    fun dataUsageFetcher(): DataUsageFetcher
}