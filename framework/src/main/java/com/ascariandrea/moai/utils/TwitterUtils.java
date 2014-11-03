package com.ascariandrea.moai.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.ascariandrea.moai.dialogs.TwitterDialog;
import com.ascariandrea.moai.persist.PersistentPreferences;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Created by andreaascari on 09/10/14.
 */
public class TwitterUtils {


    public static final int RC_TWITTER_AUTH = 345;
    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String CALLBACK_URL = "callback://oauth_callback";
    private static final String TAG = TwitterUtils.class.getSimpleName();
    private static TwitterUtils mInstance;
    private static TwitterFactory mTwitterFactory;
    public TwitterDialog mAuthDialog;
    private Twitter mTwitter;
    private String mConsumerKey;
    private String mConsumerKeySecret;
    private String mAccessToken;
    private String mAccessTokenSecret;
    private String mCallbackURL;


    public static void init(String consumerKey, String consumerKeySecret) {
        init(consumerKey, consumerKeySecret, null, null);
    }

    public static void init(String consumerKey, String consumerKeySecret, String twitterAccessToken, String twitterAccessTokenSecret) {
        getInstance().setConsumerKey(consumerKey);
        getInstance().setConsumerKeySecret(consumerKeySecret);
        if (twitterAccessToken != null)
            getInstance().setAccessToken(twitterAccessToken);
        if (twitterAccessToken != null)
            getInstance().setAccessTokenSecret(twitterAccessTokenSecret);

        getInstance().factory(consumerKey, consumerKeySecret, twitterAccessToken, twitterAccessTokenSecret);
    }

    public static TwitterUtils getInstance() {
        if (mInstance == null) mInstance = new TwitterUtils();
        return mInstance;
    }

    public static Twitter getTwitterInstance() {
        return mTwitterFactory.getInstance();
    }

    public static String getConsumerKey() {
        return TwitterUtils.getInstance().mConsumerKey;
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

    private void setAccessToken(String twitterAccessToken) {
        mAccessToken = twitterAccessToken;
    }

    private static String getCallbackURL() {
        if (TwitterUtils.getInstance().mCallbackURL != null && !TwitterUtils.getInstance().mCallbackURL.isEmpty())
            return TwitterUtils.getInstance().mCallbackURL;

        return OAuth.OAUTH_CALLBACK;
    }

    public void setCallbackURL(String callbackURL) {
        this.mCallbackURL = callbackURL;
    }

    private Twitter factory(String consumerKey, String consumerKeySecret, String twitterAccessToken, String twitterAccessTokenSecret) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerKeySecret);

        if (twitterAccessToken != null)
            builder.setOAuthAccessToken(twitterAccessToken);

        if (twitterAccessTokenSecret != null)
            builder.setOAuthAccessTokenSecret(twitterAccessTokenSecret);

        mTwitterFactory = new TwitterFactory(builder.build());
        return (mTwitterFactory.getInstance());
    }

    private void setAccessTokenSecret(String twitterAccessTokenSecret) {
        mAccessTokenSecret = twitterAccessTokenSecret;
    }

    private void setConsumerKeySecret(String consumerKeySecret) {
        this.mConsumerKeySecret = consumerKeySecret;
    }

    public twitter4j.auth.RequestToken getOauthRequestToken() {
        try {
            return mTwitter.getOAuthRequestToken();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void authenticate(final Activity activity, final TwitterOAuthInterface twitterOAuthInterface) {

        if (TwitterUtils.getInstance().mAccessToken == null) {
            Utils.Dialogs.showCircleProgress(activity);

            new AsyncTask<String, String, RequestToken>() {

                @Override
                protected RequestToken doInBackground(String... params) {
                    try {
                        return TwitterUtils.getTwitterInstance().getOAuthRequestToken(getCallbackURL());
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(final RequestToken requestToken) {
                    if (requestToken != null && requestToken.getAuthorizationURL() != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAuthDialog = new TwitterDialog(activity, requestToken, twitterOAuthInterface);
                                mAuthDialog.setOwnerActivity(activity);
                                mAuthDialog.show();
                                mAuthDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        Utils.Dialogs.clearScreen();
                                    }
                                });
                            }
                        });

                    }
                }
            }.execute();
        } else {
            twitterOAuthInterface.onTwitterAuthenticationSuccess(null, new AccessToken(getInstance().mAccessToken, getInstance().mAccessTokenSecret));
        }
    }

    public void storeTwitterAuth(PersistentPreferences persistentPreferences, AccessToken accessToken) {
        persistentPreferences.saveTwitterAccessToken(accessToken.getToken());
        persistentPreferences.saveTwitterAccessTokenSecret(accessToken.getTokenSecret());
        persistentPreferences.saveTwitterLogin(true);
        init(mConsumerKey, mConsumerKeySecret, accessToken.getToken(), accessToken.getTokenSecret());
    }


    public static interface TwitterOAuthInterface {
        public void onTwitterAuthenticationSuccess(Dialog dialog, AccessToken accessToken);

        public void onTwitterAuthenticationFailure(TwitterException e);
    }

}
