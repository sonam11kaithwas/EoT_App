package com.eot_app.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eot_app.login_next.Login2Activity;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.eot_app.nav_menu.jobs.job_detail.detail.NotifyForAttchCount;
import com.eot_app.nav_menu.jobs.job_detail.generate_invoice.InvoiceItemObserver;
import com.eot_app.nav_menu.jobs.joboffline_db.JobItem_Observer;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOverViewNotify;
import com.eot_app.services.ForegroundService2;
import com.eot_app.utility.util_interfaces.ApiCallbackObserver;
import com.eot_app.utility.util_interfaces.ApiContactSiteObserver;
import com.eot_app.utility.util_interfaces.NotificationObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by geet-pc on 18/5/18.
 */

public class EotApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static EotApp INSTANCE;
    List<Activity> activityList = new ArrayList<>();
    private NotificationObserver notiobserver;
    private ApiCallbackObserver apiobserver;
    private ApiContactSiteObserver con_site_observer;
    private JobItem_Observer jobItemObserver;
    private InvoiceItemObserver invoiceItemObserver;
    private JobOverViewNotify jobOverViewNotify;
    private NotifyForAttchCount notifyForAttchCount;
//    private DocDeleteNotfy docDeleteNotfy;

    public static synchronized EotApp getAppinstance() {
        return INSTANCE;
    }

    public void showToastmsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void sessionExpired() {
        if (EotApp.getAppinstance().isMyServiceRunning(ForegroundService2.class)) {
//        stop service on logout.
            Intent stopIntent = new Intent(this, ForegroundService2.class);
            stopIntent.setAction(ForegroundService2.STOPFOREGROUND_ACTION);
//            startService(stopIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(stopIntent);
            } else {
                startService(new Intent(stopIntent));
            }

        }

        /****clear token on session expire */
        App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();

        Intent intent = new Intent(this, Login2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

//        sign out from firebse
        ChatController.getInstance().signOutUserFromFirebase();

    }

    public void notifyObserver(String action, String key, String msg) {
//        notify observer
        if (this.notiobserver instanceof JobDetailActivity && action.equals("removeFW")) {
            notiobserver.onNotifyListner(key, msg);
        }
    }


    public void setObserver(NotificationObserver observer) {
        this.notiobserver = observer;
    }

    public void notifyApiObserver(String api_name) {
        if (this.apiobserver != null) {
            apiobserver.onObserveCallBack(api_name);
        }
    }

    public void setApiObserver(ApiCallbackObserver observer) {
        this.apiobserver = observer;
    }

    public void setJobFlagObserver(JobOverViewNotify jobOverViewNotify) {
        this.jobOverViewNotify = jobOverViewNotify;
    }

    public void getNotifyForAttchCount() {
        if (notifyForAttchCount != null) {
            notifyForAttchCount.updateCOuntAttchment();
        }
    }

    public void setNotifyForAttchCount(NotifyForAttchCount notifyForAttchCount) {
        this.notifyForAttchCount = notifyForAttchCount;
    }

    public void getJobFlagOverView() {
        if (this.jobOverViewNotify != null) {
            jobOverViewNotify.updateJobOverViewFlag();
        }
    }

    public void notifyCon_SiteObserver(String api_name) {
        if (this.con_site_observer != null) {
            con_site_observer.onObserveCallBack(api_name);
        }
    }

    public void setApiCon_SiteObserver(ApiContactSiteObserver observer) {
        this.con_site_observer = observer;
    }

    public void setApiItemAddEdit_Observer(JobItem_Observer itemAddEditObserver) {
        this.jobItemObserver = itemAddEditObserver;
    }

    public void notifyApiItemAddEdit_Observer(String api_name, String jobId) {
        if (this.jobItemObserver != null) {
            jobItemObserver.onObserveCallBack(api_name, jobId);
        }
    }

    public void setInvoiceItemObserver(InvoiceItemObserver invoiceItemObserver) {
        this.invoiceItemObserver = invoiceItemObserver;
    }

    public void notifyInvoiceItemObserver(String api_name) {
        if (this.invoiceItemObserver != null) {
            invoiceItemObserver.onObserveCallBack(api_name);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("TAG", "terminate app");
    }


    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void setLocalLanguage(String local_language) {
        LocaleHelper.setLocale(EotApp.getAppinstance(), local_language);
    }

    public String getString(String stringName) {
        int id = getResources().getIdentifier(stringName, "string", getPackageName());
        return getString(id);
    }

    public String[] getStringArray(String arrayName) {
        String packageName = INSTANCE.getPackageName();
        int id = getResources().getIdentifier(arrayName, "array", packageName);
        return getResources().getStringArray(id);
    }

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        try {
            activityList.add(activity);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("", "");

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("", "");
//        try {
//            ChatController.getInstance().setAppUserOnline(0);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            if (activityList != null) {
                activityList.remove(activity);
                if (activityList.isEmpty()) {
                    if (!TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isOnline", 0);
                        if (EotApp.getAppinstance().isMyServiceRunning(ForegroundService2.class)) {
                            ForegroundService2.isAppInBackground = true;
                            hashMap.put(ForegroundService2.F_ISBACKGROUND_FIELD, 1);
                        } else {
                            hashMap.put(ForegroundService2.F_ISBACKGROUND_FIELD, 0);
                            hashMap.put(ForegroundService2.F_STATUS_FIELD, 9);
                        }
                        // RealTimeDBController.setStatus(hashMap);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
