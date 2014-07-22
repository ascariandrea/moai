package com.seventydivision.framework.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.seventydivision.framework.utils.Utils;

/**
 * Created by andreaascari on 22/05/14.
 */
public class PoweredEditText extends EditText {
    private OnClickListener mOnBackPressedListener;

    public PoweredEditText(Context context) {
        super(context);
    }

    public PoweredEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PoweredEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.Views.hideKeyBoard(getContext(), this);
            this.onBackPressed();
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void onBackPressed() {
        mOnBackPressedListener.onClick(this);
    }


    public void setOnBackPressedListener(OnClickListener listener) {
        mOnBackPressedListener = listener;
    }
}
