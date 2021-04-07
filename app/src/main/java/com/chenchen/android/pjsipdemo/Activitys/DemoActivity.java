package com.chenchen.android.pjsipdemo.Activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.User;
import com.chenchen.android.pjsipdemo.MyActivityManager;
import com.chenchen.android.pjsipdemo.Interfaces.OnCallStateListener;
import com.chenchen.android.pjsipdemo.Interfaces.OnPJSipRegStateListener;
import com.chenchen.android.pjsipdemo.R;
import com.google.android.material.navigation.NavigationView;


public class DemoActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, OnCallStateListener, OnPJSipRegStateListener {


    // fragments切换
    public static final int PAGE_CONTACT = 0;
    public static final int PAGE_PHONE = 1;
    public static final int PAGE_MESSAGE = 2;
    public static final int PAGE_RECORD = 3;

    // Activity 返回值
    public static final int REQUEST_CODE_PERMISSION = 0;
    public static final int REQUEST_CODE_USER = 1;
    public static final int REQUEST_CODE_CALL_LISTEN = 2;
    public static final int REQUEST_CODE_CALLING = 3;

    //UI Objects
    private Toolbar mToolbar;
    private ViewPager vPager;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_contact;
    private RadioButton rb_message;
    private RadioButton rb_phone;
    private RadioButton rb_record;

    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private ImageView headView;

    private MyFragmentPagerAdapter mAdapter;

    // domain
    private User mUser;
    private SipAccount acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_bottom_nv);
        // 获取单例
        mUser = User.getInstance(this);

        // 适配器
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_contact.setChecked(true);

        // 请求权限
        myRequestPermissions();

        acc = SipAccount.getInstance(mUser);
        MyActivityManager.getManager().addActivity(this);
    }

    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_contact = (RadioButton) findViewById(R.id.rb_contact);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_phone = (RadioButton) findViewById(R.id.rb_phone);
        rb_record = (RadioButton) findViewById(R.id.rb_record);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vPager = (ViewPager) findViewById(R.id.vpager);
        vPager.setAdapter(mAdapter);
        vPager.setCurrentItem(0);
        vPager.addOnPageChangeListener(this);


        // toolBar
        drawerLayout = findViewById(R.id.DrawerLayout);
        mToolbar = findViewById(R.id.mToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mToolbar.setOnClickListener(v1 -> acc.register());

        // 侧划栏
        navigation_view = findViewById(R.id.navigation_view);
        headView = navigation_view.getHeaderView(0).findViewById(R.id.iv_head);
        headView.setOnClickListener(v2 -> {
            drawerLayout.closeDrawers();
            Toast.makeText(this, "点击了图片", Toast.LENGTH_SHORT).show();
        });
        navigation_view.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_item_user:
                    startUserActivity();
                    break;
                case R.id.navigation_item_setting:
                    Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.navigation_item_exit:
                    this.finish();
                    break;
                default:
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 申请权限
        if(REQUEST_CODE_PERMISSION == requestCode){
            Toast.makeText(this, "缺少权限，无法正常运行", Toast.LENGTH_LONG).show();
        }
        // 用户Activity
        else if(REQUEST_CODE_USER == requestCode){
            if (Activity.RESULT_OK == resultCode) {
                Toast.makeText(this, "修改用户成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "修改用户失败", Toast.LENGTH_SHORT).show();
            }
        }
        // 来电话
        else if(REQUEST_CODE_CALL_LISTEN == requestCode){
            if(Activity.RESULT_OK == resultCode){
                acc.answer();
            }
            else if(Activity.RESULT_CANCELED == resultCode){
                acc.hangUp();
            }
        }
        else if(REQUEST_CODE_CALLING == requestCode){
            if(Activity.RESULT_CANCELED == resultCode){
                acc.hangUp();
            }
        }

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_contact:
                vPager.setCurrentItem(PAGE_CONTACT);
                break;
            case R.id.rb_message:
                vPager.setCurrentItem(PAGE_MESSAGE);
                break;
            case R.id.rb_phone:
                vPager.setCurrentItem(PAGE_PHONE);
                break;
            case R.id.rb_record:
                vPager.setCurrentItem(PAGE_RECORD);
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
            switch (vPager.getCurrentItem()) {
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

    private void myRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_SIP) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_SIP}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, REQUEST_CODE_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_PERMISSION);
        }
    }

    private void setToolbarState(String state){
        mToolbar.setTitle(state);
    }

    // 启动来电话Activity
    public void startCallListenActivity(String info){
        startActivityForResult(CallListenActivity.newIntent(this, info), REQUEST_CODE_CALL_LISTEN);
    }
    // 启动user activity
    public void startUserActivity(){
        startActivityForResult(UserActivity.newIntent(this), REQUEST_CODE_USER);
    }

    @Override
    public void callIn(String contactName) {
        Intent intent = CallingActivity.newIntent(this,"Call From " + contactName);
        startActivityForResult(intent, REQUEST_CODE_CALLING);
    }

    @Override
    public void callOut(String contactName) {
        Intent intent = CallingActivity.newIntent(this,"Call To " + contactName);
        startActivityForResult(intent, REQUEST_CODE_CALLING);
    }

    @Override
    public void calling() {

    }

    @Override
    public void early() {

    }

    @Override
    public void connecting() {

    }

    @Override
    public void confirmed() {

    }

    @Override
    public void disconnected() {
        Log.d("demo", "disconnected: ");
        MyActivityManager.getManager().finishActivity(CallingActivity.class);
    }

    @Override
    public void error() {

    }

    @Override
    public void onSuccess() {
        setToolbarState("REGISTER_SUCCESS");
    }

    @Override
    public void onError() {
        setToolbarState("REGISTER_FAIL");
    }
}