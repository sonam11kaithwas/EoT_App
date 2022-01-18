package com.eot_app.nav_menu.appointment;

public interface ServerResponse {
    //server request code for multiple calls
    int reqCode = 1;
    int reqCode2 = 2;
    int reqCode3 = 3;
    int reqCode4 = 4;

    void onSuccess(Object successObject, int requestCode);

    void onError(Object errorObject, int requestCode);
}
