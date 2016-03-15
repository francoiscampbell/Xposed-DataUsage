package android.net;

import android.content.Context;

/**
 * Created by francois on 16-03-15.
 */
public abstract class NetworkPolicyManager {
    public static NetworkPolicyManager from(Context context) {
        return null;
    }

    public static long computeLastCycleBoundary(NetworkPolicy policy) {
        return -1;
    }

    public static long computeNextCycleBoundary(NetworkPolicy policy) {
        return -1;
    }

    public abstract NetworkPolicy[] getNetworkPolicies();
}
