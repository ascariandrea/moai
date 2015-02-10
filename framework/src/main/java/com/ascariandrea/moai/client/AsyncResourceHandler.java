package com.ascariandrea.moai.client;

import android.util.Log;

import com.ascariandrea.moai.models.Model;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AsyncResourceHandler<T extends Model> extends AsyncHttpResponseHandler {

    private String TAG = AsyncResourceHandler.class.getSimpleName();
    private String mWrapProperty;
    protected Class<T> mType;


    public AsyncResourceHandler() {
        mWrapProperty = "data";
    }

    public AsyncResourceHandler(String wp) {
        mWrapProperty = wp;
    }

    public AsyncResourceHandler(Class<T> type) {
        mWrapProperty = "data";
        mType = type;
    }

    protected void setmWrapProperty(String wrapProperty) {
        mWrapProperty = wrapProperty;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getTypeParameterClass() {
        if (mType != null) return mType;

        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }

    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        onSuccess(new String(bytes));
    }

    public final void onSuccess(String json) {
        if (json != null) Log.d(TAG, json);

        try {
            JSONObject jsonResponse = new JSONObject(json);
            if (mWrapProperty != null) {
                json = jsonResponse.getJSONObject(mWrapProperty).toString();
            }
            onSuccess(Model.fromJSON(json, getTypeParameterClass()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        if (bytes == null)
            onFailure(throwable, new String());
        else {
            String res = new String(bytes);
            onFailure(throwable, res);
            Log.d("Throwable", throwable.getMessage());

            try {

                String message = "";
                int code = 500;
                JSONArray errors = new JSONArray();
                if (!res.isEmpty()) {
                    JSONObject jsonRes = new JSONObject(res);


                    if (jsonRes.has("code") && !jsonRes.getString("code").isEmpty())
                        code = Integer.parseInt(jsonRes.getString("code"));
                    if (jsonRes.has("message"))
                        message = jsonRes.getString("message");

                    errors = new JSONArray();
                    if (jsonRes.has("errors"))
                        errors = jsonRes.getJSONArray("errors");
                }

                Log.d(TAG, "code: " + i);

                switch (i) {
                    case 404:
                        onNotFound(throwable, message, errors, code);
                        break;
                    case 401:
                        onNotAuthorized(throwable, message, errors, code);
                        break;
                    case 403:
                        onForbidden(throwable, res);
                    default:
                        onFailure(throwable, message, errors, code);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void onFailure(Throwable t, String res) {
        Log.d(TAG, t.getStackTrace().toString());
        t.printStackTrace();
        if (res != null) {
            Log.i(TAG, res);
        }
    }

    protected void onNotAuthorized(Throwable throwable, String message, JSONArray errors, int code) {
    }

    protected void onForbidden(Throwable throwable, String res) {
    }

    public void onNotFound(Throwable throwable, String message, JSONArray errors, int code) {
    }

    protected void onFailure(Throwable throwable, String errorMessage, JSONArray errors, int apiCode) {
    }


    public abstract void onSuccess(T res);


}
