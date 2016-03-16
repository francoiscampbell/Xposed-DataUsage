package io.github.francoiscampbell.xposeddatausage.settings

import io.github.francoiscampbell.xposeddatausage.model.settings.SettingsImpl

/**
 * Created by francois on 16-03-15.
 */
class SettingsPresenterImpl(val settingsView: SettingsView) : SettingsPresenter {
    private val settings = SettingsImpl()
}