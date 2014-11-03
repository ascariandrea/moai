package com.ascariandrea.moai.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ascariandrea.moai.models.Model;

/**
 * Created by andreaascari on 09/07/14.
 */
public class ModelAdapter<T extends Model> extends BaseAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Class<T> getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
