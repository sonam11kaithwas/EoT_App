package com.eot_app.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.eot_app.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by aplite_pc302 on 9/27/18.
 */

public class NotificationUtils extends ContextWrapper {
    private static final String TAG = "NotificationUtils";
    private final String ANDROID_CHANNEL_ID = "eot_app";
    private final String ANDROID_CHANNEL_NAME = "eot";
    private final Context mContext;
    private Bitmap bitmap = null;
    private NotificationManager mManager;

    public NotificationUtils(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        createChannels();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * crtaete notification channel
     ***/
    private void createChannels() {
        // create android channel
        NotificationChannel androidChannel = null;
        if (Build.VERSION.SDK_INT >= 26) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME
                    //mContext  .getString(R.string.app_name)
                    , NotificationManager.IMPORTANCE_HIGH);

            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
//            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(androidChannel);

        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name"
                //mContext  .getString(R.string.app_name)
                , NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification.Builder getAndroidChannelNotification(Uri alarmSound, int icon, int badge, String timeStamp, String title, String message, PendingIntent resultPendingIntent) {
        if (Build.VERSION.SDK_INT >= 26) {

            return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(EotApp.getAppinstance().getResources(),
                            R.drawable.notificationicon2))
//                    .setColor(getResources().getColor(R.color.body_font_color))
                    .setAutoCancel(true)
                    .setStyle(
                            new Notification.BigTextStyle()
                                    .bigText(message)
                                    .setBigContentTitle(title))
                    .setContentIntent(resultPendingIntent)
                    .setNumber(badge)


                    .setWhen(500);


        } else {
            return null;

        }
    }

    public Notification getNotificationManager(int icon, String title, String msgbody, PendingIntent pendingIntent) {
        Notification notification = new Notification.Builder(mContext)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(EotApp.getAppinstance().getResources(),
                        R.drawable.notificationicon2))
                .setContentTitle(title)
                .setContentText(msgbody)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new Notification.BigTextStyle().bigText(msgbody))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();
        return notification;

    }

    public void getBckprocees(final String imageUri) {
        // Creating the object of the
        // Executor service interface
        ExecutorService executorService
                = Executors.newFixedThreadPool(1);

        // submit() method can return the
        // result of the computation
        // because it has a return type of Future.

        // By using submit(), we are
        // accepting a Callable task
        Future obj
                = executorService.submit(new Callable() {

            // Overriding the call method
            public Object call() {
                try {

                    HttpGet httpRequest = null;
                    URL url = null;
                    try {
                        url = new URL(imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        httpRequest = new HttpGet(url.toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    final HttpGet finalHttpRequest = httpRequest;


                    HttpResponse response = null;
                    HttpClient httpclient = new DefaultHttpClient();

                    try {
                        response = httpclient.execute(finalHttpRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final HttpResponse finalResponse = response;


                    final BufferedHttpEntity[] bufHttpEntity = {null};


                    HttpEntity entity = finalResponse.getEntity();
                    try {
                        bufHttpEntity[0] = new BufferedHttpEntity(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InputStream instream = null;
                    try {
                        instream = bufHttpEntity[0].getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap = BitmapFactory.decodeStream(instream);


                    System.out.println();
//                    notification = new NotificationCompat.Builder(mContext, ANDROID_CHANNEL_ID)
//                            .setSmallIcon(R.drawable.notification_icon)
//                            .setContentTitle(userName_title)
//                            .setContentText("Photo")
//                            .setLargeIcon(BitmapFactory.decodeResource(EotApp.getAppinstance().getResources(), R.drawable.notificationicon2))
//                            .setStyle(new NotificationCompat.BigPictureStyle()
//                                    .bigPicture(bitmap))
//                            .setContentIntent(pendingIntent)
//                            .setAutoCancel(true)
//                            .build();


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return bitmap;
            }
        });
        try {
            System.out.println(obj.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public Notification getNotificationImageExpand(final String userName_title, final String imageUri, final PendingIntent pendingIntent) {
        getBckprocees(imageUri);

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_gallery);
        }

        Notification notification = new NotificationCompat.Builder(mContext, ANDROID_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(userName_title)
                .setContentText("Photo")
                .setLargeIcon(BitmapFactory.decodeResource(EotApp.getAppinstance().getResources(), R.drawable.notificationicon2))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        return notification;
    }


    synchronized public void getBitmapfromImageUrl(final String image_Url) {
        //    bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_logo2);
/*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(image_Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return bitmap;*/
    }

//https://www.programcreek.com/java-api-examples/?class=android.graphics.BitmapFactory&method=decodeStream

}