package android.net;

/**
 * Created by francois on 16-03-15.
 */
public abstract class INetworkStatsService {
    public abstract void forceUpdate();

    public abstract long getNetworkTotalBytes(NetworkTemplate template, long lastCycleBoundary, long nextCycleBoundary);
}
