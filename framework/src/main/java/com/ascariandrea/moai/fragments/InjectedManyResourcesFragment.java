package com.ascariandrea.moai.fragments;

import android.os.Bundle;

import com.ascariandrea.moai.client.AsyncResourceHandler;
import com.ascariandrea.moai.interfaces.OnFetchManyResourceInterface;
import com.ascariandrea.moai.models.Model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andreaascari on 01/07/14.
 */
public abstract class InjectedManyResourcesFragment extends InjectedFragment implements OnFetchManyResourceInterface {

    private Class<? extends Model> mResource;

    private AsyncResourceHandler<? extends Model> asyncResourceHandler;
    private List<Class<? extends Model>> mResources = new ArrayList<Class<? extends Model>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Class<? extends Model> model : mResources) {

        }
    }

    @Override
    public void addResourceToFetch(Class<? extends Model> model) {
        mResources.add(model);
    }
}
