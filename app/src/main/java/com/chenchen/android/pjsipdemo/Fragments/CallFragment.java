package com.chenchen.android.pjsipdemo.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.chenchen.android.pjsipdemo.Domain.MyAccount;
import com.chenchen.android.pjsipdemo.Domain.MyCall;
import com.chenchen.android.pjsipdemo.R;
import com.chenchen.android.pjsipdemo.Domain.User;
import com.chenchen.android.pjsipdemo.Activitys.UserActivity;
import com.google.android.material.navigation.NavigationView;
import java.util.Objects;

import org.pjsip.pjsua2.*;


public class CallFragment extends Fragment {

    private TextView iPAddress;
    private Button callButton;


    private Endpoint ep;
    private MyCall myCall;
    private MyAccount acc;

    private Handler handler = new Handler();

    User mUser;

    public CallFragment() {

    }

    public static CallFragment newInstance() {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        iPAddress = (TextView)v.findViewById(R.id.IPAddressText);
        callButton = (Button)v.findViewById(R.id.call_button);
        callButton.setOnClickListener(v1 -> {
            String ip = iPAddress.getText().toString();
            call();
            Toast.makeText(getContext(), "call " + ip, Toast.LENGTH_SHORT).show();

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
        String dst_uri = "sip:" + iPAddress.getText().toString() + "@" + mUser.getUrl();
        try {
            myCall.makeCall(dst_uri, prm);
        } catch (Exception e) {
            myCall.delete();
        }
    }
}