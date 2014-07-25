package com.seventydivision.framework.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by andreaascari on 25/07/14.
 */
public class PoweredViewPager extends ViewPager {

    private ArrayList<Integer> childScrollableIds = new ArrayList<Integer>();

    public PoweredViewPager(Context context) {
        super(context);
    }

    public PoweredViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (childScrollableIds.size() > 0) {
            for(int childId : childScrollableIds) {
                View scroll = findViewById(childId);
                if (scroll != null) {
                    Rect rect = new Rect();
                    scroll.getHitRect(rect);
                    if (rect.contains((int) event.getX(), (int) event.getY())) {
                        return false;
                    }
                }
            }

        }

        return super.onInterceptTouchEvent(event);

    }

    public void  addScrollableChildren(int childId) {
        childScrollableIds.add(childId);
    }


}
