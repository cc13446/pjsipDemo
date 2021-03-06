package com.chenchen.android.pjsipdemo.Interfaces;

public interface OnCallStateListener {

    void callingIn(String contactName);
    void callingOut(String contactName);

    /***
     * 正在呼出
     */
    void callOut(String contactName);

    /***
     * 对象响铃
     */
    void early();

    /***
     * 连接成功
     */
    void connecting();

    /***
     * 通话中
     */
    void confirmed();

    /***
     * 挂断
     */
    void disconnected();

    /***
     * 通话失败
     */
    void error();
}
