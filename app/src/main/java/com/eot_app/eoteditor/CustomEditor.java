package com.eot_app.eoteditor;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;

public class CustomEditor extends EotEditor {
    private NestedScrollView parentScrollView;
    private int scrollSize = 0;

    public CustomEditor(Context context) {
        super(context);
    }

    public CustomEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //   onChangeListener();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeListener() {
        this.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollSize = oldScrollY;
                parentScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        parentScrollView.onNestedScroll(parentScrollView, 0, 0, 0, scrollSize);

                    }
                });
            }
        });

        parentScrollView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                parentScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        parentScrollView.onNestedScroll(parentScrollView, 0, 0, 0, scrollSize);

                    }
                });

            }
        });

    }

    public void setParentScrollView(NestedScrollView parentScroll) {
        parentScrollView = parentScroll;
    }
}
