package com.ascariandrea.afw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.ascariandrea.afw.fragments.InjectedFragment;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import org.androidannotations.annotations.EActivity;

/**
 * Created by andreaascari on 11/10/14.
 */
@EActivity
public abstract class AFWLoginActivity extends AFWFragmentManagerActivity {

    private static final String TAG = AFWLoginActivity.class.getSimpleName();
    private static final int SPLASH = 0;
    private static final int SELECTION = 1;



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

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
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


    protected abstract InjectedFragment getSelectionFragment();

    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        Log.d(TAG, session.toString());
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


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            showFragment(SPLASH);
        } else {
            showFragment(SELECTION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

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

    protected void setFbLogging(boolean b) {
        mFbLogging = true;
    }

    protected boolean isFbLogging() {
        return mFbLogging;
    }
}
