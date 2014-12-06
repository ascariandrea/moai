package com.ascariandrea.moai.tabbar;

/**
 * Created by andreaascari on 27/10/14.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascariandrea.moai.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;


import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by andreaascari on 06/11/13.
 */
public class TabBarIndicator extends HorizontalScrollView implements PageIndicator {

    /**
     * Title text used when no title is provided by the adapter.
     */
    private static final CharSequence EMPTY_TITLE = "";
    private String TAG = TabBarIndicator.class.getSimpleName();
    public static int EQUAL_TABS_LAYOUT = 0;
    public static int CENTERED_SPACED_TABS_LAYOUT = 1;
    private boolean mEqualTabs = true;
    private int mTabLayoutResource;


    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private TabView mLastTabViewSelected = null;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final LinearLayout mTabLayout;

    private ViewPager mViewPager;

    private ViewPager mDummyViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private TabPagerAdapter mTabPagerAdapter;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;

    public TabBarIndicator(Context context) {
        this(context, null);

    }

    public TabBarIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);


        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TabBarIndicator, 0, 0);

        try {

            mTabLayoutResource = a.getResourceId(R.styleable.TabBarIndicator_tabLayout, R.layout.tab_view);
            mTabLayout = new LinearLayout(context, attrs, R.attr.tabBarIndicatorStyle);
            addView(mTabLayout, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        } finally {
            a.recycle();
        }


    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }


    private void addTab(int index, CharSequence text, int iconResId) {

        LinearLayout tabContainer = new LinearLayout(getContext(), null, getAttrs(index));
        final TabView tabView = (TabView) ((LinearLayout) inflate(getContext(), mTabLayoutResource, tabContainer)).getChildAt(0);

        tabView.setIndex(index);
        tabView.setMaxTabWith(mMaxTabWidth);
        tabView.setFocusable(true);
        tabView.setClickable(true);
        tabView.setOnClickListener(mTabClickListener);


        ImageView tabIconView = (ImageView) tabView.findViewById(R.id.tabIcon);
        if (tabIconView != null && iconResId != 0) {
            tabIconView.setImageDrawable(getResources().getDrawable(iconResId));
            tabIconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            tabIconView.setClickable(false);
            tabView.invalidate();
        }

        TextView tabTextView = (TextView) tabView.findViewById(R.id.tabLabel);
        if (tabTextView != null) {
            tabTextView.setText(text);
        }


        mTabLayout.addView(tabContainer, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1));
    }

    public void setLayoutType(int type) {
        if (type == EQUAL_TABS_LAYOUT) {
            mEqualTabs = true;
        } else {
            mEqualTabs = false;
        }
    }

    private int getAttrs(int index) {
        int style;
        if (!mEqualTabs) {
            switch (index) {
                case 1:
                    style = R.attr.centerLeftTab;
                    break;
                case 2:
                    style = R.attr.centerRightTab;
                    break;
                default:
                    style = R.attr.simpleTabStyle;
                    break;
            }
        } else {
            style = R.attr.simpleTabStyle;
        }

        return style;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        unsetCurrentItem();
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }

    }

    @Override
    public void setViewPager(ViewPager view) {

        if (mViewPager == view) {
            Log.d(TAG, "the view pager set is the view itself.");
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        mTabPagerAdapter = (TabPagerAdapter) mViewPager.getAdapter();
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }


            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        checkViewPager();
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final LinearLayout child = (LinearLayout) mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                child.setPressed(true);
                child.setEnabled(true);
                child.invalidate();
                animateToTab(item);
            }
        }
    }


    public void unsetCurrentItem() {
        checkViewPager();
        final TabPagerAdapter tabPagerAdapter = (TabPagerAdapter) mViewPager.getAdapter();
        int previousPosition = mSelectedTabIndex;
        LinearLayout previousSelectedTab = (LinearLayout) mTabLayout.getChildAt(previousPosition);
        previousSelectedTab.setPressed(false);
        previousSelectedTab.setActivated(false);
        previousSelectedTab.invalidate();
    }

    private void checkViewPager() {
        if (mViewPager == null) {
            if (isInEditMode()) {
                mViewPager = new ViewPager(getContext());
            } else {
                throw new IllegalStateException("ViewPager has not been bound.");
            }
        }
    }


    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

}