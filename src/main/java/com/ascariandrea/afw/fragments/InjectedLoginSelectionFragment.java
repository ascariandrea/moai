package com.ascariandrea.afw.fragments;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.ascariandrea.afw.BuildConfig;
import com.ascariandrea.afw.activities.AFWLoginActivity;
import com.facebook.Session;
import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.security.UnrecoverableEntryException;

/**
 * Created by andreaascari on 11/10/14.
 */
@EFragment
public abstract class InjectedLoginSelectionFragment extends InjectedFragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = InjectedLoginSelectionFragment.class.getSimpleName();
    private final String[] READ_PERMISSION = new String[]{"email", "public_profile"};
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int RC_AUTH = 1;
    public static final int RE_AUTH_ACTIVITY_CODE = 100;

    private Bundle mSavedInstanceState;
    private GoogleApiClient mPlusClient;

    private boolean mIntentInProgress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        getLoginActivity().getUiHelper().onCreate(mSavedInstanceState);
    }

    protected abstract boolean isGooglePlusLoginEnabled();


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

    // GOOGLE PLUS

    protected GoogleApiClient getGooglePlusClient() {
        if (isGooglePlusLoginEnabled())
            if (mPlusClient == null)
                mPlusClient = new GoogleApiClient.Builder(
                        getActivity(),
                        this,
                        this)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_LOGIN)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .build();

        return mPlusClient;
    }


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
        appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, getAppActivitiesString());
        String scopes = "";
        String code = null;
        String token = null;
        try {

            token = GoogleAuthUtil.getToken(
                    getActivity(),
                    Plus.AccountApi.getAccountName(getGooglePlusClient()),
                    scopes);

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

        Log.d(TAG, token);

    }

    private String getAppActivitiesString() {

        return null;
    }

    @Override
    public void onViewInjected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                getGooglePlusClient().connect();
            }
        }
    }
}
