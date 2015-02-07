package com.ascariandrea.moai.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.ascariandrea.moai.R;
import com.ascariandrea.moai.utils.FontCache;

/**
 * Created by andreaascari on 20/01/15.
 */
public class CustomTextView extends TextView {
    private static final String TAG = CustomTextView.class.getSimpleName();

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontTxtView);
        String customFont = a.getString(R.styleable.CustomFontTxtView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }


    public boolean setCustomFont(Context ctx, String asset) {
        setTypeface(FontCache.getFont(ctx, asset));
        return true;
    }
}
