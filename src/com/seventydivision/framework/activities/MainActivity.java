package com.seventydivision.framework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.Session;
import com.seventydivision.framework.persist.PersistentPreferences;

import butterknife.ButterKnife;

/**
 * Created by andreaascari on 22/01/14.
 */

public abstract class MainActivity extends FragmentActivity {

    private Session mFbSession;
    private PersistentPreferences mPrefs;

    private boolean mLaunching;
    private boolean mViewInjectionEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebookSession(false);
        if (!isViewInjectionEnabled())
        injectInView();

    }

    private boolean isViewInjectionEnabled() {
        return mViewInjectionEnabled;
    }

    protected void initFacebookSession(boolean b) {
        mFbSession = new Session(this);
    }


    protected void injectInView() {
        ButterKnife.inject(this);
        onViewInjected();
    }

    public abstract void onViewInjected();


    protected void enableBackButton() {
        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    protected void launch(Class<?> a) {
        mLaunching = true;
        startActivity(new Intent(this, a));
        mLaunching = false;

    }

    protected void launchForResult(Class<?> a, int requestCode) {
        startActivityForResult(new Intent(this, a), requestCode);
    }


    protected void updateFbSession() {
        if (mFbSession.isClosed() ||
                mFbSession.getAccessToken().isEmpty()) {
            mFbSession = new Session(this);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public boolean isLaunching() {
        return mLaunching;
    }

    protected void setViewInjectionEnabled(boolean enabled) {
        this.mViewInjectionEnabled = enabled;
    }


    public PersistentPreferences getPrefs() {
        return mPrefs;
    }
}
