package com.chenchen.android.pjsipdemo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


public class MessagesFragment extends Fragment {


    private class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBuddyName;
        private TextView mBuddyMessage;
        private SipBuddy mSipBuddy;


        public MessageHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_message, parent, false));

            mBuddyName = itemView.findViewById(R.id.message_item_buddy_name);
            mBuddyMessage = itemView.findViewById(R.id.message_item_message_preview);

            itemView.setOnClickListener(this);
        }

        private void bind(SipBuddy sipBuddy) {
            mSipBuddy = sipBuddy;
            mBuddyName.setText(mSipBuddy.getBuddyName());
            String s = mSipBuddy.getMessages();
            int index = s.indexOf("\n");
            if(-1 == index){
                mBuddyMessage.setText(s);
            }
            else {
                mBuddyMessage.setText(s.substring(0,index));
            }
        }

        @Override
        public void onClick(View v){
            ((DemoActivity)MyActivityManager.getManager().findActivity(DemoActivity.class)).startMessageActivity(mSipBuddy.getBuddyName());
        }

    }

    private class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final String LOG_TAG = MessageAdapter.class.getSimpleName();

        private List<SipBuddy> mSipBuddies;

        public void setSipBuddies(List<SipBuddy> sipBuddies) {
            mSipBuddies = sipBuddies;
        }

        public MessageAdapter(List<SipBuddy> sipBuddies) {
            mSipBuddies = sipBuddies;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MessageHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                ((MessageHolder)holder).bind(mSipBuddies.get(position));
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
            SipBuddyList.getInstance().getSipBuddy(mSipBuddies.get(pos).getBuddyName()).setMessages("");
            mSipBuddies.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    private static final int REQUEST_BUDDY = 1;
    private RecyclerView mCrimeRecyclerView;
    private MessageAdapter mAdapter;
    private List<SipBuddy> mSipBuddies;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.message_recycler_view);
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
        mSipBuddies = sipBuddyList.getMessageSipBuddies();

        if(mAdapter == null){
            mAdapter = new MessageAdapter(mSipBuddies);
        }else{
            mAdapter.setSipBuddies(mSipBuddies);
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
