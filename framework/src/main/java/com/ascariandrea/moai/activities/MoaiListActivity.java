package com.ascariandrea.moai.activities;

import android.app.ListActivity;

import com.ascariandrea.moai.client.AsyncCollectionHandler;
import com.ascariandrea.moai.interfaces.OnFetchCollectionInterface;
import com.ascariandrea.moai.models.Model;
import com.ascariandrea.moai.utils.Utils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andreaascari on 07/11/14.
 */
@EActivity
public abstract class MoaiListActivity<T extends Model> extends ListActivity implements OnFetchCollectionInterface<T> {

    protected List<T> mCollection;

    private boolean mNeedRepopulate = false;
    private boolean mFetchDataIsDisabled = false;
    private boolean mViewInjected = false;
    protected boolean mFetching;
    private boolean mFetchingCompleted;
    private AsyncCollectionHandler<T> asyncCollectionHandler;
    private DataObserver mObserver;
    private Class<? extends Model> mExtendedModelClass;

    @AfterInject
    protected void afterCreation() {
        toCallAfterCreation();
    }


    protected void toCallAfterCreation() {
        onCreated();
        initHandler();
        if (!mFetchDataIsDisabled)
            fetchData();
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


    private void onCreated() {

    }


    protected void fetchData() {
        mFetching = true;
        fetchData(asyncCollectionHandler);
    }

    @AfterViews
    public void afterViewsInjected() {
        mViewInjected = true;
        onViewInjected();
        mObserver.update(null, true);
    }

    protected abstract void onViewInjected();


    public void fetchCompleted(boolean completed) {
        mFetchingCompleted = completed;
        mFetching = !completed;
        mObserver.update(null, true);
    }

    protected void canPopulateView() {
        if (mCollection != null)
            populateView(mCollection);
        else
            throw new RuntimeException("Can't call populateView(mCollection) with null collection.");
    }


    protected void setModel(Class<? extends Model> extendedModel) {
        mExtendedModelClass = extendedModel;
    }


    @Override
    public void onDestroy() {
        mCollection = null;
        mNeedRepopulate = true;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (mCollection == null && !mFetching)
            fetchData();
        else if (mNeedRepopulate)
            populateViewAgain();
        super.onResume();
    }

    protected void populateViewAgain() {
        populateView(mCollection);
    }


    @Override
    public void fetchData(AsyncCollectionHandler<T> asyncCollectionHandler) {

    }

    public void populateView(List<T> list) {

    }

    protected class DataObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (mViewInjected && mFetchingCompleted) {
                canPopulateView();
            }
        }

    }
}
