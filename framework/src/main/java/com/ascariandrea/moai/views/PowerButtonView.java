package com.ascariandrea.moai.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

/**
 * Created by andreaascari on 21/07/14.
 */
public class PowerButtonView extends ImageButton implements Target {
    public PowerButtonView(Context context) {
        super(context);
    }

    public PowerButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PowerButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setImageUrl(String imageUrl) {
        setImageUrl(imageUrl, true);
    }

    public void setImageUrl(String imageUrl, boolean fromCache) {
        RequestCreator rc = Picasso.with(getContext()).load(imageUrl);
        if (!fromCache)
            rc.skipMemoryCache();

        rc.into((Target) this);
    }


    @SuppressWarnings("deprecation")
    public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {
        setImageDrawable(new BitmapDrawable(arg0));
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {

    }

    @Override
    public void onPrepareLoad(Drawable drawable) {

    }

}
