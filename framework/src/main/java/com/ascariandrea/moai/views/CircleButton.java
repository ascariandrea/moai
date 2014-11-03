package com.ascariandrea.moai.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.ascariandrea.moai.utils.Utils;

/**
 * Created by andreaascari on 18/07/14.
 */
public class CircleButton extends ImageButton {

    public CircleButton(Context context) {
        super(context);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Utils.Views.drawCircle(this, canvas);
    }
}
