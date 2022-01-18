package com.eot_app.eoteditor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.EotApp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PicassoImageGetter implements Html.ImageGetter {

    private TextView textView = null;

    public PicassoImageGetter() {

    }

    public PicassoImageGetter(TextView target) {
        textView = target;
    }

    public static Spannable getSpannableHtmlWithImageGetter(TextView view, String value) {
        PicassoImageGetter imageGetter = new PicassoImageGetter(view);
        Spannable html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = (Spannable) Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            html = (Spannable) Html.fromHtml(value, imageGetter, null);
        }
        return html;
    }

    /*
    Used for setting click listener on the formulas loaded in textview / html. This is
    done using ImageSpan which detects the Image content inside the spannable.
    After that it sets a onClick listener using URLSpan. This is done for all the <img> inside
    the html.
     */
    public static void setClickListenerOnHtmlImageGetter(Spannable html, final Callback callback) {
        for (final ImageSpan span : html.getSpans(0, html.length(), ImageSpan.class)) {
            int flags = html.getSpanFlags(span);
            int start = html.getSpanStart(span);
            int end = html.getSpanEnd(span);

            html.setSpan(new URLSpan(span.getSource()) {
                @Override
                public void onClick(View v) {
                    callback.onImageClick(span.getSource());
                }
            }, start, end, flags);
        }
    }


    /*
    This method will create a spannable which is a displayable styled text. It
    also has a custom ImageGetter based on Picasso for loading <img> tags inside the html.
    We use this for rendering formulas in challenges
     */

    @Override
    public Drawable getDrawable(String source) {
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        Picasso.with(EotApp.getAppinstance())
                .load(source)
                .placeholder(R.drawable.app_logo2)
                .into(drawable);
        return drawable;
    }

    public interface Callback {
        void onImageClick(String imageUrl);
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            try {
                if (drawable != null) {
                    drawable.draw(canvas);
                }
            } catch (Exception exception) {
                EotApp.getAppinstance().showToastmsg(exception.getMessage());
                exception.printStackTrace();
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth() / 2;
            int height = drawable.getIntrinsicHeight() / 2;
            drawable.setBounds(0, 0, width, height);
            setBounds(0, 0, width, height);

            if (textView != null) {
                textView.invalidate();
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(EotApp.getAppinstance().getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    }
}