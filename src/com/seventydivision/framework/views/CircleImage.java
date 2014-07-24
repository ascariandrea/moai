package com.seventydivision.framework.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;

import com.seventydivision.framework.utils.Utils;

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
