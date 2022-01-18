package com.eot_app.nav_menu.appointment.calendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnSwipeTouchListener implements View.OnTouchListener {
    private final int SWIPE_THRESHOLD = 100;
    private final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final Context ctx;
    private GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context ctx) {
        this.ctx = ctx;
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void init() {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    public abstract void onSwipeRight();

    public abstract void onSwipeLeft();

    public abstract void onSwipeTop();

    public abstract void onSwipeBottom();

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }
    }


}
