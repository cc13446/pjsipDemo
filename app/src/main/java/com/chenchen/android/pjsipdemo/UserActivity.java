package com.chenchen.android.pjsipdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class UserActivity extends SimpleFragmentActivity  {

    @Override
    protected Fragment createFragment() {
        return new UserFragment(this);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, UserActivity.class);
        return intent;
    }
}