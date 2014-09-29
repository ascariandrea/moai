package com.ascariandrea.afw.client;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public abstract class BaseClient {
    private static final String TAG = BaseClient.class.getSimpleName();
    protected static AsyncHttpClient client = new AsyncHttpClient();
    private String authHeader = null;

    public void setAuthHeader(String ah) {
        authHeader = ah;
        client.addHeader("Authorization", authHeader);
    }

    public void resetAuthHeader() {
        authHeader = null;
        client.addHeader("Authorization", null);
    }

    public boolean isAuthenticated() {
        return authHeader != null;
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, new RequestParams(), responseHandler);
    }


    public void post(String url, String json, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(json);
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void put(String url, String json, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            client.put(null, url, entity, "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getAuth(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(null, url, new RequestParams(), responseHandler);
    }

    public void getAuth(String url, HashMap<String, String> params, AsyncHttpResponseHandler responseHandler) {
        RequestParams requestParams = (params != null) ? new RequestParams(params) : new RequestParams();
        client.get(null, url, requestParams, responseHandler);
    }


    public void putAuth(String url, AsyncHttpResponseHandler responseHandler) {
        putAuth(url, "", responseHandler);
    }


    public void putAuth(String url, String json, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            client.put(null, url, entity, "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void postAuth(String url, AsyncHttpResponseHandler handler) {
        postAuth(url, new RequestParams(), handler);
    }


    public void postAuth(String url, String json, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void postAuth(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(null, url, params, responseHandler);
    }

    public void postAuth(String url, RequestParams params, AsyncHttpResponseHandler responseHandler,
                         int timeout) {
        client.setTimeout(timeout);
        client.post(null, url, params, responseHandler);
    }

    public void deleteAuth(String url, AsyncHandler handler) {
        client.delete(null, url, handler);
    }

    public void setTimeout(int timeout) {
        client.setTimeout(timeout);
    }

    public String getAuthHeader() {
        return authHeader;
    }


}
