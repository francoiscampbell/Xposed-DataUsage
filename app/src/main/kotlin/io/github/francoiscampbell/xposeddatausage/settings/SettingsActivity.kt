package io.github.francoiscampbell.xposeddatausage.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.francoiscampbell.xposeddatausage.R

/**
 * Created by francois on 16-03-15.
 */
class SettingsActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        fragmentManager.beginTransaction().replace(R.id.prefs_container, SettingsFragment.newInstance()).commit()
    }
}