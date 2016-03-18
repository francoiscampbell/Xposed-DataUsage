package io.github.francoiscampbell.xposeddatausage.model.settings


/**
 * Created by francois on 16-03-15.
 */
interface Settings {
    fun update(listener: OnSettingsChangedListener)
}