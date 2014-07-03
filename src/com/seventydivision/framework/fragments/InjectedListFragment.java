package com.seventydivision.framework.fragments;

import android.os.Bundle;


import com.seventydivision.framework.client.AsyncCollectionHandler;
import com.seventydivision.framework.interfaces.OnFetchCollectionInterface;
import com.seventydivision.framework.models.BaseModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.List;

/**
 * Created by andreaascari on 01/07/14.
 */
public abstract class InjectedListFragment<T extends BaseModel> extends InjectedFragment implements OnFetchCollectionInterface<T> {

    protected static final String TAG = InjectedListFragment.class.getSimpleName();

    private List<T> mCollection;

    private AsyncCollectionHandler<T> asyncCollectionHandler = new AsyncCollectionHandler<T>(getBaseModelExtensionClassPluralName(), getBaseModelExtensionClass()) {
        @Override
        public void onSuccess(List<T> res) {
            mCollection = res;
            fetchCompleted(true);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
        if (!mFetchDataIsDisabled)
            fetchData(asyncCollectionHandler);

    }

    private void initHandler() {
        getBaseModelExtensionClass();
    }

    @SuppressWarnings("unchecked")
    public Class<T> getBaseModelExtensionClass() {
        Class klass = ((Object) this).getClass();
        Type type = klass.getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        Type[] actualTypes = paramType.getActualTypeArguments();
        return (Class<T>) actualTypes[0];
    }

    private String getBaseModelExtensionClassPluralName() {
        Class<T> klass = getBaseModelExtensionClass();
        try {
            Field m = klass.getDeclaredField("PLURAL_NAME");
            m.setAccessible(true);
            return (String) m.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }



    protected void fetchData() {
        fetchData(asyncCollectionHandler);
    }

    @Override
    protected void canPopulateView() {
        if (mCollection != null)
            populateView(mCollection);
        else
            throw new RuntimeException("Can't call populateView(mCollection) with null collection.");
    }

}
