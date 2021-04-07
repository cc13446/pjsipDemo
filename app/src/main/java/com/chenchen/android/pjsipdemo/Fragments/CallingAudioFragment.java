package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chenchen.android.pjsipdemo.R;

import java.util.Objects;

public class CallingAudioFragment extends Fragment {

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


        return v;
    }
}