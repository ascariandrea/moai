package com.ascariandrea.moai.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.ascariandrea.moai.utils.BlurBitmapHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by andreaascari on 22/01/15.
 */
public class BlurredImageView extends PoweredImageView {
    private Drawable mDefaultDrawable;

    public BlurredImageView(Context context) {
        super(context);
    }

    public BlurredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurredImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        super.onBitmapLoaded(BlurBitmapHelper.fastblur(getContext(), bitmap, 20), from);
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {
        Bitmap defaultBitmap;
        if (mDefaultDrawable != null) {
            if (mDefaultDrawable instanceof BitmapDrawable) {
                defaultBitmap = ((BitmapDrawable) mDefaultDrawable).getBitmap();
            } else {
                defaultBitmap = Bitmap.createBitmap(mDefaultDrawable.getIntrinsicWidth(), mDefaultDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(defaultBitmap);
                mDefaultDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                mDefaultDrawable.draw(canvas);
            }

            onBitmapLoaded(defaultBitmap, null);
        }
    }

    public void setDefaultDrawable(Drawable drawable) {
        mDefaultDrawable = drawable;
    }
}
