package com.ascariandrea.moai.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;


/**
 * Created by andreaascari on 27/01/14.
 */
public class PoweredImageView extends ImageView implements Target {

    public PoweredImageView(Context context) {
        super(context);
    }

    public PoweredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PoweredImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setImageUrl(String imageUrl) {
        setImageUrl(imageUrl, true, null);
    }


    public void setImageUrl(String imageUrl, boolean fromCache, Transformation transformation) {
        RequestCreator rc = Picasso.with(getContext()).load(imageUrl);

        if (!fromCache)
            rc.skipMemoryCache();

        if (transformation != null) {
            rc.transform(transformation);
        }

        rc.into((Target) this);
    }


    @SuppressWarnings("deprecation")
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setImageDrawable(new BitmapDrawable(bitmap));
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {

    }

    @Override
    public void onPrepareLoad(Drawable drawable) {

    }


}
