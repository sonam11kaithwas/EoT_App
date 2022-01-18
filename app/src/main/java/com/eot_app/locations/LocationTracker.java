package com.eot_app.locations;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Mahendra Dabi on 19/3/20.
 */
public class LocationTracker {

    private final FusedLocationProviderClient mFusedLocationClient;
    Context mContext;
    OnLocationUpdate onLocationUpdate;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (onLocationUpdate != null)
                if (locationResult.getLastLocation() != null) {
                    //   Toast.makeText(mContext,"GOT location",Toast.LENGTH_SHORT).show();
                    LatLngSycn_Controller.getInstance().setLat(locationResult.getLastLocation().getLatitude() + "");
                    LatLngSycn_Controller.getInstance().setLng(locationResult.getLastLocation().getLongitude() + "");
                    onLocationUpdate.OnContinue(true, true);
                    if (mFusedLocationClient != null && locationResult != null)
                        mFusedLocationClient.removeLocationUpdates(locationCallback);

                } else onLocationUpdate.OnContinue(false, true);
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }
    };

    public LocationTracker(Context mContext, OnLocationUpdate onLocationUpdate) {
        this.mContext = mContext;
        this.onLocationUpdate = onLocationUpdate;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        getCurrentLocation();
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000 * 10);
        locationRequest.setNumUpdates(1);
        return locationRequest;

    }

    private void startUPdate() {
        LocationRequest locationRequest = createLocationRequest();
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    public void getCurrentLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                onLocationUpdate.OnContinue(false, false);
            } else {
                startUPdate();
           /* new GpsUtils(mContext).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    if (isGPSEnable) {
                        LocationRequest locationRequest = createLocationRequest();
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else onLocationUpdate.OnContinue(false, true, false);
                }
            });*/
            }
        } else {
            startUPdate();
        }
    }

}
