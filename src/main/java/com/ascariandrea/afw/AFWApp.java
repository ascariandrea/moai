package com.ascariandrea.afw;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.ascariandrea.afw.persist.PersistentPreferences;
import com.ascariandrea.afw.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by andreaascari on 15/10/14.
 */

public abstract class AFWApp extends Application {

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private static final int PLAY_SERVICE_RESOLUTION_REQUEST = 9000;
    private static final String TAG = AFWApp.class.getSimpleName();
    private GoogleCloudMessaging mGcm;
    private String regId;
    private PersistentPreferences mPrefs;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean checkGooglePlayService(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil
                        .getErrorDialog(resultCode, activity, PLAY_SERVICE_RESOLUTION_REQUEST)
                        .show();
            } else {
                Utils.Dialogs.showError(activity, "This device is not supported.");
            }
            return false;
        }

        return true;
    }


    public void registerDeviceToken(Activity activity) {
        if (checkGooglePlayService(activity)) {
            mGcm = GoogleCloudMessaging.getInstance(activity);
            regId = getRegistrationId(activity);
            if (!regId.isEmpty()) {
                registerInBackground(activity);
            }
        } else {
            Utils.Dialogs.showError(activity, "No valid Google Play Services APK found.");
        }
    }

    public void registerInBackground(final Activity activity) {
        String senderId = getSenderId();
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }

                    regId = mGcm.register(params[0]);
                    msg = "Device registered, registration ID=" + regId;
                } catch (IOException ex) {
                    msg = "Error: " + ex.getMessage() + " ";
                    ex.printStackTrace();
                }

                Log.d(TAG, msg);
                return regId;
            }

            @Override
            protected void onPostExecute(String s) {
                sendRegistrationToBackend(activity, s);

            }
        }.execute(senderId, null, null);

    }

    protected abstract void sendRegistrationToBackend(Activity activity, String regId);


    protected void storeRegistrationId(Context context, String regId) {
        final SharedPreferences preferences = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(context.getClass().getSimpleName(), MODE_PRIVATE);
    }

    protected abstract String getSenderId();


    private String getRegistrationId(Activity activity) {
        final SharedPreferences preferences = getGCMPreferences(activity);
        String registrationId = preferences.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        Log.d(TAG, "Registration found: " + registrationId);

        return registrationId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }

    }

}
