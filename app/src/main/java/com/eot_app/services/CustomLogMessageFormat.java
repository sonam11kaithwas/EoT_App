package com.eot_app.services;

import android.content.Context;

import com.hypertrack.hyperlog.LogFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mahendra Dabi on 27/1/20.
 */
public class CustomLogMessageFormat extends LogFormat {

    public CustomLogMessageFormat(Context context) {
        super(context);
    }

    @Override
    public String getFormattedLogMessage(String logLevelName, String tag, String message, String timeStamp, String senderName, String osVersion, String deviceUUID) {
        return getCurrentTimeStamp() + " : " + message;
    }


    String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS a");
        Date now = new Date();
        return sdfDate.format(now);
    }
}
