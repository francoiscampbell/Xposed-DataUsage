package io.github.francoiscampbell.xposeddatausage.model.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Created by francois on 16-03-16.
 */
class NetworkManagerImpl
@Inject constructor(
        private val context: Context,
        private val connectivityManager: ConnectivityManager
) : NetworkManager {
    override val currentNetworkType: NetworkManager.NetworkType
        get() = when (connectivityManager.activeNetworkInfo?.type) {
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_WIMAX -> NetworkManager.NetworkType.MOBILE
            ConnectivityManager.TYPE_WIFI -> NetworkManager.NetworkType.WIFI
            else -> NetworkManager.NetworkType.NONE
        }

    override fun setConnectivityChangeCallback(callback: () -> Unit) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) = callback()
        }, intentFilter)
    }
}