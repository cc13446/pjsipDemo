package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.CallListenFragment;

public class CallListenActivity extends SimpleFragmentActivity{


    // 来电显示
    private static final String EXTRA_CALL_INFO = "com.chenchen.android.pjsipdemo.call_info";

    @Override
    protected Fragment createFragment() {
        String callInfo = getIntent().getStringExtra(EXTRA_CALL_INFO);
        return CallListenFragment.newInstance(callInfo);
    }

    public static Intent newIntent(Context context, String info){
        Intent intent = new Intent(context, CallListenActivity.class);
        intent.putExtra(EXTRA_CALL_INFO, info);
        return intent;
    }
}
