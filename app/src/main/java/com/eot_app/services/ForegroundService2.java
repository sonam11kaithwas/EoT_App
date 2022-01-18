package com.eot_app.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.eot_app.BuildConfig;
import com.eot_app.R;
import com.eot_app.firebases.RealTimeDBController;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.lat_lng_sync_pck.AddFWLatLng_Model1;
import com.eot_app.lat_lng_sync_pck.GPSModel;
import com.eot_app.lat_lng_sync_pck.LatLngRequest;
import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.eot_app.recievers.BatteryStatusReceiver;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.db.OfflineDataController;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by aplite_pc302 on 3/15/19.
 */

public class ForegroundService2 extends Service {
    public static final String STARTGPS_ACTION = "com.eot_app.action.startgps";
    //firebase status field name
    public static final String F_STATUS_FIELD = "gpsStatus";
    public static final String F_ISONLINE_FIELD = "isOnline";
    public static final String F_ISBACKGROUND_FIELD = "isBackground";
    private static final int FOREGROUND_SERVICE = 101;
    public static String STARTFOREGROUND_ACTION = "com.eot_app.action.startforeground";
    public static String STOPFOREGROUND_ACTION = "com.eot_app.action.stopforeground";
    public static String STOPFOREGROUND_ADMINDENIED_ACTION = "com.eot_app.action.stopadmindenied";
    //  private Location previousLocation;
    public static boolean isAppInBackground;
    private final String LOG_TAG = "ForegroundService2";
    //user detection with transition enter and exit
    private final String TRANSITIONS_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";
    private final float INACCURACYLIMIT = 100;
    private final List<GPSModel> realGpsPoint = new ArrayList<>();
    private final List<Location> inAccurateGpsPoint = new ArrayList<>();
    Location newPrevious;
    //server time
    String startTime;
    String endTime;
    SimpleDateFormat serverFormat = new SimpleDateFormat("hh:mm:ss a");
    //timer to check the last index
    Timer timer;
    //Location array
    List<AddFWLatLng_Model1> offlineLocations = new ArrayList<>();
    String path;
    boolean isGpsEnabled;
    boolean isAdminDenied;
    int travelThreshold;
    /**
     * Add Location to offline DB for later sync
     * sending max up to 3 location on single request
     * API : addFWlatlong2
     */
    Gson gson = new Gson();
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TransitionsReceiver mTransitionsReceiver;
    private int INTERVAL = 40;
    private PendingIntent mActivityTransitionsPendingIntent;
    private boolean isTimerRunning;
    private boolean isStill;
    private boolean isParamSet;
    private boolean isExpiredSet;
    private BatteryStatusReceiver batteryStatusReceiver;
    private int detectedActivity = 1;
    /**
     * Broadcast receiver for Internt/wifi ON/OFF
     */

