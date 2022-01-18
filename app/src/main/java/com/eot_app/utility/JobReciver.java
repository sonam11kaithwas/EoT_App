package com.eot_app.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by aplite_pc302 on 7/19/18.
 */

public class JobReciver extends BroadcastReceiver {
    InternetConectionListner_JOB joblistner;

    @Override
    public void onReceive(Context context, Intent intent) {
        joblistner = (InternetConectionListner_JOB) context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = cm.getActiveNetworkInfo();
        boolean isconnected = active != null && active.isConnectedOrConnecting();
        if (joblistner != null) {
            joblistner.onNetworkConnectionChanged(isconnected);
        }
    }

    public interface InternetConectionListner_JOB {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
