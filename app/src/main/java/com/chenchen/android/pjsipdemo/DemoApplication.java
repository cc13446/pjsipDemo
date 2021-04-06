package com.chenchen.android.pjsipdemo;

import android.app.Application;

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
        MyActivityManager.getManager();

    }
}
