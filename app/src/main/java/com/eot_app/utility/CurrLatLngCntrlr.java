package com.eot_app.utility;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.hypertrack.hyperlog.HyperLog;

public class CurrLatLngCntrlr {
    private static CurrLatLngCntrlr currLatLngCntrlr;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    private double latitude = 0.0, longitude = 0.0;
    private GetLatLng getLatLng;

    public static CurrLatLngCntrlr getInstance() {
        if (currLatLngCntrlr == null) {
            currLatLngCntrlr = new CurrLatLngCntrlr();
        }
        return currLatLngCntrlr;
    }

    public void getCurrLatLng(final GetLatLng getLatLng) {
        this.getLatLng = getLatLng;
        HyperLog.i("CurrLatLngCntrlr", "getCurrLatLng()M");

        //get current location update callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                HyperLog.i("CurrLatLngCntrlr", "LocationCallback()M");

                if (locationResult == null) {
                    HyperLog.i("CurrLatLngCntrlr", "Location Null");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        HyperLog.i("CurrLatLngCntrlr", "get Location ");
                        HyperLog.i("CurrLatLngCntrlr", "" + String.format("Latituted=" + latitude + "\n Longituted=" + longitude));
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if (getLatLng != null) {
                            HyperLog.i("CurrLatLngCntrlr", "getLatLngs()M called");
                            getLatLng.getLatLngs(String.valueOf(latitude), String.valueOf(longitude));
                        }
                        onStopLocationUpdate();

                    }
                }

            }
        };

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(EotApp.getAppinstance());

        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER).setInterval(2000);
        if (ActivityCompat.checkSelfPermission(EotApp.getAppinstance(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EotApp.getAppinstance(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);

        HyperLog.i("CurrLatLngCntrlr", "getCurrLatLng()S");
    }

    private void onStopLocationUpdate() {
        HyperLog.i("CurrLatLngCntrlr", "onStopLocationUpdate()S");
        if (mFusedLocationClient != null && mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public interface GetLatLng {
        void getLatLngs(String lat, String lng);
    }

}
