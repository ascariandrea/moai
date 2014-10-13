package com.ascariandrea.afw.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ascariandrea.afw.activities.AFWLoginActivity;
import com.facebook.Session;
import com.facebook.widget.LoginButton;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

/**
 * Created by andreaascari on 11/10/14.
 */
@EFragment
public abstract class InjectedLoginSelectionFragment extends InjectedFragment {

    private final String[] READ_PERMISSION = new String[]{"email", "public_profile"};
    public final int RE_AUTH_ACTIVITY_CODE = 100;

    private Bundle mSavedInstanceState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        getLoginActivity().getUiHelper().onCreate(mSavedInstanceState);
    }


    public AFWLoginActivity getLoginActivity() {
        return ((AFWLoginActivity) getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getLoginActivity().getUiHelper().onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLoginActivity().getUiHelper().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();
        getLoginActivity().getUiHelper().onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        getLoginActivity().getUiHelper().onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoginActivity().getUiHelper().onDestroy();
    }

}
