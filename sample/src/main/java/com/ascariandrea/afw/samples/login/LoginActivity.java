package com.ascariandrea.afw.samples.login;


import com.ascariandrea.afw.activities.AFWLoginActivity;
import com.ascariandrea.afw.fragments.InjectedFragment;
import com.ascariandrea.afw.fragments.InjectedLoginSelectionFragment;
import com.ascariandrea.afw.persist.PersistentPreferences;
import com.ascariandrea.afw.samples.login.fragments.SelectionFragment_;
import com.ascariandrea.afw.samples.login.fragments.SplashFragment_;
import com.ascariandrea.afw.utils.Utils;
import com.facebook.Session;

import org.androidannotations.annotations.EActivity;

import twitter4j.auth.AccessToken;

/**
 * Created by andreaascari on 01/11/14.
 */
@EActivity
public class LoginActivity extends AFWLoginActivity {

    /*
     * Return a simple fragment instance to serve a waiting page while you
     * authenticate the user to your service.
     */

    @Override
    protected InjectedFragment getSplashFragment() {
        return SplashFragment_.builder().build();
    }

    /*
     * Return an instance of InjectedLoginSelectionFragment
     * to handle Facebook Session life cycle.
     */

    @Override
    protected InjectedLoginSelectionFragment getSelectionFragment() {
        return SelectionFragment_.builder().build();
    }

    /*
     * Now you can get the Facebook user access token
     * and use it to authenticate the user to your service.
     */

    @Override
    protected void onFacebookAuthentication(Session session) {
        Utils.Views.showLongToast(this, "Facebook Token: " + session.getAccessToken());
    }

    /*
     * The PersistentPreferences are used to store twitter both
     * access token and access token secret and a status flag on Twitter login (true, false).
     */
    @Override
    protected PersistentPreferences getPersistentPreferences() {
        return getPrefs();
    }

    /*
     * Tell AFWLoginActivity if Twitter login is enable,
     * so it can handle it or not.
     */
    @Override
    public boolean isTwitterLoginEnabled() {
        return true;
    }

    /*
     * As Facebook, Twitter login return the user access token
     * that can you hold with the below method
     */
    @Override
    protected void onTwitterAuthentication(AccessToken token) {
        Utils.Views.showLongToast(this, "Twitter Token: " + token.getToken() + "///" + token.getTokenSecret());
    }

    /*
     * Same as Twitter, but for Google+.
     */
    @Override
    public boolean isGooglePlusLoginEnabled() {
        return true;
    }

    /*
     * After the first authentication to your service is useful
     * to cache your authorization token, so this is the method to handle
     * already authorized users.
     */
    @Override
    protected void onAuthorizationTokenFound(String authorizationToken) {

    }


}
