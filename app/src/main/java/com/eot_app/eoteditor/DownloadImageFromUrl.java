package com.eot_app.eoteditor;

import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;

public class DownloadImageFromUrl extends AsyncTask<String, Void, String> {
    private final String inputText;
    OnImageLoaded onImageLoaded;
    private LevelListDrawable mDrawable;

    public DownloadImageFromUrl(String text, OnImageLoaded onImageLoaded) {
        this.onImageLoaded = onImageLoaded;
        this.inputText = text;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    public interface OnImageLoaded {
        void imagedLoadedRefreshText(String text);
    }
}
