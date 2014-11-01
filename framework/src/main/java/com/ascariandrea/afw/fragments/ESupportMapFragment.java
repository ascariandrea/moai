package com.ascariandrea.afw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.SupportMapFragment;

/**
 * Created by andreaascari on 28/10/14.
 */
public class ESupportMapFragment extends SupportMapFragment {

    private MapCreationListener mMapListener;
    private boolean mFirstCall = true;


    public static ESupportMapFragment newInstance(MapCreationListener listener) {
        ESupportMapFragment eSupportMapFragment =  new ESupportMapFragment();
        eSupportMapFragment.setMapCreationListener(listener);
        return eSupportMapFragment;
    }


    public void setMapCreationListener(MapCreationListener listener) {
        mMapListener = listener;
    }


    public interface MapCreationListener {
        void onMapCreated();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mMapListener != null && isFirstCall()) {
            mMapListener.onMapCreated();
            mFirstCall = false;
        }
        return view;
    }

    private boolean isFirstCall() {
        return mFirstCall;
    }
}
