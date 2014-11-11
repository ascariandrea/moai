package com.ascariandrea.moai.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ascariandrea.moai.R;

/**
 * Created by andreaascari on 19/05/14.
 */
public class SquareFrameLayout extends FrameLayout {
    private int mMeasureToUser;

    public SquareFrameLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SquareFrameLayout,
                0, 0);
        try {
            mMeasureToUser = a.getInt(R.styleable.SquareFrameLayout_use_measure, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int biggerSize;
        if (mMeasureToUser == 0)
            biggerSize = widthMeasureSpec;
        else
            biggerSize = heightMeasureSpec;
        super.onMeasure(biggerSize, biggerSize);
    }
}
