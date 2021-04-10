package com.chenchen.android.pjsipdemo.Activitys;
//  电话呼入界面

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.CallInFragment;


public class CallInActivity extends SimpleFragmentActivity{


    // 来电显示联系人信息
    private static final String EXTRA_CALL_INFO = "com.chenchen.android.pjsipdemo.activiys.callinactivity.call_info";

    @Override
    protected Fragment createFragment() {
        String callInfo = getIntent().getStringExtra(EXTRA_CALL_INFO);
        return CallInFragment.newInstance(callInfo);
    }

    public static Intent newIntent(Context context, String info){
        Intent intent = new Intent(context, CallInActivity.class);
        intent.putExtra(EXTRA_CALL_INFO, info);
        return intent;
    }
}
