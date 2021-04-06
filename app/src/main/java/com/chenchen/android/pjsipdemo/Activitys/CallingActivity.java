package com.chenchen.android.pjsipdemo.Activitys;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.CallListenFragment;
import com.chenchen.android.pjsipdemo.Fragments.CallingFragment;

public class CallingActivity extends SimpleFragmentActivity{



    private static final String EXTRA_CONTACT_NAME = "com.chenchen.android.pjsipdemo.contact_name";

    @Override
    protected Fragment createFragment() {
        String s = getIntent().getStringExtra(EXTRA_CONTACT_NAME);
        return CallingFragment.newInstance(s);
    }

    public static Intent newIntent(Context context, String info){
        Intent intent = new Intent(context, CallingActivity.class);
        intent.putExtra(EXTRA_CONTACT_NAME, info);
        return intent;
    }
}
