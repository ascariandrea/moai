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

    @AfterViews
    public void afterViews() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            transaction.hide(f);
        }
        transaction.commit();
    }

    protected void addFragment(int fragmentIndex, InjectedFragment fragment) {
        Log.d(TAG, "Added fragment[" + fragment.toString() + "] at index " + fragmentIndex);
        fragments.add(fragmentIndex, fragment);
    }

    protected void setFragment(int fragmentIndex, InjectedFragment fragmentInstance) {
        fragments.set(fragmentIndex, fragmentInstance);
    }

    protected void showFragment(int fragmentIndex) {
        if (fragmentIndex == -1)
            fragmentIndex = 0;

        if (fragmentIndex >= 0 && fragmentIndex <= fragments.size() && fragments.get(fragmentIndex) != null) {
            mActiveFragmentIndex = fragmentIndex;
            FragmentTransaction t = fragmentManager.beginTransaction();
            if (fragmentManager.findFragmentById(fragments.get(fragmentIndex).getId()) == null) {
                t.add(R.id.fragmentManagerContainer, fragments.get(fragmentIndex));
            } else {
                t.show(fragments.get(fragmentIndex));
            }
            t.setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK);
            t.commit();
        } else {
            Log.d(TAG, Arrays.toString(fragments.toArray()));
        }
    }

    public void hideFragment(int fragmentIndex) {
        if (fragments.get(fragmentIndex) != null) {
            fragmentManager.beginTransaction().hide(fragments.get(fragmentIndex)).commit();
        } else {
            Log.d(TAG, "Can't hide fragment at index: " + fragmentIndex);
        }
    }

    public void replaceFragment(int index) {
        Fragment f = fragments.get(index);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentManagerContainer, f)
                .addToBackStack(f.getTag())
                .commit();
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


    protected ArrayList<Fragment> getFragments() {
        return fragments;
    }


    public void showAsDialog(int index) {
        DialogFragment f = (DialogFragment) fragments.get(index);
        f.setShowsDialog(true);
        f.show(getSupportFragmentManager(), f.getTag());
    }
}
