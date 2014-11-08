package com.ascariandrea.moai.tabbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ascariandrea.moai.R;

/**
 * Created by andreaascari on 08/11/14.
 */
public class TabView extends LinearLayout {

    private int mMaxTabWidth;
    private int mIndex;

    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setMaxTabWith(int maxTabWith) {
        mMaxTabWidth = maxTabWith;
    }

    public void setIndex(int index) {
        mIndex = index;
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Re-measure if we went beyond our maximum size.
        if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(mMaxTabWidth, View.MeasureSpec.EXACTLY),
                    heightMeasureSpec);
        }
    }

    public int getIndex() {
        return mIndex;
    }


}
