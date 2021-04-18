package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.PushToTalkingFragment;

public class PushToTalkingActivity extends SimpleFragmentActivity{

    private PushToTalkingFragment mPushToTalkingFragment;
    @Override
    protected Fragment createFragment() {
        mPushToTalkingFragment = PushToTalkingFragment.newInstance();
        return mPushToTalkingFragment;
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, PushToTalkingActivity.class);
        return intent;
    }

    public PushToTalkingFragment getPushToTalkingFragment() {
        return mPushToTalkingFragment;
    }
}
