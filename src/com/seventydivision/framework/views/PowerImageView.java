package com.seventydivision.framework.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;


/**
 * Created by andreaascari on 27/01/14.
 */
public class PowerImageView extends ImageView implements Target {

    private Drawable mAvatar;
    private Bitmap mBitmapAvatar;
    private Paint mCirclePaint;
    private String TAG = PowerImageView.class.getSimpleName();

    public PowerImageView(Context context) {
        super(context);
    }

    public PowerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PowerImageView(Context context, AttributeSet attrs, int defStyle) {
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
