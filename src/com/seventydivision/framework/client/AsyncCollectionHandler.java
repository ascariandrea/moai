package com.seventydivision.framework.client;

import android.util.Log;

import com.seventydivision.framework.BuildConfig;
import com.seventydivision.framework.models.BaseModel;
import com.seventydivision.framework.models.ModelCollection;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AsyncCollectionHandler<T extends BaseModel> extends AsyncHttpResponseHandler {
    private Class<T> mType;
    private String TAG = AsyncCollectionHandler.class.getSimpleName();
    private String wrapProperty;
    private String collection;

    public AsyncCollectionHandler(String colName) {
        wrapProperty = "data";
        collection = colName;
        Log.d(TAG, "ColName: " + colName);
    }

    public AsyncCollectionHandler(String colName, Class<T> type) {
        this(colName);
        mType = type;
    }


    @Override
    public void onStart() {
    }


    @SuppressWarnings("unchecked")
    public Class<T> getTypeParameterClass() {
        if (mType != null)
            return mType;

        Class klass = getClass();
        Type type = klass.getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }


    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        onSuccess(new String(bytes));
    }

    public final void onSuccess(String json) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, json);

        try {
            JSONObject jsonResponse = new JSONObject(json);
            if (wrapProperty != null) {
                json = jsonResponse.getJSONObject(wrapProperty).toString();
            }
            onSuccess(new ModelCollection<T>(collection).fromJSONList(json, getTypeParameterClass()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        onFailure(throwable, new String(bytes));
    }

    public void onFailure(Throwable t, String res) {
        if (res != null)
            Log.i(TAG, res);

        try {
            HttpResponseException httpResponseException = (HttpResponseException) t;
            int statusCode = httpResponseException.getStatusCode();
            switch (statusCode) {
                case 403: {
                    onUnauthorized(t, res);
                }
                default:
                    break;
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    public void onUnauthorized(Throwable t, String res) {}

    public abstract void onSuccess(List<T> res);
}
