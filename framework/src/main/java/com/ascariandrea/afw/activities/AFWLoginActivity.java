package com.ascariandrea.afw.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.ascariandrea.afw.AFWApp;
import com.ascariandrea.afw.BuildConfig;
import com.ascariandrea.afw.R;
import com.ascariandrea.afw.fragments.InjectedFragment;
import com.ascariandrea.afw.fragments.InjectedLoginSelectionFragment;
import com.ascariandrea.afw.persist.PersistentPreferences;
import com.ascariandrea.afw.utils.TwitterUtils;
import com.ascariandrea.afw.utils.Utils;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import static com.ascariandrea.afw.R.layout;

/**
 * Created by andreaascari on 11/10/14.
 */
@EActivity
public abstract class AFWLoginActivity extends AFWFragmentManagerActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TwitterUtils.TwitterOAuthInterface {

    private static final String TAG = AFWLoginActivity.class.getSimpleName();
    private static final int SPLASH = 0;
    private static final int SELECTION = 1;

    private static final int RC_SIGN_IN = 0;
    private static final int RC_AUTH = 1;
    public static final int RE_AUTH_ACTIVITY_CODE = 100;

    protected static final String GOOGLE_TYPE = "google";
    protected static final String FACEBOOK_TYPE = "facebook";
    protected static final String TWITTER_TYPE = "twitter";


    private GoogleApiClient mPlusClient;

    private boolean mIntentInProgress;
    private boolean isResumed = false;
    private boolean mFbLogging = false;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.fragment_manager_activity);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentManagerContainer;
    }

    @Override
    public void onViewInjected() {
        addFragment(SPLASH);
        addFragment(SELECTION);
        hideFragment(SELECTION);
        showFragment(SPLASH, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <F extends InjectedFragment> F getFragmentAtIndex(int nextFragmentIndex) {
        InjectedFragment f;
        switch (nextFragmentIndex) {
            case SPLASH:
                f = getSplashFragment();
                break;
            case SELECTION:
            default:
                f = getSelectionFragment();
                break;
        }

        return (F) f;
    }

    protected abstract InjectedFragment getSplashFragment();


    protected abstract InjectedLoginSelectionFragment getSelectionFragment();

    // FACEBOOK

    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        Log.d(TAG, session.toString());
        Log.d(TAG, session.getAccessToken() + "");
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            // Get the number of entries in the back stack
            int backStackSize = manager.getBackStackEntryCount();
            // Clear the back stack
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            if (state.isOpened()) {
                // If the session state is open:
                // Show the authenticated fragment
                showFragment(SPLASH, false);
                onFbSessionOpen(session);
            } else if (state.isClosed()) {
                // If the session state is closed:
                // Show the login fragment
                showFragment(SELECTION, false);
            }
        }

    }


    protected abstract void onFbSessionOpen(Session session);




    // TWITTER

    public abstract boolean isTwitterLoginEnabled();

    public void twitterLogin() {
        TwitterUtils.getInstance().authenticate(this, this);
    }

    @Override
    public void onTwitterAuthenticationSuccess(Dialog dialog, AccessToken accessToken) {
        showFragment(SPLASH);
        Utils.Dialogs.clearScreen();
        if (dialog != null)
            dialog.dismiss();
        TwitterUtils.getInstance().storeTwitterAuth(getPersistentPreferences(), accessToken);
        onTwitterAuthorization(accessToken);

    }

    protected abstract PersistentPreferences getPersistentPreferences();

    @Override
    public void onTwitterAuthenticationFailure(TwitterException e) {

    }


    @UiThread
    protected abstract void onTwitterAuthorization(AccessToken token);

    // GOOGLE PLUS

    public GoogleApiClient getGooglePlusClient() {
        if (isGooglePlusLoginEnabled()) {
            if (AFWApp.checkGooglePlayService(this)) {
                if (mPlusClient == null) {
                    mPlusClient = new GoogleApiClient.Builder(
                            AFWLoginActivity.this,
                            this,
                            this)
                            .addApi(Plus.API)
                            .addScope(Plus.SCOPE_PLUS_LOGIN)
                            .addScope(Plus.SCOPE_PLUS_PROFILE)
                            .build();
                }
            }
        }

        return mPlusClient;
    }

    public abstract boolean isGooglePlusLoginEnabled();


    @Override
    public void onConnected(Bundle bundle) {
        if (BuildConfig.DEBUG && bundle != null)
            Log.d(TAG, bundle.toString());

        retrieveGoogleAuthToken();

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Background
    protected void retrieveGoogleAuthToken() {
        Bundle appActivities = new Bundle();
        appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, Utils.String.join(getAppActivitiesString(), " "));

        String oauthScopes = Utils.GoogleAuth.getServerOAuthUrl(null, true, Scopes.PLUS_LOGIN);
        String token = null;
        try {

            Log.d("scopes", oauthScopes);
            token = GoogleAuthUtil.getToken(
                    this,
                    Plus.AccountApi.getAccountName(getGooglePlusClient()),
                    oauthScopes);

        } catch (UserRecoverableAuthException e) {
            Log.d(TAG, e.getLocalizedMessage());
            startActivityForResult(e.getIntent(), RC_AUTH);
            return;

        } catch (GoogleAuthException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (token != null) {
            onGooglePlusAuth(token);
        }

    }

    private String[] getAppActivitiesString() {
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("Connection failed: ", result.toString());
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                getGooglePlusClient().connect();
            }
        }
    }

    @OnActivityResult(RC_SIGN_IN)
    protected void onGoogleSignInRequest(int resultCode, Intent data) {
        getGooglePlusClient().connect();
    }

    @OnActivityResult(RC_AUTH)
    protected void onGoogleAuthRequest(int resultCode, Intent data) {
        getGooglePlusClient().connect();
    }


    @UiThread
    public void onGooglePlusAuth(String token) {

    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();
        if ((session != null && session.isOpened()) || hasAuthorizationToken()) {
            showFragment(SPLASH);
        } else {
            showFragment(SELECTION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        uiHelper.onResume();
        if (!hasAuthorizationToken()) {
            onSessionStateChange(Session.getActiveSession(), Session.getActiveSession().getState(), null);
        } else {
            onAuthorizationTokenFound();
        }


    }

    protected abstract void onAuthorizationTokenFound();

    protected abstract boolean hasAuthorizationToken();

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }


    public UiLifecycleHelper getUiHelper() {
        return uiHelper;
    }

    protected void setIsLogging(boolean b) {
        mFbLogging = true;
    }

    protected boolean isLogging() {
        return mFbLogging;
    }

}
