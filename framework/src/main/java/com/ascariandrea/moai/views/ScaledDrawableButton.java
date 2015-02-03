package com.ascariandrea.moai.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by andreaascari on 22/01/15.
 */
public class ScaledDrawableButton extends Button {
    public ScaledDrawableButton(Context context) {
        super(context);
        init();
    }

    public ScaledDrawableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaledDrawableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Drawable[] drawables = this.getCompoundDrawables();
        for (Drawable d : drawables) if (d != null && d instanceof ScaleDrawable) d.setLevel(1);
        this.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
