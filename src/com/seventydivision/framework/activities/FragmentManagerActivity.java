package com.seventydivision.framework.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.seventydivision.framework.R;
import com.seventydivision.framework.fragments.InjectedFragment;
import com.seventydivision.framework.interfaces.FragmentsCount;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by andreaascari on 27/01/14.
 */

@EActivity
public abstract class FragmentManagerActivity extends MainActivity {

    public static final String FRAGMENT_TO_SHOW = "fragment-to-show";

    protected static final int SLIDE_RIGHT_TO_LEFT = 0;
    protected static final int SLIDE_LEFT_TO_RIGHT = 1;

    public static final int NON_EXISTENT_INDEX_FRAGMENT = -1;

    private static final String TAG = FragmentManagerActivity.class.getSimpleName();


    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private int mContainerId;
    private int mPreviousActiveFragmentIndex;
    protected int mActiveFragmentIndex;

    protected LinearLayout fragmentContainerView;


    public FragmentManagerActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void afterViews() {
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (Fragment f : fragmentManager.getFragments()) {
                transaction.hide(f);
            }
            transaction.commit();
        }
        onViewInjected();
    }


    protected void setContainerId(int containerId) {
        mContainerId = containerId;
    }

    protected void addFragment(int fragmentIndex, InjectedFragment fragment) {
        fragments.add(fragmentIndex, fragment);
        if (fragment != null)
            fragmentManager.beginTransaction().add(mContainerId, fragment).commit();
    }

    protected void setFragment(int fragmentIndex, InjectedFragment fragmentInstance) {
        if (isFragmentPresent(fragmentIndex))
            fragments.remove(fragmentIndex);

        addFragment(fragmentIndex, fragmentInstance);
    }


    protected void showFragment(int productIndex) {
        showFragment(productIndex, false);
    }

    protected void showFragment(int fragmentIndex, boolean b) {
        if (fragmentIndex == -1)
            fragmentIndex = 0;

        mPreviousActiveFragmentIndex = mActiveFragmentIndex;

        Log.d(TAG, Arrays.toString(fragments.toArray()));

        if (fragmentIndex >= 0 && fragmentIndex <= fragments.size() && fragments.get(fragmentIndex) != null) {
            FragmentTransaction t = fragmentManager.beginTransaction();
            Log.d(TAG, "Fragment to show: " + fragments.get(fragmentIndex));
            t.hide(fragments.get(mActiveFragmentIndex));
            t.show(fragments.get(fragmentIndex));
            if (b)
                t.addToBackStack(null);
            t.commit();
            mActiveFragmentIndex = fragmentIndex;
        } else {
            Log.d(TAG, Arrays.toString(fragments.toArray()));
        }
    }

    protected void toggleFragment(int index, boolean addToBackStack) {

    }

    public void hideFragment(int fragmentIndex) {
        if (fragments.get(fragmentIndex) != null) {
            fragmentManager.beginTransaction().hide(fragments.get(fragmentIndex)).commit();
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
        t.commit();
        mActiveFragmentIndex = index;
    }


    protected void removeFragment(int fragmentIndex) {
        if (fragments.get(fragmentIndex) != null) {
            fragmentManager.beginTransaction().remove(fragments.get(fragmentIndex)).commit();
            fragments.set(fragmentIndex, null);
        }
    }


    public int getCurrentActiveIndex() {
        return mActiveFragmentIndex;
    }

    protected boolean isFragmentPresent(int productIndex) {

        return fragments.size() > productIndex && fragments.get(productIndex) != null;
    }


    protected ArrayList<Fragment> getFragments() {
        return fragments;
    }


    public void showAsDialog(int index) {
        DialogFragment f = (DialogFragment) fragments.get(index);
        f.setShowsDialog(true);
        f.show(getSupportFragmentManager(), f.getTag());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mActiveFragmentIndex = mPreviousActiveFragmentIndex;
    }
}
