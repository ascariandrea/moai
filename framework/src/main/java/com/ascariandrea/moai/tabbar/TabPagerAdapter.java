package com.ascariandrea.moai.tabbar;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by andreaascari on 27/10/14.
 */
public abstract class TabPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    protected int[] mIcons;
    protected String[] mFragmentsTitles;


    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public int getIconResId(int index) {
        return getIcons()[index % getIcons().length];

    }

    @Override
    public int getCount() {
        return getIcons().length;
    }

    public int[] getIcons() {
        return mIcons;
    }


}
