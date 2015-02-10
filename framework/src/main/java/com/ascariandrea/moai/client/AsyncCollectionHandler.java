package com.ascariandrea.moai.client;

import android.util.Log;

import com.ascariandrea.moai.models.Model;
import com.ascariandrea.moai.models.ModelCollection;
import com.ascariandrea.moai.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AsyncCollectionHandler<T extends Model> extends AsyncHttpResponseHandler {
    private Class<T> mType;
    private String TAG = AsyncCollectionHandler.class.getSimpleName();
    private String wrapProperty;
    private String mCollection;

    public AsyncCollectionHandler(String colName) {
        this(colName, null);
    }

    public AsyncCollectionHandler(Class<T> type) {
        this(null, type);
    }


    public AsyncCollectionHandler(String collectionName, Class<T> type) {
        init(collectionName, type);
    }


    private void init(String collectionName, Class<T> type) {
        wrapProperty = "data";

        mType = type;

        if (collectionName == null)
            collectionName = getModelExtensionClassPluralName();

        mCollection = collectionName;

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

    private String getModelExtensionClassPluralName() {
        Class<T> klass;
        if (mType == null)
            klass = Utils.getTypeParameter(this);
        else
            klass = mType;

        try {
            Field m = klass.getDeclaredField("PLURAL_NAME");
            m.setAccessible(true);
            return (String) m.get(null);
        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        }

        return null;
    }


    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        onSuccess(new String(bytes));
    }

    public void onSuccess(String json) {
        Log.d(TAG, json);
        try {
            JSONObject jsonResponse = new JSONObject(json);
            if (wrapProperty != null) {
                json = jsonResponse.getJSONObject(wrapProperty).toString();
            }

            Log.d(TAG, getCollectionName() + "");

            onSuccess(new ModelCollection<T>(getCollectionName()).fromJSONList(json, getTypeParameterClass()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        if (bytes == null)
            onFailure(throwable, "");
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


    public abstract void onSuccess(List<T> res);

    public void onUnauthorized(Throwable t, String res) {
    }

    protected void onFailure(Throwable throwable, JSONObject jsonRes, int code) {
    }

    protected String getWrapProperty() {
        return wrapProperty;
    }

    protected String getCollectionName() { return mCollection; }
}
