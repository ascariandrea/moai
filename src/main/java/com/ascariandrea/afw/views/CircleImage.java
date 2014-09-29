package com.ascariandrea.afw.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.ascariandrea.afw.utils.Utils;

/**
 * Created by andreaascari on 27/01/14.
 */
public class CircleImage extends PoweredImageView {


    private String TAG = CircleImage.class.getSimpleName();

    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Utils.Views.drawCircle(this, canvas);
    }

}
