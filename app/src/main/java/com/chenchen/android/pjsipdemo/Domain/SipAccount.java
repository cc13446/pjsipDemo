package com.chenchen.android.pjsipdemo.Domain;

import android.os.Handler;
import android.os.Looper;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;
import com.chenchen.android.pjsipdemo.Interfaces.OnPJSipRegStateListener;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.MyActivityManager;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.HashMap;
import java.util.Set;


public class SipAccount extends Account {

    private static final String LOG_TAG = SipAccount.class.getSimpleName();


    public static SipAccount acc;
    private User mUser;
    private SipCall mCall;

    public SipAccount() {

    }

    public SipCall getCall() {
        return mCall;
    }

    public void setCall(SipCall call) {
        mCall = call;
    }

    public void setUser(User user) {
        mUser = user;
    }


    public static SipAccount getInstance(User user){
        if(null == acc){
            acc = new SipAccount();
        }
        acc.setUser(user);
        return acc;
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        Handler handler = new Handler(Looper.getMainLooper());
        OnPJSipRegStateListener onPJSipRegStateListener = (OnPJSipRegStateListener)MyActivityManager.getManager().findActivity(DemoActivity.class);
        if (prm.getCode().swigValue() / 100 == 2) {
            handler.post(onPJSipRegStateListener::onSuccess);
        } else {
            handler.post(onPJSipRegStateListener::onError);
        }

    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        if(null != mCall && mCall.isActive()){
            return;
        }
        mCall =  new SipCall(acc, prm.getCallId());
        // 获取来电信息
        String info = prm.getRdata().getWholeMsg();
        int i = info.lastIndexOf("Contact:");
        info = info.substring(i, i + 30);

        ((DemoActivity)MyActivityManager.getManager().findActivity(DemoActivity.class)).startCallListenActivity(info);

    }

    public void register() {
        try {
            AccountConfig accountConfig = new AccountConfig();
            accountConfig.setIdUri("sip:" + mUser.getUserName() + "@" + mUser.getUrl());
            accountConfig.getRegConfig().setRegistrarUri("sip:" + mUser.getUrl());
            AuthCredInfo cred = new AuthCredInfo("digest", "*", mUser.getUserName(), 0, mUser.getPassWord());
            accountConfig.getSipConfig().getAuthCreds().add( cred );
            // Create the account
            accountConfig.getVideoConfig().setAutoShowIncoming(true);
            accountConfig.getVideoConfig().setAutoTransmitOutgoing(true);
            acc.create(accountConfig);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
