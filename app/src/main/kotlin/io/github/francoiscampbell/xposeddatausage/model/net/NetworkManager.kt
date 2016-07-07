package io.github.francoiscampbell.xposeddatausage.model.net

/**
 * Created by francois on 16-03-16.
 */
interface NetworkManager {
    val currentNetworkType: NetworkType
    fun setConnectivityChangeCallback(callback: (NetworkType) -> Unit)

    enum class NetworkType { UNKNOWN, MOBILE, WIFI }
}