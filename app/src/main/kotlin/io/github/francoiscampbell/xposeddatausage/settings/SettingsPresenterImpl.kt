package io.github.francoiscampbell.xposeddatausage.settings

import android.content.Context

/**
 * Created by francois on 16-03-15.
 */
class SettingsPresenterImpl(val context: Context) : SettingsPresenter {
    private val settings = SettingsImpl(context)
}