package com.chenchen.android.pjsipdemo;

import android.app.Application;

import com.chenchen.android.pjsipdemo.Dao.DataBaseHelper;
import com.chenchen.android.pjsipdemo.Domain.Setting;
import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipBuddyList;
import com.chenchen.android.pjsipdemo.Domain.SipEndPoint;
import com.chenchen.android.pjsipdemo.Domain.User;

public class DemoApplication extends Application {

    private User mUser;
    private Setting mSetting;
    private SipEndPoint mSipEndPoint;
    private SipAccount mSipAccount;
    private MyActivityManager mMyActivityManager;
    private DataBaseHelper mDataBaseHelper;
    private SipBuddyList mSipBuddyList;

    private static DemoApplication demoApplication;

    private static final String LOG_TAG = DemoApplication.class.getSimpleName();

    static {
        try{
            System.loadLibrary("openh264");
            // Ticket #1937: libyuv is now included as static lib
            //System.loadLibrary("yuv");
        } catch (UnsatisfiedLinkError e) {
            Logger.error(LOG_TAG,"UnsatisfiedLinkError: " + e.getMessage());
        }
        System.loadLibrary("pjsua2");
        Logger.error(LOG_TAG,"Library loaded");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication = this;
        // 单例初始化
        mUser = User.getInstance(this);
        mSetting = Setting.getInstance(this);
        mMyActivityManager = MyActivityManager.getManager();
        mSipEndPoint = SipEndPoint.getInstance();
        mSipEndPoint.init();
        mSipAccount = SipAccount.getInstance();
        mDataBaseHelper = DataBaseHelper.getInstance(this);
        mSipBuddyList = SipBuddyList.getInstance();

    }

    @Override
    public void onTerminate() {

        mSipEndPoint.destroy();
        super.onTerminate();
    }

    public static DemoApplication getInstance(){
        return demoApplication;
    }
}
