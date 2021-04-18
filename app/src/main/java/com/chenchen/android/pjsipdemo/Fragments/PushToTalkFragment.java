package com.chenchen.android.pjsipdemo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.chenchen.android.pjsipdemo.Activitys.DemoActivity;
import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipBuddy;
import com.chenchen.android.pjsipdemo.Domain.SipBuddyList;
import com.chenchen.android.pjsipdemo.Domain.SipCall;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.MyActivityManager;
import com.chenchen.android.pjsipdemo.R;

import org.pjsip.pjsua2.CallOpParam;

import java.util.List;


public class PushToTalkFragment extends Fragment {


    private class PushToTalkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBuddyName;
        private TextView mBuddyState;
        private SipBuddy mSipBuddy;

        private CheckBox mCheckBox;

        public PushToTalkHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_push_to_talk, parent, false));

            mBuddyName = itemView.findViewById(R.id.push_buddy_name);
            mBuddyState = itemView.findViewById(R.id.push_buddy_state);
            mCheckBox = itemView.findViewById(R.id.checkBox);
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSipBuddy.setPushToTalk(isChecked);
                }
            });

            itemView.setOnClickListener(this);
        }

        private void bind(SipBuddy sipBuddy) throws Exception {
            mSipBuddy = sipBuddy;
            mBuddyName.setText(mSipBuddy.getBuddyName());
            mBuddyState.setText(mSipBuddy.getInfo().getPresStatus().getStatusText());
            mCheckBox.setChecked(mSipBuddy.getPushToTalk());
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class PushToTalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final String LOG_TAG = PushToTalkFragment.PushToTalkAdapter.class.getSimpleName();

        private List<SipBuddy> mSipBuddies;


        public PushToTalkAdapter(List<SipBuddy> sipBuddies) {
            mSipBuddies = sipBuddies;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new PushToTalkFragment.PushToTalkHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                ((PushToTalkFragment.PushToTalkHolder)holder).bind(mSipBuddies.get(position));
            } catch (Exception e) {
                Logger.error(LOG_TAG, "onBindViewHolder: ", e);
            }
        }

        @Override
        public int getItemCount() {
            return mSipBuddies.size();
        }
    }

    private RecyclerView mCrimeRecyclerView;
    private PushToTalkFragment.PushToTalkAdapter mAdapter;
    private Button mPushToTalkBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_push_to_talk, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.push_to_talk_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPushToTalkBtn = view.findViewById(R.id.push_to_talk_btn);
        mPushToTalkBtn.setOnClickListener(v1->{

            List<SipBuddy> mSipBuddies = SipBuddyList.getInstance().getSipBuddies();
            for(SipBuddy s : mSipBuddies){
                if(s.getPushToTalk()) PushToTalk(s);
            }
            ((DemoActivity)MyActivityManager.getManager().findActivity(DemoActivity.class)).startPushToTalkingActivity();
        });
        updateUI();
        return view;
    }
    private void PushToTalk(SipBuddy sipBuddy){
        if(null == sipBuddy) {
            return;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        SipBuddyList sipBuddyList = SipBuddyList.getInstance();
        List<SipBuddy> mSipBuddies = sipBuddyList.getSipBuddies();
        if(mAdapter == null){
            mAdapter = new PushToTalkFragment.PushToTalkAdapter(mSipBuddies);
        }
        mCrimeRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
