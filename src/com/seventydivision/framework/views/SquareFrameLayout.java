package com.seventydivision.framework.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by andreaascari on 19/05/14.
 */
public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int biggerSize;
        if (widthMeasureSpec >= heightMeasureSpec)
            biggerSize = widthMeasureSpec;
        else
            biggerSize = heightMeasureSpec;
        super.onMeasure(biggerSize, biggerSize);
    }
}
