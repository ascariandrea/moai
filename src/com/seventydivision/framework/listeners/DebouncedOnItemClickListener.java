package com.seventydivision.framework.listeners;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by andreaascari on 31/07/14.
 */
public abstract class DebouncedOnItemClickListener implements AdapterView.OnItemClickListener {

    private final long minimumInterval;
    private Map<View, Long> lastClickMap;

    /**
     * Implement this in your subclass instead of onClick
     *
     * @param parent
     * @param v        The view that was clicked
     * @param position
     * @param id
     */
    public abstract void onDebouncedItemClick(AdapterView<?> parent, View v, int position, long id);


    public DebouncedOnItemClickListener() {
        this.minimumInterval = 1000;
        this.lastClickMap = new WeakHashMap<View, Long>();
    }

    public DebouncedOnItemClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<View, Long>();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
        Long previousClickTimestamp = lastClickMap.get(clickedView);
        long currentTimestamp = SystemClock.uptimeMillis();

        lastClickMap.put(clickedView, currentTimestamp);
        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp > minimumInterval)) {
            onDebouncedItemClick(parent, clickedView, position, id);
        }
    }
}
