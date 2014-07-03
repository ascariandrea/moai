package com.seventydivision.framework.fragments;

import android.os.Bundle;

import com.seventydivision.framework.client.AsyncResourceHandler;
import com.seventydivision.framework.interfaces.OnFetchResourceInterface;
import com.seventydivision.framework.models.BaseModel;


/**
 * Created by andreaascari on 01/07/14.
 */
public abstract class InjectedResourceFragment<T extends BaseModel> extends InjectedFragment implements OnFetchResourceInterface<T> {

    private T mResource;

    private AsyncResourceHandler<T> asyncResourceHandler = new AsyncResourceHandler<T>() {
        @Override
        public void onSuccess(T res) {
            setResource(res);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchData(asyncResourceHandler);
    }


    protected void fetchData() {
        fetchData(asyncResourceHandler);
    }

    @Override
    protected void canPopulateView() {
        populateView(mResource);
    }

    protected void setResource(T resource) {
        this.mResource = resource;
        fetchCompleted(true);
    }
}
