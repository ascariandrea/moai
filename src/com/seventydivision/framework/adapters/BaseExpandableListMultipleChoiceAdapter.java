package com.seventydivision.framework.adapters;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by andreaascari on 16/07/14.
 */
public abstract class BaseExpandableListMultipleChoiceAdapter extends BaseExpandableListAdapter {

    private static final String TAG = BaseExpandableListMultipleChoiceAdapter.class.getSimpleName();
    /**
     * Multiple choice for all the groups
     */
    public static final int CHOICE_MODE_MULTIPLE = AbsListView.CHOICE_MODE_MULTIPLE;

    public static final int CHOICE_MODE_MULTIPLE_MODAL = AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

    /**
     * No child could be selected
     */
    public static final int CHOICE_MODE_NONE = AbsListView.CHOICE_MODE_NONE;

    /**
     * One single choice per group
     */
    public static final int CHOICE_MODE_SINGLE_PER_GROUP = AbsListView.CHOICE_MODE_SINGLE;

    /**
     * One single choice for all the groups
     */
    public static final int CHOICE_MODE_SINGLE_ABSOLUTE = 10001;


    private SparseArray<SparseBooleanArray> checkedPositions = new SparseArray<SparseBooleanArray>();
    private int mChoiceMode;


    public void setSelectionType(int choiceMode) {
        this.mChoiceMode = choiceMode;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        boolean isChecked = false;
        if (checkedPositions != null && checkedPositions.get(groupPosition) != null)
            isChecked = checkedPositions.get(groupPosition).get(childPosition, false);
        return getChildChoiceView(groupPosition, childPosition, isLastChild, convertView, parent, isChecked);
    }

    protected abstract View getChildChoiceView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent, boolean isChecked);


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public void setChecked(int groupPosition, int childPosition) {
        switch (mChoiceMode) {
            case CHOICE_MODE_MULTIPLE:
                SparseBooleanArray checkedChildPositionsMultiple = checkedPositions.get(groupPosition);
                // if in the group there was not any child checked
                if (checkedChildPositionsMultiple == null) {
                    checkedChildPositionsMultiple = new SparseBooleanArray();
                    // By default, the status of a child is not checked
                    // So a click will enable it
                    checkedChildPositionsMultiple.put(childPosition, true);
                    checkedPositions.put(groupPosition, checkedChildPositionsMultiple);
                } else {
                    boolean oldState = checkedChildPositionsMultiple.get(childPosition);
                    checkedChildPositionsMultiple.put(childPosition, !oldState);
                }
                break;
            // TODO: Implement it
            case CHOICE_MODE_MULTIPLE_MODAL:
                throw new RuntimeException("The choice mode CHOICE_MODE_MULTIPLE_MODAL has not implemented yet");

            case CHOICE_MODE_NONE:
                checkedPositions.clear();
                break;
            case CHOICE_MODE_SINGLE_PER_GROUP:
                SparseBooleanArray checkedChildPositionsSingle = checkedPositions.get(groupPosition);
                // If in the group there was not any child checked
                if (checkedChildPositionsSingle == null) {
                    checkedChildPositionsSingle = new SparseBooleanArray();
                    // By default, the status of a child is not checked
                    checkedChildPositionsSingle.put(childPosition, true);
                    checkedPositions.put(groupPosition, checkedChildPositionsSingle);
                } else {
                    boolean oldState = checkedChildPositionsSingle.get(childPosition);
                    // If the old state was false, set it as the unique one which is true
                    if (!oldState) {
                        checkedChildPositionsSingle.clear();
                        checkedChildPositionsSingle.put(childPosition, !oldState);
                    } // Else does not allow the user to uncheck it
                }
                break;
            // This mode will remove all the checked positions from other groups
            // and enable just one from the selected group
            case CHOICE_MODE_SINGLE_ABSOLUTE:
                checkedPositions.clear();
                SparseBooleanArray checkedChildPositionsSingleAbsolute = new SparseBooleanArray();
                checkedChildPositionsSingleAbsolute.put(childPosition, true);
                checkedPositions.put(groupPosition, checkedChildPositionsSingleAbsolute);
                break;
        }

        // Notify that some data has been changed
        notifyDataSetChanged();
        Log.v(TAG, "List position updated");
        for(int i = 0; i < checkedPositions.size(); i++) {
            Log.v(TAG, "group " + i + " " + Arrays.asList(checkedPositions.get(i)).toString());
        }
    }

    protected int getChoiceMode() {
        return mChoiceMode;
    }

    public SparseArray<SparseBooleanArray> getSelectedPositions() {
        return checkedPositions;
    }

    public boolean isChecked(int groupPosition, int childPosition) {
        return checkedPositions.get(groupPosition).get(childPosition, false);
    }
}
