package com.ascariandrea.moai.listeners;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ascariandrea.moai.utils.Utils;

/**
 * Created by andreaascari on 06/02/14.
 */
public class HideKeyboardOnEditorActionListener implements TextView.OnEditorActionListener {

    private final Context mContext;

    public HideKeyboardOnEditorActionListener(Context context) {
        mContext = context;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            Utils.Views.hideKeyBoard(mContext, v);
            return true;
        }

        return false;
    }
}
