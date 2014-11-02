package com.ascariandrea.afw.samples.login;


import com.ascariandrea.afw.activities.AFWLoginActivity;
import com.ascariandrea.afw.fragments.InjectedFragment;
import com.ascariandrea.afw.fragments.InjectedLoginSelectionFragment;
import com.ascariandrea.afw.persist.PersistentPreferences;
import com.ascariandrea.afw.samples.login.fragments.SelectionFragment_;
import com.ascariandrea.afw.samples.login.fragments.SplashFragment_;
import com.facebook.Session;

import org.androidannotations.annotations.EActivity;

import twitter4j.auth.AccessToken;

/**
 * Created by andreaascari on 01/11/14.
 */
@EActivity
public class LoginActivity extends AFWLoginActivity {


    @Override
    protected InjectedFragment getSplashFragment() {
        return SplashFragment_.builder().build();
    }

    @Override
    protected InjectedLoginSelectionFragment getSelectionFragment() {
        return SelectionFragment_.builder().build();
    }

    @Override
    protected void onFbSessionOpen(Session session) {

    }

    @Override
    public boolean isTwitterLoginEnabled() {
        return false;
    }

    @Override
    protected PersistentPreferences getPersistentPreferences() {
        return null;
    }

    @Override
    protected void onTwitterAuthorization(AccessToken token) {

    }

    @Override
    public boolean isGooglePlusLoginEnabled() {
        return false;
    }

    @Override
    protected void onAuthorizationTokenFound() {

    }

    @Override
    protected boolean hasAuthorizationToken() {
        return false;
    }

}
