package com.chenchen.android.pjsipdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class User {

    private String mUserName;
    private String mPassWord;
    private String mUrl;
    private SharedPreferences mPref;
    private SharedPreferences.Editor ed;


    private static User mUser;

    protected User(String userName, String passWord, String url, SharedPreferences pref) {
        mUserName = userName;
        mPassWord = passWord;
        mUrl = url;
        mPref = pref;
        ed = mPref.edit();
    }

    public static User getInstance(Context context){
        if(null == mUser){
            SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String mUserName = pref.getString("UserName", "");
            String mPassWord = pref.getString("PassWord", "");
            String mUrl = pref.getString("Url", "0.0.0.0");
            mUser = new User(mUserName, mPassWord, mUrl, pref);
        }
        return mUser;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        ed.putString("UserName", userName);
        ed.commit();
        mUserName = userName;
    }

    public String getPassWord() {
        return mPassWord;
    }

    public void setPassWord(String passWord) {
        ed.putString("PassWord", passWord);
        ed.commit();
        mPassWord = passWord;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        ed.putString("Url", url);
        ed.commit();
        mUrl = url;
    }

    public void modifyUser(String userName, String passWord, String url) {
        setUserName(userName);
        setPassWord(passWord);
        setUrl(url);
    }
}
