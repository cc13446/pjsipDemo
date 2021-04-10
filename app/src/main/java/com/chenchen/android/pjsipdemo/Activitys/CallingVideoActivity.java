package com.chenchen.android.pjsipdemo.Activitys;
// 视频电话中界面

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.CallingVideoFragment;

public class CallingVideoActivity extends SimpleFragmentActivity{


    // 通话页面显示联系人信息
    private static final String EXTRA_CONTACT_NAME = "com.chenchen.android.pjsipdemo.activitys.callingvideoactivity.contact_name";

    @Override
    protected Fragment createFragment() {
        String s = getIntent().getStringExtra(EXTRA_CONTACT_NAME);
        return CallingVideoFragment.newInstance(s);
    }

    public static Intent newIntent(Context context, String info){
        Intent intent = new Intent(context, CallingVideoActivity.class);
        intent.putExtra(EXTRA_CONTACT_NAME, info);
        return intent;
    }
}

