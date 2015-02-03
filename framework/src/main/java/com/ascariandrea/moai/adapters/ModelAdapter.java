package com.ascariandrea.moai.adapters;

import android.content.Context;
import android.widget.BaseAdapter;

import com.ascariandrea.moai.models.Model;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by andreaascari on 09/07/14.
 */
@EBean
public abstract class ModelAdapter<T extends Model> extends BaseAdapter {


    @RootContext
    public Context context;

    protected List<T> mModels;

    protected final Context getContext() {
        return context;
    }


    public void init(List<T> list) {
        mModels = list;
        notifyDataSetInvalidated();
    }

    public void append(List<T> list) {
        mModels.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mModels != null) {
            mModels.clear();
        }
        notifyDataSetInvalidated();
    }

    public List<T> getList() {
        return mModels;
    }

    @Override
    public int getCount() {
        return mModels != null ? mModels.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mModels.get(position).hashCode();
    }



}
