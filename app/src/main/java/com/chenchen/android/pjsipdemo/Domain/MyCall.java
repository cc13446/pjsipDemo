package com.chenchen.android.pjsipdemo.Domain;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;
import com.chenchen.android.pjsipdemo.Interfaces.OnCallStateListener;
import com.chenchen.android.pjsipdemo.MyActivityManager;

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
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;

public class MyCall extends Call {
    private CallInfo info;
    private pjsip_inv_state state;
    private pjsip_role_e role;

    public MyCall(Account acc, int call_id) {
        super(acc, call_id);
    }


    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
        OnCallStateListener onCallStateListener = (OnCallStateListener) MyActivityManager.getManager().findActivity(DemoActivity.class);

        try {
            info = getInfo();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        state = info.getState();
        role = info.getRole();//这个参数就可以判断，这个通话，你是呼出还是呼入
        if (state == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
            onCallStateListener.calling();
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {
            onCallStateListener.early();
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
            onCallStateListener.connecting();
            // 电话呼出
            if (role == pjsip_role_e.PJSIP_ROLE_UAC) {
                onCallStateListener.callOut(info.getRemoteContact());
            }
            //电话呼入
            else if (role == pjsip_role_e.PJSIP_ROLE_UAS) {
                onCallStateListener.callIn(info.getRemoteContact());
            }
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
            onCallStateListener.confirmed();
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
            onCallStateListener.disconnected();
        }
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
