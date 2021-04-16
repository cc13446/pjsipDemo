package com.chenchen.android.pjsipdemo.Domain;


import android.view.Surface;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;
import com.chenchen.android.pjsipdemo.Interfaces.OnCallStateListener;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.MyActivityManager;

import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.MediaSize;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua2;
import org.pjsip.pjsua2.pjsua_call_flag;
import org.pjsip.pjsua2.pjsua_call_media_status;



public class SipCall extends Call {

    private static final String LOG_TAG = SipCall.class.getSimpleName();

    private CallInfo info = null;
    private pjsip_inv_state state = null;
    private pjsip_role_e role = null;

    private boolean videoCall = false;
    private boolean pushToTalk = false;

    private VideoWindow mVideoWindow;
    private VideoPreview mVideoPreview;

    public SipCall(SipAccount acc, int callId) {
        super(acc, callId);
    }

    public void setVideoCall(boolean videoCall) {
        this.videoCall = videoCall;
    }
    public boolean getVideoCall(){
        return videoCall;
    }

    public boolean isPushToTalk() {
        return pushToTalk;
    }

    public void setPushToTalk(boolean pushToTalk) {
        this.pushToTalk = pushToTalk;
    }

    //电话状态回调
    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
        OnCallStateListener onCallStateListener = (OnCallStateListener) MyActivityManager.getManager().findActivity(DemoActivity.class);

        try {
            info = getInfo();
            state = info.getState();
            // 这个参数就可以判断，这个通话，你是呼出还是呼入
            role = info.getRole();
        }catch (Exception ex){
            Logger.error(LOG_TAG, "Error while getting call status", ex);
        }

        if (state == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
            onCallStateListener.callOut(info.getRemoteUri());
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {
            onCallStateListener.early();
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
            onCallStateListener.connecting();
            // 电话呼出
            if (role == pjsip_role_e.PJSIP_ROLE_UAC) {
                onCallStateListener.callingOut(info.getRemoteContact());
            }
            //电话呼入
            else if (role == pjsip_role_e.PJSIP_ROLE_UAS) {
                onCallStateListener.callingIn(info.getRemoteContact());
            }
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
            onCallStateListener.confirmed();
        } else if (state == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
            onCallStateListener.disconnected();
            stopVideoFeeds();
        }
    }

    //接电话
    public void acceptIncomingCall() {
        CallOpParam param = new CallOpParam();
        param.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        CallSetting callSetting = param.getOpt();
        callSetting.setAudioCount(1);
        callSetting.setVideoCount(videoCall ? 1 : 0);


        if (!videoCall) {
            callSetting.setFlag(pjsua_call_flag.PJSUA_CALL_INCLUDE_DISABLED_MEDIA.swigValue());
        }
        try {
            answer(param);
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Failed to accept incoming call", exc);
        }
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
    // 挂电话
    public void hangUp() {
        CallOpParam param = new CallOpParam();
        param.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);

        try {
            hangup(param);
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Failed to hangUp call", exc);
        }
    }

    // 打电话
    @Override
    public void makeCall(String dst_uri, CallOpParam prm) throws java.lang.Exception {
        CallSetting callSetting = prm.getOpt();
        callSetting.setAudioCount(1);
        callSetting.setVideoCount(videoCall ? 1 : 0);
        super.makeCall(dst_uri, prm);
    }

    // 处理媒体流
    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        CallInfo info;
        try {
            info = getInfo();
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "onCallMediaState: error while getting call info", exc);
            return;
        }
        CallMediaInfoVector cmiv = info.getMedia();
        for (int i = 0; i < cmiv.size(); i++) {
            Media media = getMedia(i);
            CallMediaInfo mediaInfo = cmiv.get(i);

            if (mediaInfo.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO
                    && media != null
                    && mediaInfo.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE) {

                handleAudioMedia(media);

            } else if (mediaInfo.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO
                    && mediaInfo.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE
                    && mediaInfo.getVideoIncomingWindowId() != pjsua2.INVALID_ID) {

                handleVideoMedia(mediaInfo);
            }
        }
    }
    private void handleAudioMedia(Media media) {
        AudioMedia audioMedia = AudioMedia.typecastFromMedia(media);

        // connect the call audio media to sound device
        try {
            if (audioMedia != null) {
                AudDevManager mgr = SipEndPoint.getInstance().audDevManager();
                mgr.getCaptureDevMedia().startTransmit(audioMedia);
                audioMedia.startTransmit(mgr.getPlaybackDevMedia());
            }
        } catch (Exception exc) {
            Logger.error(LOG_TAG, "Error while connecting audio media to sound device", exc);
        }
    }

    private void handleVideoMedia(CallMediaInfo mediaInfo) {
        if (mVideoWindow != null) {
            mVideoWindow.delete();
        }
        if (mVideoPreview != null) {
            mVideoPreview.delete();
        }
        mVideoPreview = new VideoPreview(mediaInfo.getVideoCapDev());
        mVideoWindow = new VideoWindow(mediaInfo.getVideoIncomingWindowId());
    }

    public VideoWindow getVideoWindow() {
        return mVideoWindow;
    }

    public void setVideoWindow(VideoWindow mVideoWindow) {
        this.mVideoWindow = mVideoWindow;
    }

    public VideoPreview getVideoPreview() {
        return mVideoPreview;
    }

    public void setVideoPreview(VideoPreview mVideoPreview) {
        this.mVideoPreview = mVideoPreview;
    }

    private void stopVideoFeeds() {
        stopIncomingVideoFeed();
        stopPreviewVideoFeed();
    }

    public void startIncomingVideoFeed(Surface surface) {
        if (mVideoWindow != null) {
            VideoWindowHandle videoWindowHandle = new VideoWindowHandle();
            videoWindowHandle.getHandle().setWindow(surface);
            try {
                mVideoWindow.setWindow(videoWindowHandle);

            } catch (Exception ex) {
                Logger.error(LOG_TAG, "Unable to setup Incoming Video Feed", ex);
            }
        }
    }

    public void startPreviewVideoFeed(Surface surface) {
        if (mVideoPreview != null) {
            VideoWindowHandle videoWindowHandle = new VideoWindowHandle();
            videoWindowHandle.getHandle().setWindow(surface);
            VideoPreviewOpParam videoPreviewOpParam = new VideoPreviewOpParam();
            videoPreviewOpParam.setWindow(videoWindowHandle);
            Logger.error(LOG_TAG, "able to start Video Preview");
            try {
                mVideoPreview.start(videoPreviewOpParam);
            } catch (Exception ex) {
                Logger.error(LOG_TAG, "Unable to start Video Preview", ex);
            }
        }
    }

    public void stopIncomingVideoFeed() {
        VideoWindow videoWindow = getVideoWindow();
        if (videoWindow != null) {
            try {
                videoWindow.delete();
            } catch (Exception ex) {
                Logger.error(LOG_TAG, "Unable to stop remote video feed", ex);
            }
        }
    }

    public void stopPreviewVideoFeed() {
        VideoPreview videoPreview = getVideoPreview();
        if (videoPreview != null) {
            try {
                videoPreview.stop();
            } catch (Exception ex) {
                Logger.error(LOG_TAG, "Unable to stop preview video feed", ex);
            }
        }
    }
}
