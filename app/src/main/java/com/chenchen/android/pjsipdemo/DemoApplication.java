package com.chenchen.android.pjsipdemo;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chenchen.android.pjsipdemo.Domain.MyAccount;
import com.chenchen.android.pjsipdemo.Domain.User;

public class DemoApplication extends Application {

    private User mUser;

    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 单例初始化
        mUser = User.getInstance(this);

    }
}
