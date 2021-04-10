package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chenchen.android.pjsipdemo.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class CallingAudioFragment extends Fragment {
    private static final String LOG_TAG = CallingAudioFragment.class.getSimpleName();

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private int minute = 0;
    private int second = 0;
    private int hour = 0;

    private TextView contactNameText;
    private TextView timeText;
    private Button hangUpBtn;
    private Button louderBtn;

    private String mContactName;

    private static final String CONTACTNAME = "conatct_name";

    public CallingAudioFragment() {

    }
    public static CallingAudioFragment newInstance(String callInfo) {

        Bundle args = new Bundle();
        args.putString(CONTACTNAME, callInfo);
        CallingAudioFragment fragment = new CallingAudioFragment();
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

        View v = inflater.inflate(R.layout.fragment_calling_audio, container, false);
        contactNameText = v.findViewById(R.id.contact_name_text);
        contactNameText.setText(mContactName);
        timeText = v.findViewById(R.id.time_text);
        hangUpBtn = v.findViewById(R.id.hang_up_btn);
        louderBtn = v.findViewById(R.id.louder_btn);

        hangUpBtn.setOnClickListener(v1 -> {
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_FIRST_USER);
            getActivity().finish();
        });

        louderBtn.setOnClickListener(v1 -> {
            Toast.makeText(getActivity(),"免提", Toast.LENGTH_SHORT).show();
        });

        startTimer();
        return v;
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