package com.chenchen.android.pjsipdemo.Domain;

import android.util.Log;

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

    public SipBuddy(String buddyName, String buddyUrl){
        super();
        mBuddyName = buddyName;
        mBuddyUrl = buddyUrl;
        isRegister = registerBuddy();
        mMessages = "";
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

    public String getMessages() {
        return mMessages;
    }

    public void setMessages(String messages) {
        mMessages = messages;
    }
    public void addMessages(String messages) {
        mMessages = mMessages +"\n" + messages;
    }

    public Boolean getRegister() {
        return isRegister;
    }

    public void setRegister(Boolean register) {
        isRegister = register;
    }

    public String getBuddyName() {
        return mBuddyName;
    }

    public void setBuddyName(String buddyName) {
        mBuddyName = buddyName;
        try {
            getInfo().setContact(buddyName);
        }catch (Exception e){
            Logger.error(LOG_TAG, "setBuddyName", e);
        }
    }

    public String getBuddyUrl() {
        return mBuddyUrl;
    }

    public void setBuddyUrl(String buddyUrl) {
        mBuddyUrl = buddyUrl;
        try {
            getInfo().setUri(buddyUrl);
        }catch (Exception e){
            Logger.error(LOG_TAG, "setBuddyUrl", e);
        }
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
