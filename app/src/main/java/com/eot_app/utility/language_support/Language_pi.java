package com.eot_app.utility.language_support;

import java.util.concurrent.Callable;

public interface Language_pi {
    boolean isFileStoreInLocalStorage();

    void downloadFile(String file_url, Callable<Boolean> function);

    void checkFileInLocal();

    String getServerMsgByKey(String key);

    String getMobileMsgByKey(String key);


    //    using shared pref
    void initializeJsonObject2();

    void clearlanguageData();
}
