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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallOutFragment extends Fragment {

    private ImageView mImageView;
    private ImageButton noListenBtn;
    private TextView callToText;

    private String callInfo;

    private static final String CALLINFO = "call_info";

    public CallOutFragment() {

    }

    public static CallOutFragment newInstance(String callInfo) {

        Bundle args = new Bundle();
        args.putString(CALLINFO, callInfo);
        CallOutFragment fragment = new CallOutFragment();
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

        View v = inflater.inflate(R.layout.fragment_call_out, container, false);
        mImageView = v.findViewById(R.id.contactImageView);
        noListenBtn = v.findViewById(R.id.no_listen_button);
        callToText = v.findViewById(R.id.call_to_text);

        callToText.setText(callInfo);

        noListenBtn.setOnClickListener(v1 -> {
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_FIRST_USER);
            getActivity().finish();
        });
        return v;
    }
}