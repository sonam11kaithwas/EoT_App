package com.eot_app.locations;

/**
 * Created by Mahendra Dabi on 19/3/20.
 */
public interface OnLocationUpdate {
    void OnContinue(boolean isLocationUpdated, boolean isPermissionAllowed);
}
