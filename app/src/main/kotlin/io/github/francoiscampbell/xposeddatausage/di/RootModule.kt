package io.github.francoiscampbell.xposeddatausage.di

import android.content.res.XModuleResources
import dagger.Provides
import de.robv.android.xposed.XSharedPreferences

/**
 * Created by francois on 2016-07-07.
 */
@dagger.Module
class RootModule(private val xposedModulePath: String) {
    private val xSharedPreferences =
            XSharedPreferences(io.github.francoiscampbell.xposeddatausage.Module.PACKAGE_MODULE).apply { makeWorldReadable() }

    @Provides
    fun provideXModuleResources(): XModuleResources {
        return XModuleResources.createInstance(xposedModulePath, null)
    }

    @Provides
    fun provideXSharedPreferences(): XSharedPreferences = xSharedPreferences
}