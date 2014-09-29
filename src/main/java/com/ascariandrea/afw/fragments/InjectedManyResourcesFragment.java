package com.ascariandrea.afw.fragments;

import android.os.Bundle;

import com.ascariandrea.afw.client.AsyncResourceHandler;
import com.ascariandrea.afw.interfaces.OnFetchManyResourceInterface;
import com.ascariandrea.afw.models.BaseModel;

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
