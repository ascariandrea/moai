package com.ascariandrea.afw.persist;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class PersistentPreferences {
    private static final String PREFS_FILE = "hs-mPrefs";
    private static final String TWITTER_ACCESS_TOKEN = "twitter-access-token";
    private static final String TWITTER_ACCESS_TOKEN_SECRET = "twitter-access-token-secret";
    private static final String TWITTER_LOGIN = "twitter-login";
    private static final String USER_TOKEN = "user-token";


    protected Context mContext;
    protected SharedPreferences mPrefs;
    private String TAG = PersistentPreferences.class.getSimpleName();

    public PersistentPreferences(Context context) {
        mContext = context;
    }

    protected String getPref(String key) {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return mPrefs.getString(key, "{}");
    }

    protected boolean getBooleanPrefs(String key) {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return mPrefs.getBoolean(key, false);
    }

    protected Set<String> getSetPref(String key) {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return mPrefs.getStringSet(key, null);
    }

    protected void putPref(String key, String value) {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    protected void putPref(String key, boolean b) {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean(key, b).apply();
    }

    protected void deletePref(String key) {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        Editor editor = mPrefs.edit();
        editor.remove(key);
        editor.apply();
    }

    protected void deleteAll() {
        if (mPrefs == null) mPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        Editor editor = mPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public String getCurrentUser() {
        return getPref("user");
    }

    public void saveCurrentUser(String jsonUser) {
        putPref("user",jsonUser);
    }


    public String getFbImage(String fbUserId, int size) {
        return getPref("fb-image-" + fbUserId + "-" + size);
    }

    public void saveFbImage(String fbUserId, int size, String fbImageUrl) {
        putPref("fb-image-" + fbUserId + "-" + size, fbImageUrl);
    }

    public String getFbFriends() {
        return getPref("fb-friends");
    }

    public void saveFbFriends(String fbFriends) {
        putPref("fb-friends",fbFriends);
    }

    public void saveTwitterAccessToken(String accessToken) {
        putPref(TWITTER_ACCESS_TOKEN, accessToken);
    }

    public void saveTwitterAccessTokenSecret(String accessTokenSecret) {
        putPref(TWITTER_ACCESS_TOKEN_SECRET, accessTokenSecret);
    }

    public void saveTwitterLogin(boolean b) {
        putPref(TWITTER_LOGIN, b);

    }

    public boolean isTwitterLogged() {
        return getBooleanPrefs(TWITTER_LOGIN);
    }

    public String getTwitterAccessToken() {
        return getPref(TWITTER_ACCESS_TOKEN);
    }

    public String getTwitterAccessTokenSecret() {
        return getPref(TWITTER_ACCESS_TOKEN_SECRET);
    }

    public void saveUserToken(String token) {
        putPref(USER_TOKEN, token);
    }
}
