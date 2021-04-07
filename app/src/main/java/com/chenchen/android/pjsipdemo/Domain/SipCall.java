package com.chenchen.android.pjsipdemo.Domain;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;
import com.chenchen.android.pjsipdemo.Interfaces.OnCallStateListener;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.MyActivityManager;

import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.ToneGenerator;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;


public class SipCall extends Call {

    private static final String LOG_TAG = SipCall.class.getSimpleName();

    private SipAccount account;
    private boolean localHold = false;
    private boolean localMute = false;
    private boolean localVideoMute = false;
    private long connectTimestamp = 0;
    private ToneGenerator toneGenerator = null;
    private boolean videoCall = false;
    private boolean videoConference = false;
    private boolean frontCamera = true;

    private VideoWindow mVideoWindow = null;
    private VideoPreview mVideoPreview = null;

    private CallInfo info = null;
    private pjsip_inv_state state = null;
    private pjsip_role_e role = null;
    private pjsip_status_code callStatus = null;
    private int callID = 0;

    /**
     * Incoming call constructor.
     * @param acc the account which own this call
     * @param callId the id of this call
     */

    public SipCall(SipAccount acc, int callId) {
        super(acc, callId);
        account = acc;
    }

    /**
     * Outgoing call constructor.
     * @param account account which owns this call
     */
    public SipCall(SipAccount account) {
        super(account);
        this.account = account;
    }

    public pjsip_inv_state getCurrentState() {
        try {
            CallInfo info = getInfo();
            return info.getState();
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Error while getting call Info", exc);
            return pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED;
        }
    }


    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
        OnCallStateListener onCallStateListener = (OnCallStateListener) MyActivityManager.getManager().findActivity(DemoActivity.class);

        try {
            info = getInfo();
            callID = info.getId();
            state = info.getState();
            role = info.getRole();//这个参数就可以判断，这个通话，你是呼出还是呼入
            callStatus = info.getLastStatusCode();
        }catch (Exception ex){
            Logger.error(LOG_TAG, "Error while getting call status", ex);
        }

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

    public void acceptIncomingCall() {
        CallOpParam param = new CallOpParam();
        param.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        setMediaParams(param);
        if (!videoCall) {
            CallSetting callSetting = param.getOpt();
            callSetting.setFlag(pjsua_call_flag.PJSUA_CALL_INCLUDE_DISABLED_MEDIA.swigValue());
        }
        try {
            answer(param);
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Failed to accept incoming call", exc);
        }
    }

    private void setMediaParams(CallOpParam param) {
        CallSetting callSetting = param.getOpt();
        callSetting.setAudioCount(1);
        callSetting.setVideoCount(videoCall ? 1 : 0);
    }

    public void sendBusyHereToIncomingCall() {
        CallOpParam param = new CallOpParam();
        param.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE);

        try {
            answer(param);
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Failed to send busy here", exc);
        }
    }

    public void declineIncomingCall() {
        CallOpParam param = new CallOpParam();
        param.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);

        try {
            answer(param);
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Failed to decline incoming call", exc);
        }
    }

    public void hangUp() {
        CallOpParam param = new CallOpParam();
        param.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);

        try {
            hangup(param);
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Failed to hangUp call", exc);
        }
    }

    @Override
    public void makeCall(String dst_uri, CallOpParam prm) throws java.lang.Exception {
        setMediaParams(prm);
        if (!videoCall) {
            CallSetting callSetting = prm.getOpt();
            callSetting.setFlag(pjsua_call_flag.PJSUA_CALL_INCLUDE_DISABLED_MEDIA.swigValue());
        }
        super.makeCall(dst_uri, prm);
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
