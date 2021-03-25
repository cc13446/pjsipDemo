package com.chenchen.android.pjsipdemo;

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

import java.util.Objects;


public class UserFragment extends Fragment {

    private  User mUser;

    private TextView mUserNameText;
    private TextView mPassWordText;
    private TextView mUrlText;
    private Button mSubmitBtn;

    public UserFragment(Context context) {
        mUser = User.getInstance(context);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        mUserNameText = (TextView)v.findViewById(R.id.UserName);
        mUserNameText.setText(mUser.getUserName());
        mPassWordText = (TextView)v.findViewById(R.id.PassWord);
        mPassWordText.setText(mUser.getPassWord());
        mUrlText = (TextView)v.findViewById(R.id.Domain);
        mUrlText.setText(mUser.getUrl());
        mSubmitBtn = (Button)v.findViewById(R.id.SubmitBtn);
        mSubmitBtn.setOnClickListener(v1 -> {
            String userName = mUserNameText.getText().toString();
            if(0 == userName.length()){
                Toast.makeText(getContext(),"User name is empty",Toast.LENGTH_SHORT).show();
                return;
            }
            else mUser.setUserName(userName);

            String passWord = mPassWordText.getText().toString();
            if(0 == passWord.length()){
                Toast.makeText(getContext(),"Pass word is empty",Toast.LENGTH_SHORT).show();
                return;
            }
            else mUser.setPassWord(passWord);

            String url = mUrlText.getText().toString();
            if(0 == url.length()){
                Toast.makeText(getContext(),"Domain is empty",Toast.LENGTH_SHORT).show();
                return;
            }
            else mUser.setUrl(url);
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK);
            getActivity().finish();
        });
        return v;
    }
}