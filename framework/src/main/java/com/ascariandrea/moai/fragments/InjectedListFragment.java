package com.ascariandrea.moai.fragments;


import android.util.Log;

import com.ascariandrea.moai.BuildConfig;
import com.ascariandrea.moai.client.AsyncCollectionHandler;
import com.ascariandrea.moai.interfaces.OnFetchCollectionInterface;
import com.ascariandrea.moai.models.Model;
import com.ascariandrea.moai.utils.Utils;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * Created by andreaascari on 01/07/14.
 */
@EFragment
public abstract class InjectedListFragment<T extends Model> extends InjectedFragment implements OnFetchCollectionInterface<T> {

    protected static final String TAG = InjectedListFragment.class.getSimpleName();

    protected List<T> mCollection;

    protected AsyncCollectionHandler<T> asyncCollectionHandler;
    private Class<? extends Model> mExtendedModelClass;

    private boolean mNeedRepopulate = false;
    private boolean mIsVisibleToUser;


    @Override
    protected void toCallAfterCreation() {
        onCreated();
        initHandler();
    }


    @SuppressWarnings("unchecked")
    protected void initHandler() {
        asyncCollectionHandler = new AsyncCollectionHandler<T>((Class<T>) Utils.getTypeParameter(this)) {
            @Override
            public void onSuccess(List<T> res) {
                mCollection = res;
                fetchCompleted(true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mFetching = false;
            }
        };
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed() && mIsVisibleToUser) {
            fetchDataIfNeeded();
            populateViewIfNeeded();
        }
    }

    protected void fetchData() {
        mFetching = true;
        fetchData(asyncCollectionHandler);
    }

    @Override
    protected void canPopulateView() {
        Log.d(TAG, "call canPopulateView()");
        if (mCollection != null) {
            populateView(mCollection);
        }
        else {
            Log.d(TAG, "Can't call populateView(mCollection) with null collection.");
        }
    }


    protected void setModel(Class<? extends Model> extendedModel) {
        mExtendedModelClass = extendedModel;
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
        if (getUserVisibleHint()) {
            mIsVisibleToUser = true;
        }

        if (!hasViewInjected())
            afterViewsInjected();

        fetchDataIfNeeded();
        populateViewIfNeeded();

        super.onResume();
    }


    public void fetchDataIfNeeded() {
        if (mCollection == null && !mFetching && !mFetchDataIsDisabled && mIsVisibleToUser) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "A fetch data is needed!");
            }
            if (getAsyncCollectionHandler() == null)
                initHandler();
            fetchData();
        }
    }

    private void populateViewIfNeeded() {
        if (mCollection != null && !mFetching && mNeedRepopulate && mIsVisibleToUser) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "A populate view is needed!");
            }
            populateViewAgain();
        }
    }

    protected void populateViewAgain() {
        populateView(mCollection);
    }


    public AsyncCollectionHandler<T> getAsyncCollectionHandler() {
        return asyncCollectionHandler;
    }
}
