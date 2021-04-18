package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.SettingFragment;


public class SettingActivity extends SimpleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new SettingFragment(getApplicationContext());
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }
}
