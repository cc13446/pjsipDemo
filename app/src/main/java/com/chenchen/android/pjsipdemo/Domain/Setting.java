package com.chenchen.android.pjsipdemo.Domain;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting {

    private String mConferencesNumber;
    private SharedPreferences mPref;
    private SharedPreferences.Editor ed;


    private static Setting mSetting;

    protected Setting(String conferencesNumber, SharedPreferences pref) {
        mConferencesNumber = conferencesNumber;
        mPref = pref;
        ed = mPref.edit();
    }


    public static Setting getInstance(Context context){
        if(null == mSetting){
            SharedPreferences pref = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
            String mConferencesNumber = pref.getString("ConferencesNumber", "");
            mSetting = new Setting(mConferencesNumber, pref);
        }
        return mSetting;
    }

    public String getConferencesNumber() {
        return mConferencesNumber;
    }

    public void setConferencesNumber(String conferencesNumber) {
        ed.putString("ConferencesNumber", conferencesNumber);
        ed.commit();
        mConferencesNumber = conferencesNumber;
    }
}

