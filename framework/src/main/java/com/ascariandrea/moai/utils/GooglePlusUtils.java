package com.ascariandrea.moai.utils;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by andreaascari on 15/10/14.
 */
public class GooglePlusUtils {

    public static Person getMe(GoogleApiClient googleApiClient) {
        Person me = null;
        if (googleApiClient != null && Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
            me = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        }

        return me;
    }
}
