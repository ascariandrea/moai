package com.ascariandrea.moai.adapters;

import android.support.v4.app.*;
import android.util.SparseArray;

import com.ascariandrea.moai.fragments.InjectedFragment;

/**
 * Created by andreaascari on 10/11/14.
 */
public class DynamicFragmentPager extends android.support.v4.app.FragmentPagerAdapter {


    private SparseArray<InjectedFragment> mFragments;
    private SparseArray<String> mFragmentsTitles;

    public DynamicFragmentPager(FragmentManager fm) {
        super(fm);
        mFragments = new SparseArray<InjectedFragment>();
        mFragmentsTitles = new SparseArray<String>();

    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentsTitles.size();
    }

    public void addPage(int i, String pageTitle, InjectedFragment fragment) {
        mFragments.put(i, fragment);
        mFragmentsTitles.put(i, pageTitle);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsTitles.get(position);
    }
}
