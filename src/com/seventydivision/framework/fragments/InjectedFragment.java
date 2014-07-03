package com.seventydivision.framework.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.seventydivision.framework.interfaces.OnInjectionCallback;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;

/**
 * Created by andreaascari on 03/02/14.
 */
public abstract class InjectedFragment extends Fragment implements OnInjectionCallback {

    private static final String TAG = InjectedFragment.class.getSimpleName();
    protected int mLayout;
    private boolean mViewInjected = false;
    private boolean mFetchingCompleted = false;
    private Class<? extends Class> mObjectClass;
    protected boolean mFetchDataIsDisabled = false;
    protected boolean mFetching;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        Log.d(TAG, getResources().getResourceName(mLayout));
        if (mLayout != 0) {
            view = inflater.inflate(mLayout, container, false);
            injectView(view);
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }

        return view;
    }

    protected void setLayoutToInject(int resId) {
        if (resId != 0) {
            mLayout = resId;
        }
    }

    protected int getLayoutToInject() {
        return mLayout;
    }


    protected void injectView(View view) {

        if (view == null && getView() == null)
            return;
        if (view != null)
            ButterKnife.inject(this, view);
        else
            ButterKnife.inject(this, getView());

        onViewInjected();

        mViewInjected = true;
        mObserver.update(null, true);
    }

    @Override
    public void onViewInjected() {}


    protected boolean isInjected() {
        return mViewInjected;
    }


    public void fetchCompleted(boolean completed) {
        mFetchingCompleted = completed;
        mFetching = completed;
        mObserver.update(null, mFetchingCompleted);
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


    public String getTitle() {
        return "";
    }

}
