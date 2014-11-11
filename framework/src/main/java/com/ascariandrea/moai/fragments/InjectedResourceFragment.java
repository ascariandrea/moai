package com.ascariandrea.moai.fragments;

import com.ascariandrea.moai.client.AsyncResourceHandler;
import com.ascariandrea.moai.interfaces.OnFetchResourceInterface;
import com.ascariandrea.moai.models.Model;
import com.ascariandrea.moai.utils.Utils;

import org.androidannotations.annotations.EFragment;
import org.json.JSONArray;


/**
 * Created by andreaascari on 01/07/14.
 */
@EFragment
public abstract class InjectedResourceFragment<T extends Model> extends InjectedFragment implements OnFetchResourceInterface<T> {

    protected T mResource;

    private AsyncResourceHandler<T> asyncResourceHandler;
    private boolean mNeedRepopulate = false;


    @Override
    protected void toCallAfterCreation() {
        onCreated();
        initHandler();
        if (!mFetchDataIsDisabled)
            fetchData();
    }

    protected void onCreated() {
    }


    protected void fetchData() {
        mFetching = true;
        if (mResource == null)
            fetchData(asyncResourceHandler);
    }

    @SuppressWarnings("unchecked")
    protected void initHandler() {
        asyncResourceHandler = new AsyncResourceHandler<T>((Class<T>) Utils.getTypeParameter(this)) {

            @Override
            public void onSuccess(T res) {
                setResource(res);
            }


            @Override
            protected void onFailure(Throwable throwable, String errorMessage, JSONArray errors, int apiCode) {
                Utils.Views.showLongToast(getActivity(), errorMessage);
            }

            @Override
            public void onFailure(Throwable t, String res) {
                super.onFailure(t, res);
            }
        };
    }

    @Override
    public void afterViewsInjected() {
        super.afterViewsInjected();
    }

    @Override
    public boolean isFetching() {
        return mFetching;
    }

    @Override
    protected void canPopulateView() {
        if (getActivity() != null && mResource != null)
            populateView(mResource);
    }

    protected void setResource(T resource) {
        this.mResource = resource;
        fetchCompleted(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        invalidateResource();
        mNeedRepopulate = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mResource == null && !isFetching() && !mFetchDataIsDisabled)
            fetchData();
        else if (mNeedRepopulate && getActivity() != null)
            populateViewAgain();
    }

    protected void invalidateResource() {
        this.mResource = null;
    }

    private void populateViewAgain() {
        populateView(mResource);
    }

}
