package com.seventydivision.framework;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by andreaascari on 03/07/14.
 */

@ReportsCrashes(
        formKey = "formKey",
        formUri = "http://acra.70division.com/gift"
)
public class MainApplication extends Application {


    private String mACRAFormUri;
    private String mACRAFormKey;

    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
    }


    public void setACRAReportsCrashes(String formKey, String formUri) {
        mACRAFormKey = formKey;
        mACRAFormUri = formUri;
    }
}
