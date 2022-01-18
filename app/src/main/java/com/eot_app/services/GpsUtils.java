package com.eot_app.services;

import android.content.Context;
import android.location.LocationManager;

import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;

public class GpsUtils {
    public static final int REQUEST_CHECK_SETTINGS_LOCATION = 1001;
    private final Context context;
    private final SettingsClient mSettingsClient;
    private final LocationSettingsRequest mLocationSettingsRequest;
    private final LocationManager locationManager;
    private final LocationRequest locationRequest;
    private final GoogleApiClient googleApiClient;
    private onGpsListener mlistner;

    public GpsUtils(Context context) {
        this.context = context;

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
    }

    // method for turn on GPS
    public void turnGPSOn(final onGpsListener mlistner) {
        this.mlistner = mlistner;
        //enable location setting popup
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        try {
            if (context instanceof MainActivity)
                pendingResult.setResultCallback((MainActivity) context);
            else if (context instanceof JobDetailActivity)
                pendingResult.setResultCallback((JobDetailActivity) context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (this.mlistner != null) {
                this.mlistner.gpsStatus(true);
            }
        }
    }

    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }
}