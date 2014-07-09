package com.seventydivision.framework.fragments;

import android.os.Bundle;

import com.seventydivision.framework.client.AsyncResourceHandler;
import com.seventydivision.framework.interfaces.OnFetchManyResourceInterface;
import com.seventydivision.framework.models.BaseModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andreaascari on 01/07/14.
 */
public abstract class InjectedManyResourcesFragment extends InjectedFragment implements OnFetchManyResourceInterface {

    private Class<? extends BaseModel> mResource;

    private AsyncResourceHandler<? extends BaseModel> asyncResourceHandler;
    private List<Class<? extends BaseModel>> mResources = new ArrayList<Class<? extends BaseModel>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Class<? extends BaseModel> model : mResources) {

        }
    }

    @Override
    public void addResourceToFetch(Class<? extends BaseModel> model) {
        mResources.add(model);
    }
}
