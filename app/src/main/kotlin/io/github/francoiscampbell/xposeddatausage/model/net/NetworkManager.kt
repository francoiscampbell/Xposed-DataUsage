package io.github.francoiscampbell.xposeddatausage.model.net

/**
 * Created by francois on 16-03-16.
 */
interface NetworkManager {
    val currentNetworkType: NetworkType
    fun setConnectivityChangeCallback(callback: () -> Unit)

    enum class NetworkType {
        NONE,
        MOBILE,
        WIFI;

        companion object {
            fun valueOf(value: String?, default: NetworkType): NetworkType {
                if (value == null) return default
                try {
                    return NetworkType.valueOf(value)
                } catch (e: IllegalArgumentException) {
                    return default
                }
            }
        }
    }
}