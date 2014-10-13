package com.ascariandrea.afw.utils;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;


/**
 * Created by andreaascari on 09/10/14.
 */
public class TwitterUtils {

    /////////////////Customizable fields and methods//////////////////////////
    private static final String CONSUMER_KEY = "8U99vtcSLG4USG5ahuiK7q4qq";
    private static final String CONSUMER_KEY_SECRET = "L8R0Cj8o7ym12Oi4Yn5702a8GssAVcxJvUvuE9noMWk1f7OYJC";
    private static final String ACCESS_TOKEN = "452357036-51UlJfkPvmVOsbAy0hNigTKdwLkqAqQPttqLZkiy";
    private static final String ACCESS_TOKEN_SECRET = "k9ojDcvoEtDlb9OIRw2fwWM70f4gvNjhyJ1qRw9TW1yYC";
    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static TwitterUtils mInstance;
    private String mConsumerKey;
    private String mConsumerKeySecret;
    private String mCallbackURL;


    public static void init(String consumerKey, String consumerKeySecret, String accessToken, String accessTokenSecret, String mCallbackURL) {
        TwitterUtils.getInstance().setConsumerKey(consumerKey);
        TwitterUtils.getInstance().setConsumerKeySecret(consumerKeySecret);
        TwitterUtils.getInstance().setCallbackURL(mCallbackURL);
    }

    private static TwitterUtils getInstance() {
        if (mInstance == null) mInstance = new TwitterUtils();
        return mInstance;
    }

    public static String getConsumerKey() {
        return TwitterUtils.getInstance().mConsumerKey;
    }


    private void setConsumerKeySecret(String consumerKeySecret) {
        this.mConsumerKeySecret = consumerKeySecret;
    }


    private void setConsumerKey(String consumerKey) {
        this.mConsumerKey = consumerKey;
    }

