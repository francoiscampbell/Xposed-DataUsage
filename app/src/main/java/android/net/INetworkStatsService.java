package android.net;

/**
 * Created by francois on 16-03-15.
 */
public interface INetworkStatsService {
    void forceUpdate();

    long getNetworkTotalBytes(NetworkTemplate template, long lastCycleBoundary, long nextCycleBoundary);
}
