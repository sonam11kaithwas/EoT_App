package com.eot_app.utility.settings.firstSync;

/**
 * Created by aplite_pc302 on 7/24/18.
 */

public interface FirstSyncView {
    void goHomePage();

    void errorMsg(String msg);

    void progressStatus(int status_no);

    void showRetryDialog(String title, String msg, String lable1, String lable2);

    void setUI(int i);

    void upateForcefully();

    void updateNotForcefully();

    void setSubscriptionExpire(String msg);

    void sessionExpiredFinishActivity();
}
