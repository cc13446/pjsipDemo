package com.chenchen.android.pjsipdemo.Domain;

import com.chenchen.android.pjsipdemo.Dao.BuddyDao;
import com.chenchen.android.pjsipdemo.Logger;

import org.pjsip.pjsua2.BuddyConfig;

import java.util.ArrayList;
import java.util.List;

public class SipBuddyList {
    private static final String LOG_TAG = SipBuddyList.class.getSimpleName();
    private static SipBuddyList mSipBuddyList;
    private static BuddyDao mBuddyDao;

    private List<SipBuddy> mSipBuddies;


    private SipBuddyList(List<SipBuddy> sipBuddies){
         mSipBuddies = sipBuddies;
    }

    public static SipBuddyList getInstance(){
        if(null == mSipBuddyList){
            mBuddyDao = BuddyDao.getInstance();
            mSipBuddyList = new SipBuddyList(mBuddyDao.QueryAllBuddy());
        }
        return mSipBuddyList;
    }

    public List<SipBuddy> getSipBuddies() {
        return mSipBuddies;
    }

    public SipBuddy getSipBuddy(String buddyName){
        try{
            for(SipBuddy s : mSipBuddies){
                if(buddyName.equals(s.getBuddyName())){
                    return s;
                }
            }
        }catch (Exception e){
            Logger.error(LOG_TAG, "getSipBuddy", e);
        }
        return null;
    }


    public void addSipBuddy(String buddyName, String buddyUrl){
        SipBuddy sipBuddy = new SipBuddy(buddyName, buddyUrl, "");
        SipBuddy old = mBuddyDao.QueryBuddy(buddyName);
        if(sipBuddy.getRegister()) {
            if (null == old) {
                mBuddyDao.insertBuddy(sipBuddy);
            } else {
                if(old.getBuddyUrl().equals(buddyUrl)) return;
                mBuddyDao.ModifyBuddy(new SipBuddy(buddyName, buddyUrl, old.getMessages()));
                for(SipBuddy s: mSipBuddies){
                    if(buddyName.equals(s.getBuddyName())){
                        mSipBuddies.remove(s);
                        break;
                    }
                }
            }
            mSipBuddies.add(sipBuddy);
        }
    }
    public void removeSipBuddy(SipBuddy sipBuddy){
        mSipBuddies.remove(sipBuddy);
        mBuddyDao.DeleteBuddy(sipBuddy);
    }

    public List<SipBuddy> getMessageSipBuddies(){
        List<SipBuddy> sipBuddies = new ArrayList<SipBuddy>();
        for(SipBuddy s:mSipBuddies){
            if(!s.getMessages().equals("")){
                sipBuddies.add(s);
            }
        }
        return sipBuddies;
    }
}
