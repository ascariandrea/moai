package com.ascariandrea.afw.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ascariandrea.afw.R;
import com.ascariandrea.afw.utils.TwitterUtils;
import com.ascariandrea.afw.utils.Utils;

import org.androidannotations.annotations.UiThread;

import oauth.signpost.OAuth;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by andreaascari on 21/10/14.
 */

public class TwitterDialog extends Dialog {

    private final String mUrl;
    private final TwitterUtils.TwitterOAuthInterface mTwitterOAuthInterface;
    private WebView twitterLoginWebView;
    private RequestToken mRequestToken;
    private ProgressDialog mProgressDialog;
    private LinearLayout mContent;
    private boolean authComplete;

    public TwitterDialog(Context context, RequestToken requestToken, TwitterUtils.TwitterOAuthInterface twitterOAuthInterface) {
        super(context);
        mUrl = requestToken.getAuthorizationURL();
        mRequestToken = requestToken;
        mTwitterOAuthInterface = twitterOAuthInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_twitter_login);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


        twitterLoginWebView = (WebView) findViewById(R.id.twitterLoginWebView);
        twitterLoginWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        Point screenSize = Utils.Screen.getSize(getWindow().getWindowManager());
        twitterLoginWebView.setLayoutParams(new FrameLayout.LayoutParams(
                screenSize.x - (screenSize.x / 30),
                screenSize.y - (screenSize.y / 30)
        ));

        twitterLoginWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("url loaded", url);
                if (url.contains(OAuth.OAUTH_VERIFIER) && !authComplete) {
                    authComplete = true;
                    Uri uri = Uri.parse(url);
                    getAccessTokenAndFinish(uri);
                    return true;
                }

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mProgressDialog != null && !mProgressDialog.isShowing())
                    mProgressDialog.show();
            }
        });

        twitterLoginWebView.loadUrl(mUrl);

    }

    private void getAccessTokenAndFinish(final Uri url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String verifier = url.getQueryParameter(OAuth.OAUTH_VERIFIER);
                try {
                    AccessToken accessToken = TwitterUtils.getTwitterInstance().getOAuthAccessToken(mRequestToken, verifier);
                    mTwitterOAuthInterface.onTwitterAuthenticationSuccess(TwitterDialog.this, accessToken);

                } catch (TwitterException e) {
                    e.printStackTrace();
                    mTwitterOAuthInterface.onTwitterAuthenticationFailure(e);
                }

            }
        }).start();
    }

}
