package com.ascariandrea.afw.samples.login;

import android.app.Activity;

import com.ascariandrea.afw.activities.AFWLoginActivity;
import com.ascariandrea.afw.fragments.InjectedFragment;
import com.ascariandrea.afw.persist.PersistentPreferences;
import com.facebook.Session;

import twitter4j.auth.AccessToken;

/**
 * Created by andreaascari on 01/11/14.
 */
public class LoginActivity extends AFWLoginActivity {

    @Override
    protected InjectedFragment getSplashFragment() {
        return null;
    }

    @Override
    protected InjectedFragment getSelectionFragment() {
        return null;
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

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