    BroadcastReceiver networkSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(F_STATUS_FIELD, detectedActivity);
                hashMap.put("isOnline", 1);
                setUserOffline(hashMap, true);
                hashMap.clear();
                Log.d(LOG_TAG, "Connected...");

            }


        }
    };
    /**
     * Broadcast receiver for GPS ON/OFF
     */
    BroadcastReceiver locationSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HashMap<String, Object>
                    hashMap = new HashMap<>();
            long remainingTime = calculateRemaingTime(serverFormat.format(Calendar.getInstance().getTime()));

            if (remainingTime == 0) {
                hashMap.put(ForegroundService2.F_STATUS_FIELD, 7);
                setUserOffline(hashMap, true);
            } else if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                //    HyperLog.i(LOG_TAG, "gps setting change: " + isGpsEnabled);


                if (isGpsEnabled) {
                    hashMap.put(F_STATUS_FIELD, detectedActivity);
                    Log.d(LOG_TAG, "GPS ON");
                    setUserOffline(hashMap, true);
                    //location is enabled
                } else {
                    hashMap.put(F_STATUS_FIELD, 4);
                    setUserOffline(hashMap, true);
                    Log.d(LOG_TAG, "GPS OFF");

                    //location is disabled
                }
            }
        }
    };

    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.IN_VEHICLE:
                return "VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "BICYCLE";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            default:
                return "UNKNOWN";

        }
    }

    private static String toTransitionType(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {

                /**********    set Initial Values   **********/
                initializeDistanceParameter();
                Notification notification = generateNotification(this, "");
                //            for start in back ground
                startForeground(FOREGROUND_SERVICE,
                        notification);

                // ******for location tracking :- ******
                fucedLocationEnableFunction();
                getLocation();

            } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {
                if (mFusedLocationClient != null) {
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                }
                stopForeground(true);
                stopSelf();
            } else if (intent.getAction().equals(STOPFOREGROUND_ADMINDENIED_ACTION)) {
                isAdminDenied = true;
                if (mFusedLocationClient != null) {
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                }
                stopForeground(true);
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    /**
     * request for starting location update
     */
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void initializeDistanceParameter() {
        try {

            path = App_preference.getSharedprefInstance().getRegion()
                    + "/cmp" +
                    App_preference.getSharedprefInstance().getLoginRes().getCompId().toLowerCase() + "/users/usr"
                    + App_preference.getSharedprefInstance().getLoginRes().getUsrId();

            RealTimeDBController.initFirebase(path);
            isGpsEnabled = ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            //  HyperLog.i(LOG_TAG, "isGpsEnabled: " + isGpsEnabled);
            try {
                String trkDuration = App_preference.getSharedprefInstance().getLoginRes().getTrkDuration();
                if (trkDuration != null)
                    INTERVAL = Integer.parseInt(trkDuration);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            startTime = App_preference.getSharedprefInstance().getLoginRes().getTrkStartingHour();
            endTime = App_preference.getSharedprefInstance().getLoginRes().getTrkEndingHour();

            //   getDeviceConfigurations();
            registerGPSReceiver();
            registerNetworkReceiver();
            registerBatteryReceiver();
            if (!isTimerRunning)
                addTimer();

//            HyperLog.i(LOG_TAG, "Start Time: " + startTime);
//            HyperLog.i(LOG_TAG, "End Time: " + endTime);
//            HyperLog.i(LOG_TAG, "Interval: " + INTERVAL + " Sec");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerBatteryReceiver() {
        if (batteryStatusReceiver == null) {
            batteryStatusReceiver
                    = new BatteryStatusReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
            this.registerReceiver(batteryStatusReceiver, intentFilter);
        }
    }

    private void fucedLocationEnableFunction() {

        registerUserActivityListener();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        int i = INTERVAL / 2;
        locationRequest.setInterval(1000 * i);
        locationRequest.setSmallestDisplacement(30);
        locationRequest.setMaxWaitTime(1000 * i * 3);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                HyperLog.i(LOG_TAG, "Received Loc: "
                        + locationResult.getLastLocation().getLatitude() + ","
                        + locationResult.getLastLocation().getLongitude() + " Accuracy:"
                        + locationResult.getLastLocation().getAccuracy() + " Speed:"
                        + locationResult.getLastLocation().getSpeed());

                LatLngSycn_Controller.getInstance().setLat(locationResult.getLastLocation().getLatitude() + "");
                LatLngSycn_Controller.getInstance().setLng(locationResult.getLastLocation().getLongitude() + "");


                long remainingTime = calculateRemaingTime(serverFormat.format(Calendar.getInstance().getTime()));

                if (remainingTime == 0) {
                    if (!isExpiredSet) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(F_STATUS_FIELD, 7);
                        setUserOffline(hashMap, true);
                        isExpiredSet = true;
                    }
                    HyperLog.i(LOG_TAG, "Time Expired");
                    return;
                }

                isExpiredSet = true;

                List<Location> locationslist = locationResult.getLocations();

                if (locationslist != null) {
                    for (Location l : locationslist) {
                        float speed = l.getSpeed() * 3.6f;
                        //    HyperLog.i(LOG_TAG, "Location: " + l.getLatitude() + "," + l.getLongitude() + " Accuracy:" + l.getAccuracy() + " Speed:" + speed + " km/hr");

                        checkSpeed(speed);

                        /**
                         * save first point to server
                         * */
                        if (newPrevious == null) {
                            bufferedLocation(l);
                            newPrevious = l;
                            continue;
                        }


                        /**
                         * check device is still or moving
                         * */
/*
                        if (!isStill || speed > 5) {
*/
                        //  HyperLog.i(LOG_TAG, "Point::::" + l.getLatitude() + "," + l.getLongitude() + " Speed:" + speed + " State:" + isStill);
                        /**
                         * check the location accuracy is greater than 100 meter
                         * we'll consider it as inaccurate point.
                         * */
                        if (l.getAccuracy() >= 100) {
                            if (l.getAccuracy() <= 1000) {
                                inAccurateGpsPoint.add(l);
                                // HyperLog.i(LOG_TAG, "Inaccurate GPS Point: " + l.getLatitude() + "," + l.getLongitude());
                                /**
                                 * compare the latest location to
                                 * previous up-to 5 location to avoid zi-zag pattern for
                                 * higher accuracy point (up-to Limit:1000 meter)
                                 *
                                 * */
                                if (inAccurateGpsPoint.size() >= 2) {
                                    filterDataInaccurate();
                                }
                            } else {
                                bufferedLocation(l);
                            }
                            continue;
                        }
                        inAccurateGpsPoint.clear();
                        float distance = newPrevious.distanceTo(l);
                        //   HyperLog.i(LOG_TAG, "Distance: " + distance);
                        if (distance > 30) {
                            bufferedLocation(l);
                            newPrevious = l;
                            //  HyperLog.i(LOG_TAG, "Added to Que");
                        }
                    }


                }

            }
            /*  }*/
        }

        ;


    }


    /**
     * update device movement on Firebase database
     */
    private void checkSpeed(float speed) {
        /**
         * safety check for still condition sometimes Activity recognition api doesn't work
         * when we enable update while we driving.
         */

        if (speed >= 10) {
            if (detectedActivity == 1 || detectedActivity == 2) {
                travelThreshold++;
                if (travelThreshold >= 2) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(F_STATUS_FIELD, 3);
                    setUserOffline(hashMap, true);
                    detectedActivity = 3;
                    travelThreshold = 0;
                    HyperLog.i(LOG_TAG, "Device Moment: Travelling");
                }
            }
        } else if (speed > 4 && speed < 10) {
            if (detectedActivity == 1 || detectedActivity == 3) {
                travelThreshold++;
                if (travelThreshold >= 2) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(F_STATUS_FIELD, 2);
                    setUserOffline(hashMap, true);
                    detectedActivity = 2;
                    travelThreshold = 0;
                    HyperLog.i(LOG_TAG, "Device Moment: walking");
                }
            }
        } else if (speed <= 4) {
            if (detectedActivity == 2 || detectedActivity == 3) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(F_STATUS_FIELD, 1);
                setUserOffline(hashMap, true);
                detectedActivity = 1;
                travelThreshold = 0;
                HyperLog.i(LOG_TAG, "Device Moment: still");
            }
        }

    }

    /**
     * update user Activity
     * */


    /**
     * Calculating the tracking duration of the FW
     * from given START and END time parameter from admin
     */
    public long calculateRemaingTime(String currenttime) {

        long expirationDuration = 0;
        try {
            startTime = App_preference.getSharedprefInstance().getLoginRes().getTrkStartingHour();
            endTime = App_preference.getSharedprefInstance().getLoginRes().getTrkEndingHour();

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

    /**
     * avoid the zig-zag patter
     * on same location with 100 meter
     * radius up to 5 previous location
     * comparision
     */
    private void filterDataInaccurate() {
        Location ref_point = inAccurateGpsPoint.get(inAccurateGpsPoint.size() - 1);

        for (int i = 0; i <= inAccurateGpsPoint.size() - 2; i++) {
            Location location = inAccurateGpsPoint.get(i);
            float distance = ref_point.distanceTo(location);
            //  HyperLog.i(LOG_TAG, "Distance: " + distance);
            if (distance > INACCURACYLIMIT) {
                bufferedLocation(ref_point);
                newPrevious = ref_point;
                //     HyperLog.i(LOG_TAG, "Added inaccurateLocation: " + ref_point.getLatitude() + "," + ref_point.getLongitude());

            }
        }

        if (inAccurateGpsPoint.size() == 5) {
            inAccurateGpsPoint.clear();
            Log.d(LOG_TAG, "Point cleared......");
        }
    }

    //filter the GPS points
    private void bufferedLocation(Location location) {
        try {
            GPSModel model = new GPSModel(location);
            realGpsPoint.add(model);
            if (realGpsPoint.size() >= 4) {
                Location A = realGpsPoint.get(0).getLocation();
                Location B = realGpsPoint.get(1).getLocation();
                Location C = realGpsPoint.get(2).getLocation();
                Location D = realGpsPoint.get(3).getLocation();

                float a_b = A.distanceTo(B);
                float a_c = A.distanceTo(C);
                float a_d = A.distanceTo(D);
                //   HyperLog.i(LOG_TAG, "A-B:" + a_b + " A-C:" + a_c + " A-D:" + a_d);

                if (a_b < a_c) {
                    if (a_c < a_d) {
                        AddFWLatLng_Model1 addFWLatLng_model = new AddFWLatLng_Model1(App_preference.getSharedprefInstance().getLoginRes().getUsrId(), realGpsPoint.get(1).getLocation(), realGpsPoint.get(1).getBtryStatus(), realGpsPoint.get(1).getDateTime());
                        //  Gson gson = new Gson();
                        //  String addFwlatlng = gson.toJson(addFWLatLng_model);
                        // OfflineDataController.getInstance().addInOfflineDB(Service_apis.addFWlatlong, addFwlatlng, realGpsPoint.get(1).getDateTime());
                        offlineLocations.add(addFWLatLng_model);
                        if (offlineLocations.size() > 2)
                            createOfflineLocDBRequest(offlineLocations);
                        realGpsPoint.remove(0);

                    } else {
                        // HyperLog.i(LOG_TAG, "Removed C :" + realGpsPoint.get(2).getLocation().toString());
                        realGpsPoint.remove(2);
                    }
                } else {
                    // HyperLog.i(LOG_TAG, "Removed B" + realGpsPoint.get(1).getLocation().toString());
                    realGpsPoint.remove(1);
                }

            } else if (realGpsPoint.size() == 1) {
                AddFWLatLng_Model1 addFWLatLng_model = new AddFWLatLng_Model1(App_preference.getSharedprefInstance().getLoginRes().getUsrId(), realGpsPoint.get(0).getLocation(), realGpsPoint.get(0).getBtryStatus(), realGpsPoint.get(0).getDateTime());
                // Gson gson = new Gson();
                //  String addFwlatlng = gson.toJson(addFWLatLng_model);
                //   OfflineDataController.getInstance().addInOfflineDB(Service_apis.addFWlatlong, addFwlatlng, realGpsPoint.get(0).getDateTime());
                offlineLocations.add(addFWLatLng_model);
                createOfflineLocDBRequest(offlineLocations);
                HyperLog.i(LOG_TAG, "First point saved");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Start Timer to check last stack point
     */
    private void addTimer() {
        try {
            //avoid to add the timer on first app launch
            //and init and wait for schedule it for next activity change
            timer = new Timer();

            /**
             * timer task to save last location when
             * user not moving up-to 10 min
             */
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    HyperLog.i(LOG_TAG, "Timer run(M) called");
                    addLastStackGPSPoint();
                }
            };
            timer.scheduleAtFixedRate(t, 0, 1000 * 60 * 10);
            t.run();
            isTimerRunning = true;
            HyperLog.i(LOG_TAG, "Timer Added success");
        } catch (Exception e) {
            isTimerRunning = false;
            HyperLog.i(LOG_TAG, "Add Timer Error:" + e.toString());
            e.printStackTrace();
        }


    }

    /**
     * create User Activity request with
     * desired movement you want to track
     * with transition state (ENTER and EXIT)
     */
    private List<ActivityTransition> createTransitionRequest() {
        List<ActivityTransition> activityTransitionList = new ArrayList<>();

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        return activityTransitionList;
    }

    /**
     * Enabling user Activity
     * with transition events defined in the transition request
     */
    private void registerUserActivityListener() {

        // registerTransitionReceiver();

        ActivityTransitionRequest request = new ActivityTransitionRequest(createTransitionRequest());

        Task<Void> task = ActivityRecognition.getClient(this)
                .requestActivityTransitionUpdates(request, mActivityTransitionsPendingIntent);

        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        //    HyperLog.i(LOG_TAG, "UserActivity: Enabled");

                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //  HyperLog.i(LOG_TAG, "UserActivity Enable Error: " + e.toString());

                    }
                }
        );
    }

    /**
     * Dynamically Register receiver for user activity callback on
     * registered broadcast receiver
     */
    private void registerTransitionReceiver() {
        Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION);
        mActivityTransitionsPendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, 0);

        if (mTransitionsReceiver == null) {
            mTransitionsReceiver = new TransitionsReceiver();
            registerReceiver(
                    mTransitionsReceiver,
                    new IntentFilter(TRANSITIONS_RECEIVER_ACTION)
            );
        }
    }

    /**
     * Unsubscribe the user activity on OnDestroy of service
     */
    private void removeUserActivity() {
        if (mActivityTransitionsPendingIntent != null) {
            Task<Void> task = ActivityRecognition.getClient(this)
                    .removeActivityTransitionUpdates(mActivityTransitionsPendingIntent);

            task.addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            mActivityTransitionsPendingIntent.cancel();
                            //  HyperLog.i(LOG_TAG, "Activity Removed.");
                        }
                    }
            );

            task.addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            //HyperLog.i(LOG_TAG, "Activity Remove Error: " + e.toString());

                        }
                    }
            );

        }

    }

    /**
     * cancel running timer when
     * fw is in moving state
     */
    private void cancelTimer() {
        try {
            if (timer != null && isTimerRunning) {
                timer.cancel();
                isTimerRunning = false;
                //  HyperLog.i(LOG_TAG, "Timer cancelled manually");
            }

        } catch (Exception ex) {
            isTimerRunning = false;
            //   HyperLog.i(LOG_TAG, "Cancel Timer Error:" + ex.toString());
            ex.printStackTrace();
        }
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        isAppInBackground = true;
        //stopSelf();
    }

    /**
     * show notification for  foreground service
     * as user online status and tracking is on for fw
     */
    private Notification generateNotification(Context context, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = null;

        final Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "Eot Channel";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);

            builder = new Notification.Builder(context, id)
                    .setContentTitle(App_preference.getSharedprefInstance().getLoginRes().getUsername())
                    .setTicker("Eye On Task")
                    .setContentText("You are sharing your location with Eyeontask.")
                    .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(largeIcon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);

        } else {
            try {
                builder = new Notification.Builder(context)
                        .setContentTitle(App_preference.getSharedprefInstance().getLoginRes().getUsername())
                        .setTicker("Eye On Task")
                        .setContentText("You are sharing your location with Eyeontask.")
                        .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                        .setLargeIcon(
                                Bitmap.createScaledBitmap(largeIcon, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        builder.setPriority(Notification.PRIORITY_LOW);
        Notification notification = builder.build();
        return notification;
    }

    /**
     * timer will check the last received location time with current time
     * if the last location time is 10 min ago we have to sent it to server
     * to complete the path.
     */
    public void addLastStackGPSPoint() {
        try {
            if (realGpsPoint != null) {
                if (realGpsPoint.size() > 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    GPSModel gpsModel = realGpsPoint.get(realGpsPoint.size() - 1);
                    String dateTime = gpsModel.getDateTime();
                    if (dateTime != null) {
                        Date lastLocationTime = sdf.parse(dateTime);
                        Date currentTime = new Date(System.currentTimeMillis());
                        long timeDiff = currentTime.getTime() - lastLocationTime.getTime();
                        long minute = (timeDiff / 1000) / 60;
                        HyperLog.i(LOG_TAG, "last Point time: " + minute);
                        if (minute >= 10) {
                            GPSModel lastSavedLocation = null;
                            for (int i = 1; i < realGpsPoint.size(); i++) {
                                GPSModel model = realGpsPoint.get(i);
                                float distance = gpsModel.getLocation().distanceTo(model.getLocation());
                                if (i == 1 && distance <= 100 || i == 2 && distance <= 200) {
                                    AddFWLatLng_Model1 addFWLatLng_model = new AddFWLatLng_Model1(App_preference.getSharedprefInstance().getLoginRes().getUsrId(), model.getLocation(), model.getBtryStatus(), model.getDateTime());
                                    //  Gson gson = new Gson();
                                    //  String addFwlatlng = gson.toJson(addFWLatLng_model);
                                    //  OfflineDataController.getInstance().addInOfflineDB(Service_apis.addFWlatlong, addFwlatlng, model.getDateTime());
                                    lastSavedLocation = model;
                                    offlineLocations.add(addFWLatLng_model);
                                    createOfflineLocDBRequest(offlineLocations);
                                    //   HyperLog.i(LOG_TAG, "Last Stack point: " + model.getLocation().getLatitude() + "," + model.getLocation().getLongitude());
                                } //else HyperLog.i(LOG_TAG, "Distance is Too high: " + distance);

                            }
                            realGpsPoint.clear();
                            if (lastSavedLocation != null)
                                realGpsPoint.add(lastSavedLocation);

                            //   cancelTimer();
                        }

                    }

                } /*else cancelTimer();*/


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOfflineLocDBRequest(List<AddFWLatLng_Model1> list) {
        try {
            /**
             * check the lat lng array should not be null or empty
             * */
            if (list != null && list.size() > 0) {
                LatLngRequest latLngRequest = new LatLngRequest(
                        App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                        list
                );
                String toJson = gson.toJson(latLngRequest);
                HyperLog.i(LOG_TAG, "Location saved on local DB:" + toJson);
                OfflineDataController.getInstance().addInOfflineDB(Service_apis.addFWlatlong, toJson, AppUtility.getDateByFormat("yyyy-MM-dd hh:mm:ss a"));
                //clear after save to offline
                offlineLocations.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * get logged in device configuration for
     * better log understanding and bug fix
     */
    private void getDeviceConfigurations() {
        StringBuilder builder = new StringBuilder();
        builder.append(App_preference.getSharedprefInstance().getLoginRes().getUsername() + " ID:"
                + App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        builder.append("\nCompany Id:" +
                App_preference.getSharedprefInstance().getLoginRes().getCompId());
        builder.append("\nDevice Name:" + Build.MANUFACTURER + " " + Build.BRAND + " " + Build.MODEL);
        builder.append("\nDevice Version:" + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName()
                + " " + Build.ID);
        builder.append("\nApp Version:" + BuildConfig.VERSION_NAME);

        Log.d(LOG_TAG, builder.toString());
    }

    @Override
    public void onDestroy() {
        Log.e("Ee", "ForegroundService2");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(F_STATUS_FIELD, 8);
        hashMap.put("isOnline", 0);
        hashMap.put(F_ISBACKGROUND_FIELD, 0);
        if (!isAdminDenied)
            setUserOffline(hashMap, false);

        // removeUserActivity();

        try {
            if (locationCallback != null && mFusedLocationClient != null)
                mFusedLocationClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mTransitionsReceiver != null)
                unregisterReceiver(mTransitionsReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (locationSwitchStateReceiver != null)
                unregisterReceiver(locationSwitchStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (networkSwitchStateReceiver != null)
                unregisterReceiver(networkSwitchStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (batteryStatusReceiver != null)
                unregisterReceiver(batteryStatusReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }


        cancelTimer();

        HyperLog.i(LOG_TAG, "Location service On Destroy:");
        super.onDestroy();
    }

    /**
     * register Method GPS receiver
     * to update GPS ON AND OFF status to server
     */
    private void registerGPSReceiver() {
        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(locationSwitchStateReceiver, filter);

    }

    /**
     * updating all the required param
     * to fire store database for admin
     * to show user status and gps conditions
     */

    private void setUserOffline(final HashMap hashMap, boolean isOnline) {

        /*if (isOnline)
            if (isAppInBackground) {
                hashMap.put(F_ISBACKGROUND_FIELD, 1);
                hashMap.put(F_ISONLINE_FIELD, 0);
            } else {
                hashMap.put(F_ISBACKGROUND_FIELD, 0);
                hashMap.put("isOnline", 1);
            }*/
        //   HyperLog.i(LOG_TAG, hashMap.toString());

        try {
            FirebaseDatabase.getInstance()
                    .getReference().child(path).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(LOG_TAG, "Node Update From service:" + hashMap.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(LOG_TAG, "Node update fail from service" + e.toString());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * register Method INTERNET receiver
     * to update INTERNET ON AND OFF status to server
     */
    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(networkSwitchStateReceiver, filter);
    }

    /**
     * Broadcast receiver to for transition activity
     * callback to set status of isStill so that we
     * can  check that device is still or moving
     */
    public class TransitionsReceiver extends BroadcastReceiver {
        public TransitionsReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (!TextUtils.equals(TRANSITIONS_RECEIVER_ACTION, intent.getAction())) {

                return;
            }

            // TODO: Extract activity transition information from listener.
            if (ActivityTransitionResult.hasResult(intent)) {

                ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);

                for (ActivityTransitionEvent event : result.getTransitionEvents()) {

                    if (toTransitionType(event.getTransitionType()).equals("ENTER")) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        //  HyperLog.i(LOG_TAG, toTransitionType(event.getTransitionType()) + " " + toActivityString(event.getActivityType()));
                        if (toActivityString(event.getActivityType()).equals("STILL") || toActivityString(event.getActivityType()).equals("UNKNOWN")) {
                            isStill = true;
                            //  detectedActivity = 1;
                            hashMap.put(F_STATUS_FIELD, 1);
                            if (!isTimerRunning)
                                addTimer();
                        } else {
                            cancelTimer();
                            if (toActivityString(event.getActivityType())
                                    .equals("WALKING")) {
                                hashMap.put(F_STATUS_FIELD, 2);
                                //   detectedActivity = 2;
                            } else {
                                hashMap.put(F_STATUS_FIELD, 3);
                                // detectedActivity = 3;
                            }
                            isStill = false;
                        }
                        //   setUserOffline(hashMap,true);
                    }

                }
            }
        }

    }
}


