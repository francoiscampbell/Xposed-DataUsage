package android.net;

import android.content.Context;

import io.github.francoiscampbell.StubClassNotShadowedError;

/**
 * Created by francois on 16-03-15.
 */
public abstract class NetworkPolicyManager {
    //These implementations don't matter, the actual ones executed will be the system's
    public static NetworkPolicyManager from(Context context) {
        throw new StubClassNotShadowedError();
    }

    public static long computeLastCycleBoundary(long currentTime, NetworkPolicy policy) {
        throw new StubClassNotShadowedError();
    }

    public static long computeNextCycleBoundary(long currentTime, NetworkPolicy policy) {
        throw new StubClassNotShadowedError();
    }

    public NetworkPolicy[] getNetworkPolicies() {
        throw new StubClassNotShadowedError();
    }
}
