package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.PushToTalkingFragment;

public class PushtoTalkingActivity extends SimpleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return PushToTalkingFragment.newInstance();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, PushtoTalkingActivity.class);
        return intent;
    }
}
