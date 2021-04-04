package com.chenchen.android.pjsipdemo.Domain;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.widget.Toolbar;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.Date;


public class MyAccount extends Account {

    public static MyAccount acc;
    private User mUser;
    private int mCallId;

    public void setUser(User user) {
        mUser = user;
    }


    public static MyAccount getInstance(User user){
        if(null == acc){
            acc = new MyAccount();
            acc.setUser(user);
        }
        return acc;
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        if (prm.getCode().swigValue() / 100 == 2) {
            DemoActivity.getInstance().getHandler().post(() -> DemoActivity.getInstance().setToolbarState("REGISTER_SUCCESS"));
        } else {
            DemoActivity.getInstance().getHandler().post(() -> DemoActivity.getInstance().setToolbarState("REGISTER_FAIL"));
        }

    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        mCallId =  prm.getCallId();

        String info = prm.getRdata().getWholeMsg();
        int i = info.lastIndexOf("Contact:");
        info = info.substring(i, i + 30);

        DemoActivity.getInstance().startCallListenActivity(info);

    }

    public void answer(){
        Call call = new MyCall(acc, mCallId);
        CallOpParam cprm = new CallOpParam();
        cprm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);

        try {
            call.answer(cprm);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
    public void hangUp(){
        Call call = new MyCall(acc, mCallId);
        CallOpParam cprm = new CallOpParam();
        cprm.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE);

        try {
            call.hangup(cprm);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void register() {
        try {
            AccountConfig accountConfig = new AccountConfig();
            accountConfig.setIdUri("sip:" + mUser.getUserName() + "@" + mUser.getUrl());
            accountConfig.getRegConfig().setRegistrarUri("sip:" + mUser.getUrl());
            AuthCredInfo cred = new AuthCredInfo("digest", "*", mUser.getUserName(), 0, mUser.getPassWord());
            accountConfig.getSipConfig().getAuthCreds().add( cred );
            // Create the account
            acc.create(accountConfig);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
