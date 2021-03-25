package com.chenchen.android.pjsipdemo;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.pjmedia_type;

public class MyCall extends Call {

    public MyCall(Account acc, int call_id) {
        super(acc, call_id);
    }

    /***
     * 当通话状态改变时通知应用程序。
     * 然后，应用程序可以通过调用getInfo（）函数来查询调用信息以获取详细调用状态。
     * @param prm
     */
    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
    }

    /***
     * 通话中媒体状态发生变化时通知应用程序。
     * 正常的应用程序需要实现这个回调，例如将呼叫的媒体连接到声音设备。当使用ICE时，该回调也将被调用以报告ICE协商失败。
     * @param prm
     */
    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        try {
            CallInfo callInfo = getInfo();
            CallMediaInfoVector m = callInfo.getMedia();
            for(int i = 0; i < m.size(); i++){
                AudioMedia aum = AudioMedia.typecastFromMedia(this.getMedia(i));
                if(null != aum && pjmedia_type.PJMEDIA_TYPE_AUDIO == aum.getType()){
                    AudDevManager mgr = Endpoint.instance().audDevManager();
                    aum.startTransmit(mgr.getPlaybackDevMedia());
                    mgr.getCaptureDevMedia().startTransmit(aum);
                }
            }
        } catch (Exception e){

        }
    }
}
