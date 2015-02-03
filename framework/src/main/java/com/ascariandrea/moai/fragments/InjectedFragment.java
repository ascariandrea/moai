package com.ascariandrea.moai.fragments;

import android.support.v4.app.Fragment;

import com.ascariandrea.moai.interfaces.OnInjectionCallback;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by andreaascari on 03/02/14.
 */
@EFragment
public abstract class InjectedFragment extends Fragment implements OnInjectionCallback {

    private static final String TAG = InjectedFragment.class.getSimpleName();
    protected boolean mFetchDataIsDisabled = false;
    protected boolean mFetching = false;
    protected DataObserver mObserver = new DataObserver();
    private boolean mViewInjected = false;
    private boolean mFetchingCompleted = false;
    private boolean mFirstBackPress = true;


    @AfterInject
    protected void afterCreation() {
        toCallAfterCreation();
    }

    protected void toCallAfterCreation() {
        onCreated();
    }

    protected void onCreated() {

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

    public boolean onBackPressed() {
        if (isFirstBackPress()) {
            mFirstBackPress = false;
            return onFirstBackPressed();
        } else {
            return onAnotherBackPressed();
        }
    }

    public boolean onAnotherBackPressed() {
        return false;
    }

    public boolean onFirstBackPressed() {
        return false;
    }

    public boolean isFirstBackPress() {
        return mFirstBackPress;
    }

    protected void canPopulateView() {
        populateView();
    }

    protected void populateView() {}

    protected class DataObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (mViewInjected && mFetchingCompleted) {
                canPopulateView();
            }
        }

    }


    protected final boolean hasViewInjected() {
        return mViewInjected;
    }

}
