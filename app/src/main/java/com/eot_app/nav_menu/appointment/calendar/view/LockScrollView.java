package com.eot_app.nav_menu.appointment.calendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockScrollView extends ScrollView {
    OnSwipeTouchListener onSwipeTouchListener;

    public LockScrollView(Context context) {
        super(context);
    }

    public LockScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSwipeTouchListener(OnSwipeTouchListener onSwipeTouchListener) {
        this.onSwipeTouchListener = onSwipeTouchListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (onSwipeTouchListener == null) return false;
        else
            return onSwipeTouchListener.onTouch(this, ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }
}
