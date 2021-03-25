package com.chenchen.android.pjsipdemo.Activitys;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.chenchen.android.pjsipdemo.Fragments.UserFragment;

public class UserActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserFragment(getApplicationContext());
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, UserActivity.class);
        return intent;
    }
}