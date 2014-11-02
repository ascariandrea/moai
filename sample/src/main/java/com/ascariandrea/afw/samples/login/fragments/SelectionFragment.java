package com.ascariandrea.afw.samples.login.fragments;

import android.widget.Button;

import com.ascariandrea.afw.fragments.InjectedFragment;
import com.ascariandrea.afw.fragments.InjectedLoginSelectionFragment;
import com.ascariandrea.afw.samples.R;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;

/**
 * Created by andreaascari on 01/11/14.
 */
@EFragment(R.layout.fragment_selection)
public class SelectionFragment extends InjectedLoginSelectionFragment {

    @ViewById(R.id.facebookButtonLogin)
    LoginButton facebookLoginButton;

    @ViewById(R.id.twitterButtonLogin)
    Button twitterButtonLogin;

    @ViewById(R.id.googlePlusLoginButton)
    SignInButton googlePlusLoginButton;


    @Override
    public void onViewInjected() {
        facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
    }

    @Override
    protected Button getTwitterButton() {
        return twitterButtonLogin;
    }

    @Override
    protected SignInButton getGooglePlusButton() {
        return googlePlusLoginButton;
    }
}
