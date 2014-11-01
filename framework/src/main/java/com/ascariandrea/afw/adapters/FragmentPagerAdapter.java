package com.ascariandrea.afw.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ascariandrea.afw.fragments.InjectedFragment;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Map;

/**
 * Created by andreaascari on 13/10/14.
 */
public abstract class FragmentPagerAdapter<T extends InjectedFragment> extends android.support.v4.app.FragmentPagerAdapter {


    private static InjectedFragment[] fragments;
    private final Context mContext;

    public FragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        fragments = new InjectedFragment[getCount()];
    }


    @Override
    public InjectedFragment getItem(int i) {
        return getFragment(i);
    }

    protected abstract T getFragment(int i);

    @Override
    public int getCount() {
        return fragments.length;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(getPageTitleResources(position));
    }

    protected abstract int getPageTitleResources(int position);
}
