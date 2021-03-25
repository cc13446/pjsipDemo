package com.chenchen.android.pjsipdemo;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class DemoActivity extends SimpleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new DemoFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show();
        }
    }
}