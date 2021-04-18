package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chenchen.android.pjsipdemo.Domain.Setting;
import com.chenchen.android.pjsipdemo.R;

import java.util.Objects;


public class SettingFragment  extends Fragment {

    private Setting mSetting;

    private TextView mConferencesNumber;
    private Button mSubmitBtn;

    public SettingFragment(Context context) {
        mSetting = Setting.getInstance(getActivity());
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        mConferencesNumber = (TextView)v.findViewById(R.id.conferences_number);
        mConferencesNumber.setText(mSetting.getConferencesNumber());

        mSubmitBtn = (Button)v.findViewById(R.id.submit_btn);
        mSubmitBtn.setOnClickListener(v1 -> {
            String s = mConferencesNumber.getText().toString();
            if(0 == s.length()){
                Toast.makeText(getContext(),"Conferences Number is empty",Toast.LENGTH_SHORT).show();
                return;
            }

            mSetting.setConferencesNumber(s);
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK);
            getActivity().finish();
        });
        return v;
    }
}