package com.eot_app.firebases;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.eot_app.services.ForegroundService2;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RealTimeDBController {
    /**
     * gpsStatus value with message
     * ONLINE:
     * 1. No movement.
     * 2. Walking
     * 3. Travelling
     * 4. GPS Turned OFF
     * 5. GPS Permission denied from Device.
     * 6. Admin has disabled tracking
     * 7. Time out
     * <p>
     * Offline:
     * 8. App killed / Internet turned off /No coverage
     * 9. User logged out from the app.
     */

    //firebase status field name
    public static final String F_STATUS_FIELD = "gpsStatus";
    public static final String F_ISONLINE_FIELD = "isOnline";
    static SimpleDateFormat serverFormat = new SimpleDateFormat("hh:mm:ss a");
    private static String path = null;

    public static String getPath() {
        return path;
    }

    public static final void initFirebase(String path) {
        if (RealTimeDBController.path == null) {
            RealTimeDBController.path = path;
            checkCurrentUserStatus();
        }
    }

    public static final void setStatus(final HashMap hashMap) {
        try {
            if (hashMap == null || hashMap.size() == 0) return;
            FirebaseDatabase.getInstance()
                    .getReference().child(path).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("ForegroundService2", "Node Updated:" + hashMap.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ForegroundService2", "Update Fail:" + hashMap.toString());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setOnDisconnectFirebase() {
        HashMap<String, Object> hashMap = new HashMap<>();

        /****set offline status when use disconnect from internet/Appkilled/network coverage****/
        hashMap.put(F_ISONLINE_FIELD, 0);
        hashMap.put(ForegroundService2.F_ISBACKGROUND_FIELD, 0);
        hashMap.put(F_STATUS_FIELD, 8);
        // Adding on disconnect hook
        FirebaseDatabase.getInstance().getReference(path)
                .onDisconnect()// Set up the disconnect hook
                .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ForegroundService", "Disconnect hook added");
            }
        });
    }

    public static void setGPSStatus(String field, int status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(field, status);
        setStatus(hashMap);
    }

    public static void setGPSStatusWithOnline(String field, int status, boolean isOnline) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (isOnline)
            hashMap.put(F_ISONLINE_FIELD, 1);
        else hashMap.put(F_ISONLINE_FIELD, 0);

        hashMap.put(field, status);

        setStatus(hashMap);
    }

    /**
     * Non tracking user goes offline after some time
     * we need to forcefully set online when user come back on application or using it
     */
    public static void checkCurrentUserStatus() {
        try {
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        //  HyperLog.i("firebase", "connected");
                        Log.e("initializeApp", "");
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isOnline", 1);
                        int mapStatus = checkUserMapStatus();
                        hashMap.put("gpsStatus", mapStatus);
                        RealTimeDBController.setStatus(hashMap);
                        hashMap.clear();
                        RealTimeDBController.setOnDisconnectFirebase();
                        Log.d("MainActivity", " setOnDisconnectFirebase");
                    } else {
                        Log.e("initializeApp", "");
                        //  HyperLog.i("firebase", "not connected");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                    System.err.println("Listener was cancelled");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int checkUserMapStatus() {
        int status = 1;
        try {
            LocationManager locationManager = (LocationManager) EotApp.getAppinstance().getSystemService(Context.LOCATION_SERVICE);
            if (!App_preference.getSharedprefInstance().getLoginRes().getIsFWgpsEnable().equals("1")) {
                status = 6;
                return status;
            } else if (ActivityCompat.checkSelfPermission(EotApp.getAppinstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(EotApp.getAppinstance(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                status = 5;
                return status;
            } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                status = 4;
                return status;
            } else if (calculateRemaingTime(serverFormat.format(Calendar.getInstance().getTime())) == 0) {
                status = 7;
                return status;
            } else return status;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;


    }

    public static long calculateRemaingTime(String currenttime) {
        long expirationDuration = 0;
        SimpleDateFormat serverFormat = new SimpleDateFormat("hh:mm:ss a");

        try {

            String startTime = App_preference.getSharedprefInstance().getLoginRes().getTrkStartingHour();
            String endTime = App_preference.getSharedprefInstance().getLoginRes().getTrkEndingHour();

            String currentTime = currenttime;

            Date startDate = serverFormat.parse(startTime);

            startDate.setTime(startDate.getTime() - 1000);

            Date endDate = serverFormat.parse(endTime);
            Date currentDate = serverFormat.parse(currentTime);

            if (startDate.before(currentDate) && endDate.after(currentDate)) {
                long secondEnd = endDate.getTime();
                long secondCurrent = currentDate.getTime();

                long l = (secondEnd - secondCurrent);
                if (l < 0)
                    l = (secondCurrent - secondEnd);
                expirationDuration = l;
                // Log.d(LOG_TAG, "Remaining Time: " + l);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return expirationDuration;
    }

}
