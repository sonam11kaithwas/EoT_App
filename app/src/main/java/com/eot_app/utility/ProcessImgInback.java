package com.eot_app.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sona-11 on 12/01/22.
 */
public class ProcessImgInback {
    private Bitmap bitmap;

    public ProcessImgInback() {
    }

    public void getBitMap(String image_Url) {

        HttpGet httpRequest = null;
        URL url = null;
        try {
            url = new URL(image_Url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            httpRequest = new HttpGet(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }


        final ExecutorService service = Executors.newSingleThreadExecutor();
        final HttpGet finalHttpRequest = httpRequest;

        synchronized (this) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    HttpResponse response = null;
                    HttpClient httpclient = new DefaultHttpClient();

                    try {
                        response = httpclient.execute(finalHttpRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final HttpResponse finalResponse = response;


                    final BufferedHttpEntity[] bufHttpEntity = {null};

                    service.execute(new Runnable() {
                        @Override
                        public void run() {
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
                        }
                    });
                }
            });

        }
    }

    public Bitmap getBit() {
        return bitmap;
    }
}
