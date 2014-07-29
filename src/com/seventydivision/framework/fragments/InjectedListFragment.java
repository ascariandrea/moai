package com.seventydivision.framework.fragments;

import android.os.Bundle;
import android.util.Log;


import com.seventydivision.framework.client.AsyncCollectionHandler;
import com.seventydivision.framework.interfaces.OnFetchCollectionInterface;
import com.seventydivision.framework.models.BaseModel;
import com.seventydivision.framework.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andreaascari on 01/07/14.
 */

public abstract class InjectedListFragment<T extends BaseModel> extends InjectedFragment implements OnFetchCollectionInterface<T> {

    protected static final String TAG = InjectedListFragment.class.getSimpleName();

    private List<T> mCollection;

    private AsyncCollectionHandler<T> asyncCollectionHandler;
    private Class<? extends BaseModel> mExtendedBaseModelClass;

    private boolean mNeedRepopulate = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
        if (!mFetchDataIsDisabled)
            fetchData();
    }

    @SuppressWarnings("unchecked")
    private void initHandler() {
        asyncCollectionHandler = new AsyncCollectionHandler<T>(getBaseModelExtensionClassPluralName(), (Class<T>) Utils.getTypeParameter(this)) {
            @Override
            public void onSuccess(List<T> res) {
                mCollection = res;
                fetchCompleted(true);
            }
        };
    }


    private String getBaseModelExtensionClassPluralName() {
        Class<T> klass = Utils.getTypeParameter(this);
        try {
            Field m = klass.getDeclaredField("PLURAL_NAME");
            m.setAccessible(true);
            return (String) m.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


    protected void fetchData() {
        fetchData(asyncCollectionHandler);
    }

    @Override
    protected void canPopulateView() {
        if (mCollection != null)
            populateView(mCollection);
        else
            throw new RuntimeException("Can't call populateView(mCollection) with null collection.");
    }


    protected void setModel(Class<? extends BaseModel> extendedBaseModel) {
        mExtendedBaseModelClass = extendedBaseModel;
    }

    @Override
    public void onDestroyView() {
        mNeedRepopulate = true;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mCollection = null;
        mNeedRepopulate = true;
        super.onDestroy();
    }

    @Override
    public void onResume() {

        if (mCollection == null)
            fetchData();
        else if (mNeedRepopulate)
            populateViewAgain();
        super.onResume();
    }

    protected void populateViewAgain() {
        populateView(mCollection);
    }


}
