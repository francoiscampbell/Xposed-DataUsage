package io.github.francoiscampbell.xposeddatausage.nonmodule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by francois on 2016-07-08.
 */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        SettingsChangeActions(context).apply {
            startListeningForChanges()
            stopListeningForChanges()
        }
    }
}