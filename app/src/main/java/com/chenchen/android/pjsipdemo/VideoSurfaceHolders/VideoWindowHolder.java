package com.chenchen.android.pjsipdemo.VideoSurfaceHolders;

import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipCall;
import com.chenchen.android.pjsipdemo.Logger;

import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoWindowHandle;

public class VideoWindowHolder  implements SurfaceHolder.Callback {

    private static final String LOG_TAG = VideoWindowHolder.class.getSimpleName();


    private SipAccount sipAccount;
    private SipCall sipCall;

    public void updateVideoPreview(SurfaceHolder holder){
        sipAccount = SipAccount.getInstance();
        if(null == sipAccount) return;
        sipCall = sipAccount.getCall();
        if(null == sipCall) return;
        if(null != sipCall.getVideoPreview() && null != sipCall.getVideoWindow()){
            sipCall.setIncomingVideoFeed(holder.getSurface());
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        updateVideoPreview(holder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        updateVideoPreview(holder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        sipCall.stopIncomingVideoFeed();
    }
}
