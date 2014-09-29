package com.ascariandrea.afw.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by andreaascari on 09/07/14.
 */
public class UnScrollableExpandableListView extends ExpandableListView {
    public UnScrollableExpandableListView(Context context) {
        super(context);
    }

    public UnScrollableExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnScrollableExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
