// IDataUsageService.aidl
package android.os;

// Declare any non-default types here with import statements
import android.os.Bundle;

interface IDataUsageService {
    Bundle getDataUsage(String networkType);
}
