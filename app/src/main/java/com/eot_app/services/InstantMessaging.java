package com.eot_app.services;

/**
 * Created by Sona-11 on 21/9/21.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstantMessaging extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public static String refreshedToken;

    @Override
    public void onTokenRefresh() {

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        System.out.println(refreshedToken + "*****");

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
