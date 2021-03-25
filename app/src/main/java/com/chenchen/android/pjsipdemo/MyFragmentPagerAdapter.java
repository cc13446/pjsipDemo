package com.chenchen.android.pjsipdemo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private final int PAGER_COUNT = 4;

    private ContactFragment mContactFragment = null;
    private CallFragment mCallFragment = null;
    private MessageFragment mMessageFragment = null;
    private RecordFragment mRecordFragment = null;



    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContactFragment = new ContactFragment();
        mCallFragment = new CallFragment();
        mMessageFragment = new MessageFragment();
        mRecordFragment = new RecordFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case DemoActivity.PAGE_CONTACT:
                fragment = mContactFragment;
                break;
            case DemoActivity.PAGE_MESSAGE:
                fragment = mMessageFragment;
                break;
            case DemoActivity.PAGE_PHONE:
                fragment = mCallFragment;
                break;
            case DemoActivity.PAGE_RECORD:
                fragment = mRecordFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
