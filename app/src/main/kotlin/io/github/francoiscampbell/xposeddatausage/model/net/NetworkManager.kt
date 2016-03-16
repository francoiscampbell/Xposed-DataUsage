package io.github.francoiscampbell.xposeddatausage.model.net

/**
 * Created by francois on 16-03-16.
 */
interface NetworkManager {
    val isCurrentNetworkMobile: Boolean
    fun setConnectivityChangeCallback(callback: () -> Unit)
}