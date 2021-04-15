package com.chenchen.android.pjsipdemo.Domain;

import android.os.Handler;
import android.os.Looper;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;
import com.chenchen.android.pjsipdemo.DemoApplication;
import com.chenchen.android.pjsipdemo.Interfaces.OnPJSipRegStateListener;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.MyActivityManager;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pj_qos_type;


public class SipAccount extends Account {

    private static final String LOG_TAG = SipAccount.class.getSimpleName();


    private static SipAccount acc;
    private SipCall mCall;

    private AccountConfig accountConfig;

    private SipAccount() {

    }

    public AccountConfig getAccountConfig() {
        return accountConfig;
    }

    public SipCall getCall() {
        return mCall;
    }

    public void setCall(SipCall call) {
        mCall = call;
    }


    public static SipAccount getInstance(){
        if(null == acc){
            acc = new SipAccount();
            acc.register();
        }
        return acc;
    }

    // 注册状态回调
    @Override
    public void onRegState(OnRegStateParam prm) {
        Handler handler = new Handler(Looper.getMainLooper());
        OnPJSipRegStateListener onPJSipRegStateListener = (OnPJSipRegStateListener)MyActivityManager.getManager().findActivity(DemoActivity.class);
        if(null == onPJSipRegStateListener) return;
        if (prm.getCode().swigValue() / 100 == 2) {
            handler.post(onPJSipRegStateListener::onSuccess);
        } else {
            handler.post(onPJSipRegStateListener::onError);
        }

    }

    // 呼入电话回调
    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        if(null != mCall && !mCall.isActive()) mCall.delete();
        else if(null != mCall && mCall.isActive()) return;
        try{
            mCall =  new SipCall(acc, prm.getCallId());
            CallInfo callInfo = mCall.getInfo();
            if(callInfo.getRemVideoCount() > 0) mCall.setVideoCall(true);

            ((DemoActivity)MyActivityManager.getManager().findActivity(DemoActivity.class)).startCallInActivity(callInfo.getRemoteContact());

        }catch (Exception e){
            Logger.error(LOG_TAG, e.toString());
        }

    }

    // 注册
    public void register() {
        try {
            User mUser = User.getInstance(DemoApplication.getInstance().getApplicationContext());
            accountConfig = new AccountConfig();
            accountConfig.setIdUri("sip:" + mUser.getUserName() + "@" + mUser.getUrl());
            accountConfig.getRegConfig().setRegistrarUri("sip:" + mUser.getUrl());
            AuthCredInfo cred = new AuthCredInfo("digest", "*", mUser.getUserName(), 0, mUser.getPassWord());
            accountConfig.getSipConfig().getAuthCreds().add( cred );
            // Create the account
            accountConfig.getMediaConfig().getTransportConfig().setQosType(pj_qos_type.PJ_QOS_TYPE_VIDEO);
            accountConfig.getVideoConfig().setAutoShowIncoming(true);
            accountConfig.getVideoConfig().setAutoTransmitOutgoing(true);
            accountConfig.getVideoConfig().setDefaultCaptureDevice(1);
            accountConfig.getVideoConfig().setDefaultRenderDevice(0);
            acc.create(accountConfig);
        } catch (Exception e) {
           Logger.error(LOG_TAG, e.toString());
        }
    }

    @Override
    public  void onInstantMessage(OnInstantMessageParam prm){
        String msgBody = prm.getMsgBody();
        String fromUri = prm.getFromUri();
        String buddyName = fromUri.substring(fromUri.indexOf(':') + 1, fromUri.indexOf("@"));
        String buddyUrl = fromUri.substring(fromUri.indexOf('@') + 1, fromUri.indexOf(">"));
        SipBuddyList.getInstance().addSipBuddy(buddyName, buddyUrl);
        SipBuddy sipBuddy = SipBuddyList.getInstance().getSipBuddy(buddyName);
        sipBuddy.addMessages(buddyName, msgBody);
    }
}
