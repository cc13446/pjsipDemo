package com.chenchen.android.pjsipdemo;

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

import com.google.android.material.navigation.NavigationView;
import java.util.Objects;

import org.pjsip.pjsua2.*;


public class CallFragment extends Fragment {

    private TextView iPAddress;
    private Button callButton;

    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private ImageView headView;
    private Toolbar mToolbar;

    private Endpoint ep;
    private MyCall myCall;
    private MyAccount acc;

    private Handler handler = new Handler();
    private static final int REQUEST_CODE = 0;

    User mUser;

    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }


    // Subclass to extend the Account and get notifications etc.
    class MyAccount extends Account {
        @Override
        public void onRegState(OnRegStateParam prm) {
            if (prm.getCode().swigValue() / 100 == 2) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mToolbar.setTitle("REAFY");
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mToolbar.setTitle("FAIL");
                    }
                });

            }

        }

        @Override
        public void onIncomingCall(OnIncomingCallParam prm) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mToolbar.setTitle("Calling");
                }
            });


            Call call = new MyCall(acc, prm.getCallId());
            CallOpParam cprm = new CallOpParam();
            cprm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);

            try {
                call.answer(cprm);
            }catch (Exception e){

            }

        }
    }
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

        // 主内容
        iPAddress = (TextView)v.findViewById(R.id.IPAddressText);
        callButton = (Button)v.findViewById(R.id.call_button);
        callButton.setOnClickListener(v1 -> {
            String ip = iPAddress.getText().toString();
            call();
            Toast.makeText(getContext(), "call " + ip, Toast.LENGTH_SHORT).show();

        });

        // Toolbar
        drawerLayout = v.findViewById(R.id.DrawerLayout);

        mToolbar = v.findViewById(R.id.mToolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, mToolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mToolbar.setOnClickListener(v1 -> {
            register();
        });

        // 侧划栏
        navigation_view = v.findViewById(R.id.navigation_view);
        headView = navigation_view.getHeaderView(0).findViewById(R.id.iv_head);
        headView.setOnClickListener(v2 -> {
            drawerLayout.closeDrawers();
            Toast.makeText(getContext(), "点击了图片", Toast.LENGTH_LONG).show();
        });
        navigation_view.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_item_user:
                    Objects.requireNonNull(getActivity()).startActivityForResult(UserActivity.newIntent(getContext()), REQUEST_CODE);
                    break;
                case R.id.navigation_item_setting:
                    Toast.makeText(getContext(), "setting", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.navigation_item_exit:
                    Objects.requireNonNull(getActivity()).finish();
                    break;
                default:
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
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
    private void register() {
        try {
            AccountConfig acfg = new AccountConfig();
            acfg.setIdUri("sip:" + mUser.getUserName() + "@" + mUser.getUrl());
            acfg.getRegConfig().setRegistrarUri("sip:" + mUser.getUrl());
            AuthCredInfo cred = new AuthCredInfo("digest", "*", mUser.getUserName(), 0, mUser.getPassWord());
            acfg.getSipConfig().getAuthCreds().add( cred );
            // Create the account
            acc = new MyAccount();
            acc.create(acfg);


        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    private void call(){
        if(null == acc) {
            mToolbar.setTitle("Call Fail");
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