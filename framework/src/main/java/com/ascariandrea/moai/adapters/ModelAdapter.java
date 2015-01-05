package com.ascariandrea.moai.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ascariandrea.moai.models.Model;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by andreaascari on 09/07/14.
 */
@EBean
public class ModelAdapter<T extends Model> extends BaseAdapter {


    @RootContext
    public Context context;

    private List<T> mModels;

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
        return mModels.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


}
