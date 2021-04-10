package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipEndPoint;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.R;
import com.chenchen.android.pjsipdemo.VideoSurfaceHolders.VideoPreviewHolder;
import com.chenchen.android.pjsipdemo.VideoSurfaceHolders.VideoWindowHolder;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.pjmedia_orient;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class CallingVideoFragment extends Fragment {

    private static final String LOG_TAG = CallingVideoFragment.class.getSimpleName();

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private int minute = 0;
    private int second = 0;
    private int hour = 0;

    private SurfaceView videoWindow;
    private SurfaceView videoPreview;
    private static VideoPreviewHolder mVideoPreviewHolder = new VideoPreviewHolder();
    private static VideoWindowHolder mVideoWindowHolder = new VideoWindowHolder();

    private TextView contactNameText;
    private TextView timeText;
    private Button hangUpBtn;
    private Button louderBtn;

    private String mContactName;

    private static final String CONTACTNAME = "conatct_name";

    public CallingVideoFragment() {

    }
    public static CallingVideoFragment newInstance(String callInfo) {

        Bundle args = new Bundle();
        args.putString(CONTACTNAME, callInfo);
        CallingVideoFragment fragment = new CallingVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactName = getArguments().getString(CONTACTNAME);
        handler  = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                second ++;
                if(60 <= second){
                    minute ++;
                    second = 0;
                }
                if(60 <= minute){
                    hour ++;
                    minute = 0;
                }
                timeText.setText(String.format("%02d:%02d:%02d", hour, minute, second));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_calling_video, container, false);
        contactNameText = v.findViewById(R.id.contact_name_text);
        contactNameText.setText(mContactName);

        timeText = v.findViewById(R.id.time_text);


        videoWindow = v.findViewById(R.id.video_window);
        videoPreview = v.findViewById(R.id.video_preview);

        videoPreview.setVisibility(View.VISIBLE);
        videoWindow.setVisibility(View.VISIBLE);
        videoPreview.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoPreview.setZOrderOnTop(true);

        videoPreview.getHolder().addCallback(mVideoPreviewHolder);
        videoWindow.getHolder().addCallback(mVideoWindowHolder);

        hangUpBtn = v.findViewById(R.id.hang_up_btn);
        louderBtn = v.findViewById(R.id.louder_btn);

        hangUpBtn.setOnClickListener(v1 -> {
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_FIRST_USER);
            getActivity().finish();
        });

        louderBtn.setOnClickListener(v1 -> {

        });

        setCaptureOrient(pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG);
        
        startTimer();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);;
        Display display = wm.getDefaultDisplay();;
        int rotation = display.getRotation();
        pjmedia_orient orient;

        Logger.error(LOG_TAG,"Device orientation changed: " + rotation);

        switch (rotation) {
            case Surface.ROTATION_0:   // Portrait
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;
                break;
            case Surface.ROTATION_90:  // Landscape, home button on the right
                orient = pjmedia_orient.PJMEDIA_ORIENT_NATURAL;
                break;
            case Surface.ROTATION_180:
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_90DEG;
                break;
            case Surface.ROTATION_270: // Landscape, home button on the left
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_180DEG;
                break;
            default:
                orient = pjmedia_orient.PJMEDIA_ORIENT_UNKNOWN;
        }
        setCaptureOrient(orient);

    }

    private void setCaptureOrient(pjmedia_orient orient){
        if (SipEndPoint.getInstance() != null && SipAccount.getInstance() != null) {
            try {
                AccountConfig cfg = SipAccount.getInstance().getAccountConfig();
                int cap_dev = cfg.getVideoConfig().getDefaultCaptureDevice();
                SipEndPoint.getInstance().vidDevManager().setCaptureOrient(cap_dev, orient, true);
            } catch (Exception e) {
                Logger.error(LOG_TAG, " onConfigurationChanged", e);
            }
        }
    }

    private void startTimer() {
        //防止多次点击开启计时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }
}