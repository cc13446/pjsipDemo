package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenchen.android.pjsipdemo.R;

import java.util.Objects;

public class CallInFragment extends Fragment {

    private ImageView mImageView;
    private ImageButton listenBtn;
    private ImageButton noListenBtn;
    private TextView callFromText;

    private String callInfo;

    private static final String CALLINFO = "call_info";

    public CallInFragment() {

    }

    public static CallInFragment newInstance(String callInfo) {

        Bundle args = new Bundle();
        args.putString(CALLINFO, callInfo);
        CallInFragment fragment = new CallInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callInfo = getArguments().getString(CALLINFO);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_call_in, container, false);
        mImageView = v.findViewById(R.id.contactImageView);
        listenBtn = v.findViewById(R.id.listen_button);
        noListenBtn = v.findViewById(R.id.no_listen_button);
        callFromText = v.findViewById(R.id.call_to_text);

        callFromText.setText(callInfo);

        listenBtn.setOnClickListener(v1 -> {
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK);
            getActivity().finish();
        });

        noListenBtn.setOnClickListener(v1 -> {
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_FIRST_USER);
            getActivity().finish();
        });
        return v;
    }
}