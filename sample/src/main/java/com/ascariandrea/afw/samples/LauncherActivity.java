package com.ascariandrea.afw.samples;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ascariandrea.afw.samples.login.LoginActivity_;

import org.androidannotations.annotations.EActivity;

@EActivity
public class LauncherActivity extends ListActivity {


    private final String[] mSamples = { "LoginActivity" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mSamples));
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        switch (position) {
            case 0:
                LoginActivity_.intent(this).start();
            default:
                LoginActivity_.intent(this).start();
        }

    }
}
