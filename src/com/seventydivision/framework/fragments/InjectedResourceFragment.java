package com.seventydivision.framework.fragments;

import android.os.Bundle;

import com.seventydivision.framework.client.AsyncResourceHandler;
import com.seventydivision.framework.interfaces.OnFetchResourceInterface;
import com.seventydivision.framework.models.BaseModel;
import com.seventydivision.framework.utils.Utils;

import org.androidannotations.annotations.AfterInject;


/**
 * Created by andreaascari on 01/07/14.
 */
public abstract class InjectedResourceFragment<T extends BaseModel> extends InjectedFragment implements OnFetchResourceInterface<T> {

    private T mResource;

    private AsyncResourceHandler<T> asyncResourceHandler;
    private boolean mNeedRepopulate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
        fetchData();
    }

    @SuppressWarnings("unchecked")
    protected void initHandler() {
        asyncResourceHandler = new AsyncResourceHandler<T>((Class<T>) Utils.getTypeParameter(this)) {

            @Override
            public void onSuccess(T res) {
                setResource(res);
            }

        };
    }

    @Override
    public void afterViewsInjected() {
        super.afterViewsInjected();
    }

    protected void fetchData() {
        fetchData(asyncResourceHandler);
    }

    @Override
    public boolean isFetching() {
        return mFetching;
    }

    @Override
    protected void canPopulateView() {
        populateView(mResource);
    }

    protected void setResource(T resource) {
        this.mResource = resource;
        fetchCompleted(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNeedRepopulate = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mResource == null)
            fetchData();
        else if (mNeedRepopulate)
            populateViewAgain();
    }

    private void populateViewAgain() {
        populateView(mResource);
    }
}
