package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.BuddyFragment;
import com.chenchen.android.pjsipdemo.Fragments.CallInFragment;
import com.chenchen.android.pjsipdemo.Fragments.UserFragment;

public class BuddyActivity extends SimpleFragmentActivity {

    private static final String EXTRA_BUDDY_INFO = "com.chenchen.android.pjsipdemo.activiys.buddyactivity.buddyinfo";

    @Override
    protected Fragment createFragment() {
        String buddyInfo = getIntent().getStringExtra(EXTRA_BUDDY_INFO);
        return BuddyFragment.newInstance(buddyInfo);
    }

    public static Intent newIntent(Context context, String info){
        Intent intent = new Intent(context, BuddyActivity.class);
        intent.putExtra(EXTRA_BUDDY_INFO, info);
        return intent;
    }
}