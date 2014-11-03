package com.ascariandrea.moai.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.ascariandrea.moai.utils.BlurBitmapHelper;

public class BlurredContainer extends Container {

    public BlurredContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurredContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("deprecation")
    protected Bitmap crop(int w, int h, Bitmap bitmap) {
        Bitmap b = super.crop(w, h, bitmap);
        return BlurBitmapHelper.fastblur(getContext(), b, 25);
    }


}
