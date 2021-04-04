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

import com.chenchen.android.pjsipdemo.Domain.MyAccount;
import com.chenchen.android.pjsipdemo.Domain.MyCall;
import com.chenchen.android.pjsipdemo.R;
import com.chenchen.android.pjsipdemo.Domain.User;

import org.pjsip.pjsua2.*;


public class CallFragment extends Fragment implements View.OnClickListener {

    private TextView mNumber;
    private ImageButton callButton;
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


    private Endpoint ep;
    private MyCall myCall;
    private MyAccount acc;


    User mUser;

    public CallFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = User.getInstance(getActivity());
        acc = MyAccount.getInstance(mUser);

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_call, container, false);

        mNumber = (TextView)v.findViewById(R.id.tel_num);
        callButton = (ImageButton)v.findViewById(R.id.phone_call_button);
        callButton.setOnClickListener(v1 -> {
            String ip = mNumber.getText().toString();
            call();
            Toast.makeText(getContext(), "call " + ip, Toast.LENGTH_SHORT).show();
        });
        btn_0 = v.findViewById(R.id.button0);
        btn_0.setOnClickListener(this);
        btn_1 = v.findViewById(R.id.button1);
        btn_1.setOnClickListener(this);
        btn_2 = v.findViewById(R.id.button2);
        btn_2.setOnClickListener(this);
        btn_3 = v.findViewById(R.id.button3);
        btn_3.setOnClickListener(this);
        btn_4 = v.findViewById(R.id.button4);
        btn_4.setOnClickListener(this);
        btn_5 = v.findViewById(R.id.button5);
        btn_5.setOnClickListener(this);
        btn_6 = v.findViewById(R.id.button6);
        btn_6.setOnClickListener(this);
        btn_7 = v.findViewById(R.id.button7);
        btn_7.setOnClickListener(this);
        btn_8 = v.findViewById(R.id.button8);
        btn_8.setOnClickListener(this);
        btn_9 = v.findViewById(R.id.button9);
        btn_9.setOnClickListener(this);
        btn_J = v.findViewById(R.id.buttonJ);
        btn_J.setOnClickListener(this);
        btn_X = v.findViewById(R.id.buttonX);
        btn_X.setOnClickListener(this);
        btn_backSpace = v.findViewById(R.id.btn_backspace);
        btn_backSpace.setOnClickListener(v1 -> {
            String s = mNumber.getText().toString();
            if(0 == s.length()) return;
            mNumber.setText(s.substring(0, s.length() - 1));
        });

        return v;
    }

    private void init() throws Exception {
        // Create endpoint
        if(null == ep) ep = new Endpoint();

        ep.libCreate();
        // Initialize endpoint
        EpConfig epConfig = new EpConfig();
        ep.libInit( epConfig );
        // Create SIP transport. Error handling sample is shown
        TransportConfig sipTpConfig = new TransportConfig();
        sipTpConfig.setPort(5060);
        ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
        // Start the library
        ep.libStart();
    }

    private void call(){
        if(null == acc) {
            return;
        }
        myCall = new MyCall(acc, -1);
        CallOpParam prm = new CallOpParam();
        CallSetting opt = prm.getOpt();
        opt.setAudioCount(1);
        opt.setVideoCount(0);

        //这里注意，格式  sip: 110@192.168.1.163
        String dst_uri = "sip:" + mNumber.getText().toString() + "@" + mUser.getUrl();
        try {
            myCall.makeCall(dst_uri, prm);
        } catch (Exception e) {
            myCall.delete();
        }
    }

    @Override
    public void onClick(View v) {
        mNumber.setText(mNumber.getText() + ((Button)v).getText().toString());
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        if(null != ep) {
            try {
                ep.libDestroy();
                ep.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}