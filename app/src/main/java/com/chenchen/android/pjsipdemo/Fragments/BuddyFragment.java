package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chenchen.android.pjsipdemo.Dao.BuddyDao;
import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipBuddy;
import com.chenchen.android.pjsipdemo.Domain.SipBuddyList;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.R;

import org.pjsip.pjsua2.BuddyInfo;

import java.util.Objects;

public class BuddyFragment extends Fragment {

    private static final String LOG_TAG = BuddyFragment.class.getSimpleName();
    private static final String BUDDY_INFO = "buddy_info";

    private SipBuddy mSipBuddy;

    private TextView mBuddyNameText;
    private TextView mUrlText;
    private Button mSubmitBtn;

    private String mBuddyInfo;

    public static BuddyFragment newInstance(String buddyInfo) {

        Bundle args = new Bundle();
        args.putString(BUDDY_INFO, buddyInfo);
        BuddyFragment fragment = new BuddyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BuddyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuddyInfo = getArguments().getString(BUDDY_INFO, "");
        mSipBuddy = SipBuddyList.getInstance().getSipBuddy(mBuddyInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buddy, container, false);
        mBuddyNameText = (TextView)v.findViewById(R.id.contact_name);
        mBuddyNameText.setText(mBuddyInfo);
        mUrlText = (TextView)v.findViewById(R.id.Domain);
        if(null != mSipBuddy){
            try{
                mUrlText.setText(mSipBuddy.getBuddyUrl());
                mBuddyNameText.setText(mSipBuddy.getBuddyName());
            }catch (Exception e){
                Logger.error(LOG_TAG, "BuddyFragment onCreateVie", e);
            }

        }
        mSubmitBtn = v.findViewById(R.id.SubmitBtn);
        mSubmitBtn.setOnClickListener(v1 -> {
            String buddyName = mBuddyNameText.getText().toString();
            if(0 == buddyName.length()){
                Toast.makeText(getContext(),"Contact name is empty",Toast.LENGTH_SHORT).show();
                return;
            }

            String url = mUrlText.getText().toString();
            if(0 == url.length()){
                Toast.makeText(getContext(),"Domain is empty",Toast.LENGTH_SHORT).show();
                return;
            }

            try{
               SipBuddyList.getInstance().addSipBuddy(buddyName, url);
            }catch (Exception e){
                Logger.error(LOG_TAG, "BuddyFragment onCreateView", e);
            }
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK);
            getActivity().finish();
        });
        return v;
    }
}