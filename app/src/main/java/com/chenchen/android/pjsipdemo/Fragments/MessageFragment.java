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

import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipBuddy;
import com.chenchen.android.pjsipdemo.Domain.SipBuddyList;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.R;
import com.chenchen.android.pjsipdemo.SoftKeyBroadManager;

import org.pjsip.pjsua2.SendInstantMessageParam;

import java.util.Objects;

public class MessageFragment extends Fragment {

    private static final String LOG_TAG = MessageFragment.class.getSimpleName();
    private static final String BUDDY_INFO = "buddy_info";

    private TextView buddyName;
    private TextView messages;
    private TextView messageToSend;
    private Button sendBtn;
    private View mBottomView;


    private SipBuddy mSipBuddy;

    private String mBuddyInfo;

    public static MessageFragment newInstance(String buddyInfo) {

        Bundle args = new Bundle();
        args.putString(BUDDY_INFO, buddyInfo);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuddyInfo = getArguments().getString(BUDDY_INFO, "");
        mSipBuddy = SipBuddyList.getInstance().getSipBuddy(mBuddyInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        buddyName = v.findViewById(R.id.message_buddy_name);
        buddyName.setText(mSipBuddy.getBuddyName());
        messages = v.findViewById(R.id.messages);
        UpdateMessages();
        messageToSend = v.findViewById(R.id.message_to_send);
        sendBtn = v.findViewById(R.id.send);
        sendBtn.setOnClickListener(v1 -> {
            String s = messageToSend.getText().toString();
            SendInstantMessageParam param = new SendInstantMessageParam();
            param.setContent(s);
            try {
                mSipBuddy.sendInstantMessage(param);
                mSipBuddy.addMessages("Me",s);
                UpdateMessages();
                messageToSend.setText("");
            }catch (Exception e){
                Logger.error(LOG_TAG, "sendInstantMessage", e);
            }
        });

        View root = v.findViewById(R.id.root);
        mBottomView = v.findViewById(R.id.bottom);
        SoftKeyBroadManager softKeyBroadManager = new SoftKeyBroadManager(root);
        softKeyBroadManager.addSoftKeyboardStateListener(softKeyboardStateListener);

        return v;
    }

    SoftKeyBroadManager.SoftKeyboardStateListener softKeyboardStateListener = new SoftKeyBroadManager.SoftKeyboardStateListener() {

        @Override
        public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            mBottomView.requestLayout();
        }

        @Override
        public void onSoftKeyboardClosed() {
            mBottomView.requestLayout();
        }
    };

    public void UpdateMessages(){
        messages.setText(mSipBuddy.getMessages());
    }
}