package com.ascariandrea.afw.client;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public abstract class AsyncHandler extends AsyncHttpResponseHandler {


    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

    }


    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }
}
