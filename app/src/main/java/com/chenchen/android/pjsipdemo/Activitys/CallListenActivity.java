package com.chenchen.android.pjsipdemo.Activitys;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Fragments.CallListenFragment;

public class CallListenActivity extends SimpleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CallListenFragment();
    }
}
