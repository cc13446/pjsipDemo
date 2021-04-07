package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.CallingAudioFragment;

public class CallingAudioActivity extends SimpleFragmentActivity{


    // 通话页面显示联系人信息
    private static final String EXTRA_CONTACT_NAME = "com.chenchen.android.pjsipdemo.contact_name";

    @Override
    protected Fragment createFragment() {
        String s = getIntent().getStringExtra(EXTRA_CONTACT_NAME);
        return CallingAudioFragment.newInstance(s);
    }

    public static Intent newIntent(Context context, String info){
        Intent intent = new Intent(context, CallingAudioActivity.class);
        intent.putExtra(EXTRA_CONTACT_NAME, info);
        return intent;
    }
}
