package com.ascariandrea.moai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.ascariandrea.moai.BuildConfig;
import com.ascariandrea.moai.fragments.InjectedFragment;
import com.ascariandrea.moai.utils.Utils;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by andreaascari on 27/01/14.
 */

@EActivity
public abstract class MoaiFragmentManagerActivity extends MoaiFragmentActivity {

    public static final String FRAGMENT_TO_SHOW = "fragment-to-show";
    public static final int NON_EXISTENT_INDEX_FRAGMENT = -1;
    public static final int DEFAULT_CONTAINER_ID = 21;
    protected static final int SLIDE_RIGHT_TO_LEFT = 0;
    protected static final int SLIDE_LEFT_TO_RIGHT = 1;
    private static final String TAG = MoaiFragmentManagerActivity.class.getSimpleName();
    @Extra
    public int stepToLaunch = 0;
    protected int mActiveFragmentIndex;
    protected LinearLayout fragmentContainerView;
    private FragmentManager fragmentManager;
    private HashMap<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();
    private int mContainerId;
    private int mPreviousActiveFragmentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // presumably, not relevant
        }
    }

    @Override
    protected void onCreated() {
        fragmentManager = getSupportFragmentManager();
        setFragmentContainerId(getFragmentContainerId());

        if (stepToLaunch != -1) {
            addFragment(stepToLaunch);
            showFragment(stepToLaunch);
        }
    }

    @Override
    public void afterViews() {
        if (fragmentManager != null && fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (Fragment f : fragmentManager.getFragments()) {
                transaction.hide(f);
            }
            transaction.commitAllowingStateLoss();
        }
        onViewInjected();
    }


    protected abstract int getFragmentContainerId();

    protected void setFragmentContainerId(int containerId) {
        mContainerId = containerId;
    }

    protected void addFragment(int fragmentIndex) {
        addFragment(fragmentIndex, getFragmentAtIndex(fragmentIndex));
    }

    protected void addFragment(int fragmentIndex, InjectedFragment fragment) {
        fragments.put(fragmentIndex, fragment);
        if (fragment != null && fragmentManager != null)
            fragmentManager.beginTransaction().add(mContainerId, fragment).commitAllowingStateLoss();

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Stored fragment: " + fragment + " at index: " + fragmentIndex);
    }

    protected void setFragment(int fragmentIndex, InjectedFragment fragmentInstance) {
        if (isFragmentPresent(fragmentIndex))
            fragments.remove(fragmentIndex);

        addFragment(fragmentIndex, fragmentInstance);
    }


    protected void showFragment(int fragmentIndex) {
        showFragment(fragmentIndex, false);
    }

    protected void showFragment(int fragmentIndex, boolean b) {

        mPreviousActiveFragmentIndex = mActiveFragmentIndex;

        if (fragments.containsKey(fragmentIndex) && fragments.get(fragmentIndex) != null) {
            FragmentTransaction t = fragmentManager.beginTransaction();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Fragment to hide: " + fragments.get(mActiveFragmentIndex));
                Log.d(TAG, "Fragment to show: " + fragments.get(fragmentIndex));
            }

            Utils.Views.hideKeyBoard(this, getWindow().getDecorView());

            if (fragments.get(mActiveFragmentIndex) != null)
                t.hide(fragments.get(mActiveFragmentIndex));

            t.show(fragments.get(fragmentIndex));
            if (b)
                t.addToBackStack(null);
            t.commitAllowingStateLoss();
            mActiveFragmentIndex = fragmentIndex;
        } else {
            throw new RuntimeException("Check passed index to show [fragment " + fragmentIndex + "]");
        }
    }

    public void showNextFragment(boolean addToBackStack) {
        int nextFragmentIndex = getCurrentActiveIndex() + 1;
        InjectedFragment f = getFragmentAtIndex(nextFragmentIndex);
        if (!fragments.containsKey(nextFragmentIndex))
            addFragment(nextFragmentIndex, f);

        showFragment(nextFragmentIndex, addToBackStack);

    }

    protected abstract <F extends InjectedFragment> F getFragmentAtIndex(int nextFragmentIndex);


    public void hideFragment(int fragmentIndex) {
        if (fragments.get(fragmentIndex) != null) {
            fragmentManager.beginTransaction().hide(fragments.get(fragmentIndex)).commitAllowingStateLoss();
            mActiveFragmentIndex = mPreviousActiveFragmentIndex;
            mPreviousActiveFragmentIndex = fragmentIndex;
        } else {
            Log.d(TAG, "Can't hide fragment at index: " + fragmentIndex);
        }
    }

    public void replaceFragment(int index, boolean addToBackStack) {
        mPreviousActiveFragmentIndex = mActiveFragmentIndex;
        Fragment f = fragments.get(index);
        Log.d(TAG, "Fragment replace with: " + f.toString());
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(mContainerId, f);
        if (addToBackStack)
            t.addToBackStack(f.getTag());
        t.commitAllowingStateLoss();
        mActiveFragmentIndex = index;
    }


    protected void removeFragment(int fragmentIndex) {
        if (fragments.get(fragmentIndex) != null) {
            fragmentManager.beginTransaction().remove(fragments.get(fragmentIndex)).commitAllowingStateLoss();
            fragments.put(fragmentIndex, null);
        }
    }


    public int getCurrentActiveIndex() {
        return mActiveFragmentIndex;
    }

    protected boolean isFragmentPresent(int productIndex) {
        return fragments.size() > productIndex && fragments.get(productIndex) != null;
    }


    protected ArrayList<Fragment> getFragments() {
        return new ArrayList<Fragment>(fragments.values());
    }

    protected Fragment getFragmentInstance(int index) {
        return getFragments().get(index);
    }


    public void showAsDialog(int index) {
        DialogFragment f = (DialogFragment) fragments.get(index);
        f.setShowsDialog(true);
        f.show(getSupportFragmentManager(), f.getTag());
    }

    @Override
    public void onBackPressed() {
        if (fragments.get(getCurrentActiveIndex()) != null) {
            if (!((InjectedFragment) fragments.get(getCurrentActiveIndex())).onBackPressed()) {
                super.onBackPressed();
                mActiveFragmentIndex = mPreviousActiveFragmentIndex;
            }
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragments.get(getCurrentActiveIndex()) != null)
            fragments.get(getCurrentActiveIndex()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        System.gc();
        super.onStop();
    }

    @Override
    protected void onPause() {
        System.gc();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        fragments = null;
        fragmentManager = null;
        System.gc();
        super.onDestroy();
    }

}
