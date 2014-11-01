package com.ascariandrea.afw.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Container extends LinearLayout implements Target {
    public List<String> mCachedUrl = new ArrayList<String>();
    private float mContainerRatio;
    private float mBitmapRatio;
    private float mBitmapW;
    private float mBitmapH;
    private float mContainerH;
    private float mContainerW;


    public Container(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Container(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (getBackground() != null) setBackgroundDrawable(getBackground());
    }

    protected Bitmap crop(int w, int h, Bitmap bitmap) {
        if (w == 0 || h == 0) return bitmap;

        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);


        int bW = bitmap.getWidth();
        int bH = bitmap.getHeight();

        int scaledWidth = bW;
        int scaledHeight = bH;


        calculateContainerRatio(w, h);
        calculateBitmapRatio(bW, bH);
        if (isAPortraitContainer() || isASquaredContainer()) {
            if (isAPortraitBitmap() || isASquaredBitmap()) {
                // calculate the size with width ratio
                scaledWidth = (int) (bitmapContainerWidthRatio() * bW);
                scaledHeight = (int) (bitmapContainerWidthRatio() * bH);
            } else  {
                scaledWidth = (int) (bitmapContainerHeightRatio() * bW);
                scaledHeight = (int) (bitmapContainerHeightRatio() * bH);
            }
        } else {
            if (isAPortraitBitmap() || isASquaredBitmap()) {
                scaledWidth = (int) (bitmapContainerHeightRatio() * bW);
                scaledHeight = (int) (bitmapContainerHeightRatio() * bH);

            } else {
                scaledWidth = (int) (bitmapContainerWidthRatio() * bW);
                scaledHeight = (int) (bitmapContainerWidthRatio() * bH);

            }
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        int left = (w - scaledWidth) / 2;
        int top = (h - scaledHeight) / 2;


        c.drawBitmap(bitmap, left, top, null);
        return result;
    }

    protected static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap =
                Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private float bitmapContainerWidthRatio() {
        return (mContainerW / mBitmapW);
    }

    private float bitmapContainerHeightRatio() {
        return (mContainerH / mBitmapH);
    }


    private void calculateBitmapRatio(int bW, int bH) {
        mBitmapW = bW;
        mBitmapH = bH;
        mBitmapRatio = mBitmapW / mBitmapH;
    }

    private boolean isALandscapeBitmap() {
        return mBitmapRatio > 1;
    }

    private boolean isAPortraitBitmap() {
        return mBitmapRatio < 1;
    }

    private boolean isASquaredBitmap() {
        return (Math.abs(mBitmapW / mBitmapH) < 0.000001);
    }


    private void calculateContainerRatio(int width, int height) {
        mContainerW = width;
        mContainerH = height;
        mContainerRatio = mContainerW / mContainerH;
    }

    private boolean isALandscapeContainer() {
        return mContainerRatio > 1;
    }

    private boolean isAPortraitContainer() {
        return mContainerRatio < 1;
    }


    private boolean isASquaredContainer() {
        return mContainerRatio == 1;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getBackground() != null) setBackgroundDrawable(getBackground());
    }

    @SuppressLint("NewApi")
    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        setBackgroundDrawable(new BitmapDrawable(crop(w, h, bitmap)));
    }


    @Override
    public void onBitmapFailed(Drawable drawable) {

    }

    @Override
    public void onPrepareLoad(Drawable drawable) {

    }


    public void setBackgroundUrl(String url) {
        setBackgroundUrl(url, this);
    }

    public void setBackgroundUrl(String url, Target target) {
        Picasso
                .with(getContext())
                .load(url)
                .noFade()
                .into(target);

    }
}
