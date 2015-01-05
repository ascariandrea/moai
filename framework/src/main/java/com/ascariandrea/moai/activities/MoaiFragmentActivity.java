package com.ascariandrea.moai.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ascariandrea.moai.persist.PersistentPreferences;
import com.ascariandrea.moai.utils.Utils;
import com.facebook.Session;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;


/**
 * Created by andreaascari on 22/01/14.
 */
@EActivity
public abstract class MoaiFragmentActivity extends FragmentActivity {

    public static final int RESULT_DONE = 4;
    private static final int NO_REQUEST_CODE = -99;
    private static final java.lang.String FACEBOOK_SDK_APP_ID = "com.facebook.sdk.ApplicationId";
    @Extra
    public int requestCode = NO_REQUEST_CODE;
    protected PersistentPreferences mPrefs;
    private String TAG = MoaiFragmentActivity.class.getSimpleName();
    private Session mFbSession;
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
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String fbId = bundle.getString(FACEBOOK_SDK_APP_ID);
            if (fbId != null && !fbId.isEmpty()) {
                mFbSession = new Session(this);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
        if (mPrefs == null) mPrefs = new PersistentPreferences(this);
        return mPrefs;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
