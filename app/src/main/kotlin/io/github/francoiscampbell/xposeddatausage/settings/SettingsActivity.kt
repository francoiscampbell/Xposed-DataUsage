package io.github.francoiscampbell.xposeddatausage.settings

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by francois on 16-03-15.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        fragmentManager.beginTransaction().add(0, SettingsFragment.newInstance()).commit()
    }
}