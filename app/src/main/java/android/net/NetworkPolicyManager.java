package android.net;

import android.content.Context;

/**
 * Created by francois on 16-03-15.
 */
public abstract class NetworkPolicyManager {

    //These implementations don't matter, the actual ones executed will be the system's
    public static NetworkPolicyManager from(Context context) {
        return null;
    }

    public static long computeLastCycleBoundary(long currentTime, NetworkPolicy policy) {
        return -1;
    }

    public static long computeNextCycleBoundary(long currentTime, NetworkPolicy policy) {
        return -1;
    }

    public abstract NetworkPolicy[] getNetworkPolicies();
}
