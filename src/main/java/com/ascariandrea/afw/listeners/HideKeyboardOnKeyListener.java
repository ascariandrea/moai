package com.ascariandrea.afw.listeners;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by andreaascari on 06/02/14.
 */
public class HideKeyboardOnKeyListener implements View.OnKeyListener {

    private final Context mContext;

    public HideKeyboardOnKeyListener(Context context) {
        mContext = context;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ( (event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }

        return false;
    }
}
