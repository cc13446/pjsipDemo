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

    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
    }

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
