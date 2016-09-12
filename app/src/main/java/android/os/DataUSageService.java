package android.os;

import javax.inject.Inject;

import io.github.francoiscampbell.xposeddatausage.model.usage.DataUsageFetcher;

/**
 * Created by francois on 2016-09-11.
 */

public class DataUsageService extends IDataUsageService.Stub {
    private DataUsageFetcher dataUsageFetcher;

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return super.onTransact(code, data, reply, flags);
    }

    @Inject
    public DataUsageService(DataUsageFetcher dataUsageFetcher) {
        this.dataUsageFetcher = dataUsageFetcher;
    }

    @Override
    public Bundle getDataUsage(String networkType) throws RemoteException {
//        return dataUsageFetcher.getCurrentCycleBytes(
//                NetworkManager.NetworkType.Companion.valueOf(
//                        networkType, NetworkManager.NetworkType.MOBILE
//                )
//        ).bundle();
        return new DataUsageFetcher.DataUsage(123, 150, 200, 0.5f).bundle();
    }
}
