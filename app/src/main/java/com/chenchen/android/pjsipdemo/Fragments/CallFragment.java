package com.chenchen.android.pjsipdemo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipCall;
import com.chenchen.android.pjsipdemo.Domain.SipEndPoint;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.R;
import com.chenchen.android.pjsipdemo.Domain.User;

import org.pjsip.pjsua2.*;


public class CallFragment extends Fragment{

    private static final String LOG_TAG = SipAccount.class.getSimpleName();

    private TextView mNumber;
    private ImageButton callButton;
    private ImageButton callVideoButton;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private Button btn_J;
    private Button btn_X;
    private ImageButton btn_backSpace;


    private SipCall mSipCall;
    private SipAccount acc;

    User mUser;

    public CallFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = User.getInstance(getActivity());
        acc = SipAccount.getInstance(mUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_call, container, false);

        mNumber = v.findViewById(R.id.tel_num);
        callButton = v.findViewById(R.id.phone_call_button);
        callButton.setOnClickListener(v1 -> {
            call(false);
            Logger.error(LOG_TAG, "CALL " + mNumber.getText().toString());
        });

        callVideoButton = v.findViewById(R.id.video_call_button);
        callVideoButton.setOnClickListener(v1 -> {
            call(true);
            Logger.error(LOG_TAG, "CALL Video " + mNumber.getText().toString());
        });

        View.OnClickListener onClickListener = v1 -> mNumber.setText(mNumber.getText() + ((Button) v1).getText().toString());
        btn_0 = v.findViewById(R.id.button0);
        btn_0.setOnClickListener(onClickListener);
        btn_1 = v.findViewById(R.id.button1);
        btn_1.setOnClickListener(onClickListener);
        btn_2 = v.findViewById(R.id.button2);
        btn_2.setOnClickListener(onClickListener);
        btn_3 = v.findViewById(R.id.button3);
        btn_3.setOnClickListener(onClickListener);
        btn_4 = v.findViewById(R.id.button4);
        btn_4.setOnClickListener(onClickListener);
        btn_5 = v.findViewById(R.id.button5);
        btn_5.setOnClickListener(onClickListener);
        btn_6 = v.findViewById(R.id.button6);
        btn_6.setOnClickListener(onClickListener);
        btn_7 = v.findViewById(R.id.button7);
        btn_7.setOnClickListener(onClickListener);
        btn_8 = v.findViewById(R.id.button8);
        btn_8.setOnClickListener(onClickListener);
        btn_9 = v.findViewById(R.id.button9);
        btn_9.setOnClickListener(onClickListener);
        btn_J = v.findViewById(R.id.buttonJ);
        btn_J.setOnClickListener(onClickListener);
        btn_X = v.findViewById(R.id.buttonX);
        btn_X.setOnClickListener(onClickListener);
        btn_backSpace = v.findViewById(R.id.btn_backspace);
        btn_backSpace.setOnClickListener(v1 -> {
            String s = mNumber.getText().toString();
            if(0 == s.length()) return;
            mNumber.setText(s.substring(0, s.length() - 1));
        });

        return v;
    }

    private void call(boolean video){
        if(null == acc) {
            return;
        }
        mSipCall = new SipCall(acc, -1);
        mSipCall.setVideoCall(video);
        acc.setCall(mSipCall);
        CallOpParam prm = new CallOpParam();

        //这里注意，格式  sip: 110@192.168.1.163
        String dst_uri = "sip:" + mNumber.getText().toString() + "@" + mUser.getUrl();
        try {
            mSipCall.makeCall(dst_uri, prm);
        } catch (Exception e) {
            mSipCall.delete();
        }
    }
}