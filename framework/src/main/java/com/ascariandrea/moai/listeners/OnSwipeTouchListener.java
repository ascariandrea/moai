package com.ascariandrea.moai.listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by andreaascari on 29/01/14.
 */

public class OnSwipeTouchListener implements View.OnTouchListener {


    private final Context mContext;

    private final GestureDetector gestureDetector;
    private View mView;

    public OnSwipeTouchListener(Context context) {
        mContext = context;
        gestureDetector = new GestureDetector(mContext, new GestureListener());
    }


    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        mView = view;
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public void onClick(View view) {
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20;
        private static final int SWIPE_VELOCITY_THRESHOLD = 300;
        private final String TAG = GestureListener.class.getSimpleName();

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onClick(mView);
            return false;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                if (e1 != null && e2 != null) {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}
