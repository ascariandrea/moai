package com.ascariandrea.afw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.Session;
import com.ascariandrea.afw.persist.PersistentPreferences;
import com.ascariandrea.afw.utils.Utils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;


/**
 * Created by andreaascari on 22/01/14.
 */
@EActivity
public abstract class AFWFragmentActivity extends FragmentActivity {

    private static final int NO_REQUEST_CODE = -99;
    private String TAG = AFWFragmentActivity.class.getSimpleName();

    public static final int RESULT_DONE = 4;

    private Session mFbSession;
    protected PersistentPreferences mPrefs;


    @Extra public int requestCode = NO_REQUEST_CODE;

    private boolean mLaunching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebookSession(false);
        onCreated();
    }

    protected void onCreated() {

    }

    @AfterInject
    protected void afterInjection() {
        onInjection();
    }

    protected void onInjection() {
        // method stub
    }

    protected void initFacebookSession(boolean b) {
        mFbSession = new Session(this);
    }


    @AfterViews
    public void afterViews() {
        onViewInjected();
    }


    public abstract void onViewInjected();


    protected void enableBackButton() {
        if (getActionBar() != null) {
            if (Utils.API.isGreatEqualsThan(14))
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


    public PersistentPreferences getPrefs() {
        return mPrefs;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
