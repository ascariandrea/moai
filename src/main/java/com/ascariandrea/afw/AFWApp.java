package com.ascariandrea.afw;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.ascariandrea.afw.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by andreaascari on 15/10/14.
 */
public class AFWApp extends Application {

    private static final int PLAY_SERVICE_RESOLUTION_REQUEST = 9000;

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
}
