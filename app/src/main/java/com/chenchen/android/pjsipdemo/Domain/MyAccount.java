package com.chenchen.android.pjsipdemo.Domain;

import android.content.Context;
import android.os.Handler;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;


public class MyAccount extends Account {

    public static MyAccount acc;
    private User mUser;


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


    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {

        Call call = new MyCall(acc, prm.getCallId());
        CallOpParam cprm = new CallOpParam();
        cprm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);

        try {
            call.answer(cprm);
        }catch (Exception e){

        }

    }

    public void register() {
        try {
            AccountConfig acfg = new AccountConfig();
            acfg.setIdUri("sip:" + mUser.getUserName() + "@" + mUser.getUrl());
            acfg.getRegConfig().setRegistrarUri("sip:" + mUser.getUrl());
            AuthCredInfo cred = new AuthCredInfo("digest", "*", mUser.getUserName(), 0, mUser.getPassWord());
            acfg.getSipConfig().getAuthCreds().add( cred );
            // Create the account
            acc.create(acfg);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
