package com.ascariandrea.afw.fragments;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ascariandrea.afw.AFWApp;
import com.ascariandrea.afw.BuildConfig;
import com.ascariandrea.afw.activities.AFWLoginActivity;
import com.ascariandrea.afw.utils.Utils;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;

import java.io.IOException;

/**
 * Created by andreaascari on 11/10/14.
 */
@EFragment
public abstract class InjectedLoginSelectionFragment extends InjectedFragment  {

    private String TAG = InjectedLoginSelectionFragment.class.getSimpleName();
    private final String[] READ_PERMISSION = new String[]{"email", "public_profile"};
    /* Request code used to invoke sign in user interactions. */

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
        Log.d(TAG, resultCode + " " + requestCode + " " +  data);
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

    @Override
    public void afterViewsInjected() {
        super.afterViewsInjected();


        if (getLoginActivity() != null) {

            if (getLoginActivity().isTwitterLoginEnabled()) {
                if (getTwitterButton() != null) {
                    getTwitterButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getLoginActivity().twitterLogin();
                        }
                    });
                }
            }

            if (getLoginActivity().isGooglePlusLoginEnabled()) {
                if (getGooglePlusButton() != null) {
                    getGooglePlusButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getLoginActivity().getGooglePlusClient().connect();
                        }
                    });
                }
            }
        }
    }


    protected abstract Button getTwitterButton();


    protected abstract SignInButton getGooglePlusButton();


}
