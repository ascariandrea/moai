package com.seventydivision.framework.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.seventydivision.framework.interfaces.OnInjectionCallback;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by andreaascari on 03/02/14.
 */
@EFragment
public abstract class InjectedFragment extends Fragment implements OnInjectionCallback {

    private static final String TAG = InjectedFragment.class.getSimpleName();
    private boolean mViewInjected = false;
    private boolean mFetchingCompleted = false;
    protected boolean mFetchDataIsDisabled = false;
    protected boolean mFetching = false;

    public static InjectedFragment newInstance(Bundle args) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Fragment> T newInstance(Bundle args, Class<? extends Fragment> fragmentClass) {
        try {
            Constructor<? extends Fragment> newInstanceMethod = fragmentClass.getConstructor();
            newInstanceMethod.newInstance();
            return (T) fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return (T) InjectedFragment.newInstance(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        afterCreation();
    }

    @AfterViews
    public void afterViewsInjected() {
        mViewInjected = true;
        onViewInjected();
        mObserver.update(null, true);
    }



    public void fetchCompleted(boolean completed) {
        mFetchingCompleted = completed;
        mFetching = !completed;
        mObserver.update(null, true);
    }

    public void disableFetchData() {
        mFetchDataIsDisabled = true;
    }

    public void enableFetchData() {
        mFetchDataIsDisabled = false;
    }


    public void onBackPressed() {
    }

    protected DataObserver mObserver = new DataObserver();

    protected class DataObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (mViewInjected && mFetchingCompleted) {
                canPopulateView();
            }
        }

    }

    protected void canPopulateView() {
        populateView();
    }

    protected void populateView() {}

    protected void afterCreation() {}


    public String getTitle() {
        return "";
    }

}