    public static twitter4j.auth.AccessToken getAccessToken() {
        try {
            return TwitterFactory.getSingleton().getOAuthAccessToken();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static twitter4j.auth.RequestToken getOauthRequestToken() {
        try {
            return TwitterFactory.getSingleton().getOAuthRequestToken(getCallbackURL());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCallbackURL() {
        return TwitterUtils.getInstance().mCallbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.mCallbackURL = callbackURL;
    }
//    private static CommonsHttpOAuthConsumer getStoredCommonsHttpOAuthConsumer() {
//        return Yourp.getInstance().getTwitterConsumer();
//    }
//
//    private static void storeCommonsHttpOAuthConsumer(CommonsHttpOAuthConsumer choac) {
//        Yourp.getInstance().setTwitterConsumer(choac);
//    }
//
//    private static DefaultOAuthProvider getStoredDefaultOAuthProvider() {
//        return Yourp.getInstance().getTwitterProvider();
//    }
//
//    private static void storeDefaultOAuthProvider(DefaultOAuthProvider choac) {
//        Yourp.getInstance().setTwitterProvider(choac);
//    }
//
//    private static void saveTwitterSession(Twitter twitter) {
//        Yourp.getInstance().setTwitterActiveSession(twitter);
//    }
//
//    private static void storeAccessToken(AccessToken a) throws IOException {
//        PreferenceManager.getDefaultSharedPreferences(Yourp.getInstance())
//                .edit().putString("ATW", Base64.encodeObject(a)).commit();
//    }
//
//    private static AccessToken retrieveAccessToken() throws ClassNotFoundException, IOException {
//        String serialized = PreferenceManager.getDefaultSharedPreferences(Yourp.getInstance())
//                .getString("ATW", null);
//        if (serialized != null)
//            return (AccessToken) Base64.decodeToObject(serialized);
//        return null;
//    }
//
//    public static void clearAccessToken() {
//        PreferenceManager.getDefaultSharedPreferences(Yourp.getInstance()).edit().remove("ATW").commit();
//    }
//    //////////////////////////////////////////////////////////////////////////
//
//    //Don't edit
//    protected static final String TWITTER_CALLBACK_URL = "callback://tweeter";
//
//    /**
//     * Call to perform a login
//     */
//    public static void performTwitterLogin(final Activity activity, final TwitterOAuthSuccessCallback callback) {
//        AccessToken a = null;
//        try {
//            a = retrieveAccessToken();
//        } catch (ClassNotFoundException e) {
//        } catch (IOException e) {
//        }
//        if (a != null) {
//            new AsyncParallelTask<AccessToken, Void, SerializableTWITTERuser>() {
//                @Override
//                protected void onPreExecute() {
//                    Utils.showProgress(activity, activity.getString(R.string.login_loading), true, this);
//                }
//
//                @Override
//                protected SerializableTWITTERuser doInBackground(AccessToken... a) {
//                    User user = null;
//                    try {
//                        user = handleAccessToken(a[0]);
//                        return Utils.serializeTWITTERuser(user);
//                    } catch (TwitterException e) {
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(SerializableTWITTERuser user) {
//                    Utils.clearScreen();
//                    if (user != null)
//                        callback.onTwitterLoginSuccess(user);
//                    else
//                        new PerformTwitterLoginTask(activity, callback).executeParallel(activity);
//                }
//            }.executeParallel(a);
//        } else {
//            new PerformTwitterLoginTask(activity, callback).executeParallel(activity);
//        }
//    }
//
//    private static String getVerifierFromCallBackUrl(String callbackUrl) {
//        String verifier = "";
//        try {
//            callbackUrl = callbackUrl.replace("callback", "http");
//            URL url = new URL(callbackUrl);
//            String query = url.getQuery();
//            String array[] = query.split("&");
//            for (String parameter : array) {
//                String v[] = parameter.split("=");
//                if (URLDecoder.decode(v[0], "UTF-8").equals(oauth.signpost.OAuth.OAUTH_VERIFIER)) {
//                    verifier = URLDecoder.decode(v[1], "UTF-8");
//                    break;
//                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return verifier;
//    }
//
//    private static class PerformTwitterLoginTask extends AsyncParallelTask<Activity, Void, String> implements TwitterDialog.TwDialogListener {
//        private final Activity _Activity;
//        private final TwitterOAuthSuccessCallback _Callback;
//
//        public PerformTwitterLoginTask(Activity activity, TwitterOAuthSuccessCallback callback) {
//            this._Activity = activity;
//            this._Callback = callback;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Utils.showProgress(this._Activity, this._Activity.getString(R.string.login_loading), true, this);
//        }
//
//        @Override
//        protected String doInBackground(final Activity... activity) {
//            try {
//                if (TwitterOAuthHelper.getStoredCommonsHttpOAuthConsumer() == null)
//                    TwitterOAuthHelper.storeCommonsHttpOAuthConsumer(new CommonsHttpOAuthConsumer(TwitterOAuthHelper.CONSUMER_KEY, TwitterOAuthHelper.CONSUMER_KEY_SECRET));
//
//                if (TwitterOAuthHelper.getStoredDefaultOAuthProvider() == null)
//                    TwitterOAuthHelper.storeDefaultOAuthProvider(new DefaultOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL));
//
//                return TwitterOAuthHelper.getStoredDefaultOAuthProvider().retrieveRequestToken(TwitterOAuthHelper.getStoredCommonsHttpOAuthConsumer(), TwitterOAuthHelper.TWITTER_CALLBACK_URL);
//            } catch (Exception e) {
//                Log.d(TwitterOAuthHelper.class.getSimpleName(), e.getMessage());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String url) {
//            if (url == null && !this.isCancelled()) {
//                Utils.clearScreen();
//                this.onError(null);
//                return;
//            }
//
//            if (this.isCancelled()) {
//                Utils.clearScreen();
//                return;
//            }
//            TwitterDialog dialog = new TwitterDialog(this._Activity, url, this);
//            Utils.clearScreen();
//            dialog.show();
//        }
//
//        @Override
//        public void onComplete(String callbackUrl) {
//            new AsyncParallelTask<String, Void, User>() {
//                @Override
//                protected User doInBackground(String... callbackUrl) {
//                    try {
//                        if (callbackUrl[0] == null)
//                            return null;
//                        // this will populate token and token_secret in consumer
//                        String verifier = TwitterOAuthHelper.getVerifierFromCallBackUrl(callbackUrl[0]);
//                        if (verifier.length() == 0)
//                            return null;
//                        TwitterOAuthHelper.getStoredDefaultOAuthProvider().retrieveAccessToken(
//                                TwitterOAuthHelper.getStoredCommonsHttpOAuthConsumer(),
//                                verifier);
//                        // Get Access Token and persist it
//                        AccessToken a = new AccessToken(TwitterOAuthHelper.getStoredCommonsHttpOAuthConsumer().getToken(), TwitterOAuthHelper.getStoredCommonsHttpOAuthConsumer().getTokenSecret());
//                        TwitterOAuthHelper.storeAccessToken(a);
//                        return handleAccessToken(a);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        this.publishProgress();
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void onProgressUpdate(Void... VOID) {
//                    onError(null);
//                }
//
//                @Override
//                protected void onPostExecute(User user) {
//                    if (user != null && _Callback != null)
//                        _Callback.onTwitterLoginSuccess(Utils.serializeTWITTERuser(user));
//                }
//            }.executeParallel(callbackUrl);
//        }
//
//        @Override
//        public void onError(String value) {
//            this._Callback.onTwitterLoginFail();
//        }
//    }
//
//    private static User handleAccessToken(AccessToken a) throws TwitterException {
//        // initialize Twitter4J
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        builder.setDebugEnabled(true)
//                .setOAuthConsumerKey(TwitterOAuthHelper.CONSUMER_KEY)
//                .setOAuthAccessTokenSecret(CONSUMER_KEY_SECRET)
//                .setOAuthAccessToken(ACCESS_TOKEN)
//                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
//                .setJSONStoreEnabled(true);
//
//        TwitterFactory twitterFactory = new TwitterFactory(builder.build());
//        Twitter twitter = twitterFactory.getInstance();
//        twitter.setOAuthConsumer(TwitterOAuthHelper.CONSUMER_KEY, TwitterOAuthHelper.CONSUMER_KEY_SECRET);
//        twitter.setOAuthAccessToken(a);
//        TwitterOAuthHelper.saveTwitterSession(twitter);
//        return twitter.showUser(a.getUserId());
//    }
//
//    public static interface TwitterOAuthSuccessCallback {
//        public void onTwitterLoginSuccess(SerializableTWITTERuser user);
//
//        public void onTwitterLoginFail();
//    }
}
