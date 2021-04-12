package com.chenchen.android.pjsipdemo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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


public class BuddiesFragment extends Fragment {


    private class BuddyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBuddyName;
        private TextView mBuddyState;
        private SipBuddy mSipBuddy;

        private ImageView mBuddyMessage;
        private ImageView mBuddyCall;
        private ImageView mBuddyVideo;

        public BuddyHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_contact, parent, false));

            mBuddyName = itemView.findViewById(R.id.buddy_name);
            mBuddyState = itemView.findViewById(R.id.buddy_state);
            mBuddyMessage = itemView.findViewById(R.id.buddy_message);
            mBuddyCall = itemView.findViewById(R.id.buddy_call);
            mBuddyCall.setOnClickListener(v1->{
                call(false);
            });
            mBuddyVideo = itemView.findViewById(R.id.buddy_video);
            mBuddyVideo.setOnClickListener(v1->{
                call(true);
            });
            mBuddyMessage.setOnClickListener(v1 -> {
                ((DemoActivity)MyActivityManager.getManager().findActivity(DemoActivity.class)).startMessageActivity(mSipBuddy.getBuddyName());
            });
            itemView.setOnClickListener(this);
        }

        private void bind(SipBuddy sipBuddy) throws Exception {
            mSipBuddy = sipBuddy;
            mBuddyName.setText(mSipBuddy.getBuddyName());
            mBuddyState.setText(mSipBuddy.getInfo().getPresStatus().getStatusText());
        }

        @Override
        public void onClick(View v){
            ((DemoActivity)MyActivityManager.getManager().findActivity(DemoActivity.class)).startBuddyActivity(mSipBuddy.getBuddyName());
        }


        private void call(boolean video){
            SipAccount acc = SipAccount.getInstance();
            if(null == acc) {
                return;
            }
            SipCall mSipCall = acc.getCall();
            if(null != mSipCall && !mSipCall.isActive()) mSipCall.delete();
            else if(null != mSipCall && mSipCall.isActive()) return;
            mSipCall = new SipCall(acc, -1);
            mSipCall.setVideoCall(video);
            acc.setCall(mSipCall);
            CallOpParam prm = new CallOpParam();

            //这里注意，格式  sip: 110@192.168.1.163
            String dst_uri = "sip:" + mSipBuddy.getBuddyName() + "@" + mSipBuddy.getBuddyUrl();
            try {
                mSipCall.makeCall(dst_uri, prm);
            } catch (Exception e) {
                mSipCall.delete();
            }
        }
    }

    private class BuddyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final String LOG_TAG = BuddyAdapter.class.getSimpleName();

        private List<SipBuddy> mSipBuddies;


        public BuddyAdapter(List<SipBuddy> sipBuddies) {
            mSipBuddies = sipBuddies;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new BuddyHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                ((BuddyHolder)holder).bind(mSipBuddies.get(position));
                holder.itemView.setOnLongClickListener(v1 -> {
                    showPopMenu(v1, position);
                    return false;
                });

            } catch (Exception e) {
                Logger.error(LOG_TAG, "onBindViewHolder: ", e);
            }
        }

        @Override
        public int getItemCount() {
            return mSipBuddies.size();
        }

        public void removeItem(int pos){
            SipBuddyList.getInstance().removeSipBuddy(mSipBuddies.get(pos));
            notifyItemRemoved(pos);
        }
    }

    private static final int REQUEST_BUDDY = 1;
    private RecyclerView mCrimeRecyclerView;
    private BuddyAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_buddies, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.buddy_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
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
            mAdapter = new BuddyAdapter(mSipBuddies);
        }
        mCrimeRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                mAdapter.removeItem(pos);
                return false;
            }
        });
        popupMenu.show();
    }
}
