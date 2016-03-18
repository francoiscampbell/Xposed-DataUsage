package io.github.francoiscampbell.xposeddatausage.model.net

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.github.francoiscampbell.xposeddatausage.Module
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver

/**
 * Created by francois on 16-03-16.
 */
class NetworkManagerImpl : NetworkManager {
    private val context = Module.hookedContext
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isCurrentNetworkMobile: Boolean
        get() = when (connectivityManager.activeNetworkInfo?.type) {
            ConnectivityManager.TYPE_MOBILE,
            ConnectivityManager.TYPE_MOBILE_DUN,
            ConnectivityManager.TYPE_WIMAX -> {
                true
            }
            else -> {
                false
            }
        }

    override fun setConnectivityChangeCallback(callback: () -> Unit) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(intentFilter) { context, intent ->
            callback()
        }
    }
}