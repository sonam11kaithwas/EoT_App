package com.eot_app.utility;

/**
 * Created by Sona-11 on 26/11/21.
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * You have to make a clone of the file DigitalClock.java to use in your application, modify in the following manner:-
 * private final static String m12 = "h:mm aa";
 * private final static String m24 = "k:mm";
 */

public class MyDigitalClock {
    private static MyDigitalClock myDigitalClock;
    int tempmSec = 0, temphrs = 0, tempminutes = 0;
    private Timer timer;
    private TimerTask timerTask;
    private double myTimerTime = Double.parseDouble(App_preference.getSharedprefInstance().getShiftClockTime());
    private NotifyClock notifyClock;

    public static MyDigitalClock getInstance() {
        if (myDigitalClock == null) {
            myDigitalClock = new MyDigitalClock();
        }
        return myDigitalClock;
    }

    public void createTimerInstance() {
        if (timer == null)
            timer = new Timer();
        calculateTimeDurationBck();
    }


    public void setNotifyClock(NotifyClock notifyClock) {
        this.notifyClock = notifyClock;
    }

    public void startTimerCounting() {
        if (!App_preference.getSharedprefInstance().getCheckInTime().equals("")) {
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            myTimerTime++;
                            String str = getTimerTime();
                            Log.e("myTimerTime-----", "" + str);
                            if (notifyClock != null) {
                                notifyClock.setClockTime(str);
                            }
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        }
    }


    private String getTimerTime() {
        int round = (int) Math.round(myTimerTime);
        int second = (((round % 86000) % 3600) % 60) + tempmSec;
        int minutes = (((round % 86000) % 3600) / 60) + tempminutes;
        int hours = (((round % 86000) / 3600)) + temphrs;
        App_preference.getSharedprefInstance().setShiftClockime(String.valueOf(myTimerTime));
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", second);
    }


    public void stopTimerCounting() {
        if (timerTask != null) {
            timerTask.cancel();
            myTimerTime = 0.0;
            temphrs = tempminutes = tempmSec = 0;
            App_preference.getSharedprefInstance().setShiftClockime(String.valueOf(myTimerTime));
        }

    }

    public void setMyTimerTime() {
        App_preference.getSharedprefInstance().setShiftClockime(String.valueOf(myTimerTime));
        if (timerTask != null)
            timerTask.cancel();
    }

    public void calculateTimeDurationBck() {
        try {
            String time1 = App_preference.getSharedprefInstance().getCheckInTime();
            String time2 = AppUtility.getCurrentDateByFormat("hh:mm a");

            if (!time1.isEmpty() && !time2.isEmpty()) {
                SimpleDateFormat simpleDateFormat
                        = new SimpleDateFormat("hh:mm a");
                Date date1 = null, date2 = null;
                try {
                    date1 = simpleDateFormat.parse(time1);
                    date2 = simpleDateFormat.parse(time2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Calculating the difference in milliseconds
                long differenceInMilliSeconds
                        = Math.abs(date2.getTime() - date1.getTime());

                Log.e("TimerTask-", String.valueOf(myTimerTime));

                // Calculating the difference in Hours
                long differenceInHours
                        = (differenceInMilliSeconds / (60 * 60 * 1000))
                        % 24;

                temphrs = (int) differenceInHours;

                // Calculating the difference in Minutes
                long differenceInMinutes
                        = (differenceInMilliSeconds / (60 * 1000)) % 60;

                tempminutes = (int) differenceInMinutes;

                long differenceInSeconds
                        = (differenceInMilliSeconds / 1000) % 60;
                tempmSec = (int) differenceInSeconds;

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public interface NotifyClock {
        void setClockTime(String str);
    }


}
