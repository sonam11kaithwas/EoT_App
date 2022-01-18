package com.eot_app.utility;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.eot_app.utility.util_interfaces.OnImageCompressed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mahendra Dabi on 13/3/21.
 */
public class CompressImageInBack {
    private final Uri uriAsyn;
    private final Context context;
    OnImageCompressed onImageCompressed;
    private boolean saveBitmap;
    private String savedImagePath;

    public CompressImageInBack(Context context, OnImageCompressed onImageCompressed, Uri uriAsyn) {
        this.context = context;
        this.onImageCompressed = onImageCompressed;
        this.uriAsyn = uriAsyn;
    }

    public String getSavedImagePath() {
        return savedImagePath;
    }

    public void setSaveBitmap(boolean saveBitmap) {
        this.saveBitmap = saveBitmap;
    }

    public void compressImageInBckg() {
        ExecutorService service = Executors.newSingleThreadExecutor();


        service.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap imageBitmapFromURI = null;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AppUtility.progressBarShow(context);

                    }
                });


                imageBitmapFromURI = getImageBitmapFromURI(uriAsyn);
                if (saveBitmap && imageBitmapFromURI != null) {

                    saveImageToCache(imageBitmapFromURI);
                }


                final Bitmap finalImageBitmapFromURI = imageBitmapFromURI;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (!saveBitmap)
                            AppUtility.progressBarDissMiss();
                        if (onImageCompressed != null)
                            onImageCompressed.onImageCompressed(finalImageBitmapFromURI
                            );
                    }
                });

            }
        });

    }

    private Bitmap getImageBitmapFromURI(Uri imagePath) {
        ContentResolver resolver = context.getContentResolver();
        InputStream is;
        try {
            is = resolver.openInputStream(imagePath);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "Image not found.", e);
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);

        // scale the image
        float maxSideLength = 1000;
        float scaleFactor = Math.min(maxSideLength / opts.outWidth, maxSideLength / opts.outHeight);
        // do not upscale!
        if (scaleFactor < 1) {
            opts.inDensity = 10000;
            opts.inTargetDensity = (int) ((float) opts.inDensity * scaleFactor);
        }
        opts.inJustDecodeBounds = false;

        try {
            is.close();
        } catch (IOException e) {
            // ignore
        }
        try {
            is = resolver.openInputStream(imagePath);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "Image not found.", e);
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
        try {
            is.close();
        } catch (IOException e) {
            // ignore
        }

        return bitmap;

    }

    private void saveImageToCache(Bitmap bitmap) {
        String mTimeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

        String mImageName = "eot_" + mTimeStamp + ".jpg";

        ContextWrapper wrapper = new ContextWrapper(context);

        File file = wrapper.getDir("Images", MODE_PRIVATE);

        file = new File(file, mImageName);

        try {

            OutputStream stream = null;

            stream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            stream.flush();

            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri mImageUri = Uri.parse(file.getAbsolutePath());
        savedImagePath = mImageUri.getPath();


    }

}

