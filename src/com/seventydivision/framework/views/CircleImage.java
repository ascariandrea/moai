package com.seventydivision.framework.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.facebook.android.Util;
import com.seventydivision.framework.utils.Utils;

/**
 * Created by andreaascari on 27/01/14.
 */
public class CircleImage extends PowerImageView {


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
