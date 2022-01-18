package com.eot_app.utility.settings.firstSync;

/**
 * Created by aplite_pc302 on 7/23/18.
 */

public interface FirstSyncPi {
    void startSync();

    void startSyncFromStatus();

    void OfflineDbSync();

    void retryCall();
}
