package io.github.francoiscampbell.xposeddatausage.model.net

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.github.francoiscampbell.xposeddatausage.util.registerReceiver
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by francois on 16-03-16.
 */
class NetworkManagerImpl
@Inject constructor(
        @Named("app") private val context: Context,
        private val connectivityManager: ConnectivityManager
) : NetworkManager {
    override val isCurrentNetworkMobile: Boolean
        get() {
            val networkType = connectivityManager.activeNetworkInfo?.type
            return (networkType == ConnectivityManager.TYPE_MOBILE
                    || networkType == ConnectivityManager.TYPE_MOBILE_DUN
                    || networkType == ConnectivityManager.TYPE_WIMAX)
        }

    override fun setConnectivityChangeCallback(callback: () -> Unit) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(intentFilter) { context, intent ->
            callback()
        }
    }
}