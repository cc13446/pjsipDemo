package com.chenchen.android.pjsipdemo.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chenchen.android.pjsipdemo.R;
import com.chenchen.android.pjsipdemo.VideoSurfaceHolders.VideoPreviewHolder;
import com.chenchen.android.pjsipdemo.VideoSurfaceHolders.VideoWindowHolder;

import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindowHandle;

import java.util.Objects;

public class CallingVideoFragment extends Fragment {

    private SurfaceView videoWindow;
    private SurfaceView videoPreview;
    private VideoPreviewHolder mVideoPreviewHolder;
    private VideoWindowHolder mVideoWindowHolder;

    private TextView contactNameText;
    private Button hangUpBtn;
    private Button louderBtn;

    private String mContactName;

    private static final String CONTACTNAME = "conatct_name";

    public CallingVideoFragment() {

    }
    public static CallingVideoFragment newInstance(String callInfo) {

        Bundle args = new Bundle();
        args.putString(CONTACTNAME, callInfo);
        CallingVideoFragment fragment = new CallingVideoFragment();
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

        View v = inflater.inflate(R.layout.fragment_calling_video, container, false);
        contactNameText = v.findViewById(R.id.contact_name_text);
        contactNameText.setText(mContactName);

        videoWindow = v.findViewById(R.id.video_window);
        videoPreview = v.findViewById(R.id.video_preview);
        mVideoPreviewHolder = new VideoPreviewHolder();
        mVideoWindowHolder = new VideoWindowHolder();

        videoPreview.setVisibility(View.VISIBLE);
        videoWindow.setVisibility(View.VISIBLE);

        videoPreview.getHolder().addCallback(mVideoPreviewHolder);
        videoWindow.getHolder().addCallback(mVideoWindowHolder);

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