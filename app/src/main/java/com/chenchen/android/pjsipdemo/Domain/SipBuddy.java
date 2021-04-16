package com.chenchen.android.pjsipdemo.Domain;

import android.util.Log;

import com.chenchen.android.pjsipdemo.Dao.BuddyDao;
import com.chenchen.android.pjsipdemo.Logger;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;

public class SipBuddy extends Buddy {

    private static final String LOG_TAG = SipBuddy.class.getSimpleName();

    private String mBuddyName;
    private String mBuddyUrl;
    private String mMessages;

    private Boolean isRegister;

    private Boolean isPushToTalk;
    private SipCall mSipCall;

    public SipBuddy(String buddyName, String buddyUrl, String message){
        super();
        mBuddyName = buddyName;
        mBuddyUrl = buddyUrl;
        isRegister = registerBuddy();
        mMessages = message;
        isPushToTalk = false;
    }

    @Override
    public void onBuddyState() {
        super.onBuddyState();
        try {
            BuddyInfo buddyInfo = getInfo();
            buddyInfo.getPresStatus().getStatusText();
        }catch (Exception e){
            Logger.error(LOG_TAG, "onBuddyState:", e);
        }
    }

    public SipCall getSipCall() {
        return mSipCall;
    }

    public void setSipCall(SipCall sipCall) {
        mSipCall = sipCall;
    }

    public Boolean getPushToTalk() {
        return isPushToTalk;
    }

    public void setPushToTalk(Boolean pushToTalk) {
        isPushToTalk = pushToTalk;
    }

    public String getMessages() {
        return mMessages;
    }

    public void setMessages(String messages) {
        mMessages = messages;
        BuddyDao.getInstance().ModifyBuddy(this);
    }
    public void addMessages(String buddyName, String messages) {
        if(mMessages.equals("")){
            mMessages = buddyName + ": " + messages;
        }
        else{
            mMessages = mMessages +"\n" + buddyName + ": " + messages;
        }
        BuddyDao.getInstance().ModifyBuddy(this);
    }

    public Boolean getRegister() {
        return isRegister;
    }


    public String getBuddyName() {
        return mBuddyName;
    }


    public String getBuddyUrl() {
        return mBuddyUrl;
    }


    private boolean registerBuddy(){
        try {
            BuddyConfig cfg = new BuddyConfig();
            cfg.setUri("sip:" + this.getBuddyName() + "@" + this.getBuddyUrl());
            create(SipAccount.getInstance(), cfg);
            subscribePresence(true);
            return true;
        }catch (Exception e){
            Logger.error(LOG_TAG, "addSipBuddy", e);
            return false;
        }
    }
}
