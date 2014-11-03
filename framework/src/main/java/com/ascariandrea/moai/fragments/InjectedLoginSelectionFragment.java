package com.ascariandrea.moai.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ascariandrea.moai.activities.SocialLoginActivity;
import com.google.android.gms.common.SignInButton;

import org.androidannotations.annotations.EFragment;

/**
 * Created by andreaascari on 11/10/14.
 */
@EFragment
public abstract class InjectedLoginSelectionFragment extends InjectedFragment  {

    protected final String[] READ_PERMISSION = new String[]{"email", "public_profile"};

    private Bundle mSavedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        getLoginActivity().getUiHelper().onCreate(mSavedInstanceState);
    }


    public SocialLoginActivity getLoginActivity() {
        return ((SocialLoginActivity) getActivity());
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
