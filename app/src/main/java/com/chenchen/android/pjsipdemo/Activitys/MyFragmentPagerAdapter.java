package com.chenchen.android.pjsipdemo.Activitys;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chenchen.android.pjsipdemo.Fragments.CallFragment;
import com.chenchen.android.pjsipdemo.Fragments.BuddiesFragment;
import com.chenchen.android.pjsipdemo.Fragments.MessagesFragment;
import com.chenchen.android.pjsipdemo.Fragments.PushToTalkFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private final int PAGER_COUNT = 4;

    private BuddiesFragment mBuddiesFragment = null;
    private CallFragment mCallFragment = null;
    private MessagesFragment mMessagesFragment = null;
    private PushToTalkFragment mPushToTalkFragment = null;



    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mBuddiesFragment = new BuddiesFragment();
        mCallFragment = new CallFragment();
        mMessagesFragment = new MessagesFragment();
        mPushToTalkFragment = new PushToTalkFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case DemoActivity.PAGE_CONTACT:
                fragment = mBuddiesFragment;
                break;
            case DemoActivity.PAGE_MESSAGE:
                fragment = mMessagesFragment;
                break;
            case DemoActivity.PAGE_PHONE:
                fragment = mCallFragment;
                break;
            case DemoActivity.PAGE_RECORD:
                fragment = mPushToTalkFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
