package com.eot_app.common_api_contr;

/**
 * Created by Sona-11 on 23/8/21.
 */
public interface ApiCalServerReqRes {
    //server request code for multiple calls
    int TimeSheetReqCode = 1;
    int ReportReqCode = 2;
    int LEAVEReqCode = 3;
    int USERLEAVEReqCode = 4;

    void onSuccess(Object successObject, int requestCode);

    void onError(Throwable errorObject, int requestCode);

}
