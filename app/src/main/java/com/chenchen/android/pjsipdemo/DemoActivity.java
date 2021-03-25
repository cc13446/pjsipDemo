package com.chenchen.android.pjsipdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class DemoActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {




    public static final int PAGE_CONTACT = 0;
    public static final int PAGE_PHONE = 1;
    public static final int PAGE_MESSAGE = 2;
    public static final int PAGE_RECORD = 3;

    //UI Objects
    private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_contact;
    private RadioButton rb_message;
    private RadioButton rb_phone;
    private RadioButton rb_record;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_bottom_nv);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_contact.setChecked(true);
    }
    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_contact = (RadioButton) findViewById(R.id.rb_contact);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_phone = (RadioButton) findViewById(R.id.rb_phone);
        rb_record = (RadioButton) findViewById(R.id.rb_record);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_contact:
                vpager.setCurrentItem(PAGE_CONTACT);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_MESSAGE);
                break;
            case R.id.rb_phone:
                vpager.setCurrentItem(PAGE_PHONE);
                break;
            case R.id.rb_record:
                vpager.setCurrentItem(PAGE_RECORD);
                break;
        }
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕

        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_CONTACT:
                    rb_contact.setChecked(true);
                    break;
                case PAGE_MESSAGE:
                    rb_message.setChecked(true);
                    break;
                case PAGE_PHONE:
                    rb_phone.setChecked(true);
                    break;
                case PAGE_RECORD:
                    rb_record.setChecked(true);
                    break;
            }
        }
    }
}