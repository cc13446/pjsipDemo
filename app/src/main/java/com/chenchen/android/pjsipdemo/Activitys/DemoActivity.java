package com.chenchen.android.pjsipdemo.Activitys;
// 主界面
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipBuddy;
import com.chenchen.android.pjsipdemo.Domain.SipBuddyList;
import com.chenchen.android.pjsipdemo.Domain.SipCall;
import com.chenchen.android.pjsipdemo.Domain.User;
import com.chenchen.android.pjsipdemo.Logger;
import com.chenchen.android.pjsipdemo.MyActivityManager;
import com.chenchen.android.pjsipdemo.Interfaces.OnCallStateListener;
import com.chenchen.android.pjsipdemo.Interfaces.OnPJSipRegStateListener;
import com.chenchen.android.pjsipdemo.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DemoActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, OnCallStateListener, OnPJSipRegStateListener {


    private static final String LOG_TAG = DemoActivity.class.getSimpleName();
    // fragments 切换
    public static final int PAGE_CONTACT = 0;
    public static final int PAGE_PHONE = 1;
    public static final int PAGE_MESSAGE = 2;
    public static final int PAGE_RECORD = 3;

    // Activity 返回值
    public static final int REQUEST_CODE_PERMISSION = 0;
    public static final int REQUEST_CODE_USER = 1;
    public static final int REQUEST_CODE_CALLING = 2;
    public static final int REQUEST_CODE_CALLIN = 3;
    public static final int REQUEST_CODE_CALLOUT = 4;
    public static final int REQUEST_CODE_BUDDY = 5;
    public static final int REQUEST_CODE_MESSAGE = 6;
    public static final int REQUEST_CODE_SETTING = 7;
    public static final int REQUEST_CODE_PUSH_TO_TALKING = 8;

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

    //需要的权限
    String[] permissions = new String[]{Manifest.permission.USE_SIP,
            Manifest.permission.USE_SIP,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_NETWORK_STATE};
    //哪些权限未授予
    List<String> mPermissionList = new ArrayList<>();

    AlertDialog mPermissionDialog;
    String mPackName = "com.chenchen.android.pjsipdemo";


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


        acc = SipAccount.getInstance();
        MyActivityManager.getManager().addActivity(this);

        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        }
        else{
            myRequestPermissions();
        }
    }

    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
                    startSettingActivity();
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
        // Setting Activity
        else if(REQUEST_CODE_SETTING == requestCode){
            if (Activity.RESULT_OK == resultCode) {
                Toast.makeText(this, "修改设置成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "修改设置失败", Toast.LENGTH_SHORT).show();
            }
        }
        // buddyActivity
        else if(REQUEST_CODE_BUDDY == requestCode){
            if (Activity.RESULT_OK == resultCode) {
                Toast.makeText(this, "修改联系人成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "修改联系人失败", Toast.LENGTH_SHORT).show();
            }
        }
        // 来电话
        else if(REQUEST_CODE_CALLIN == requestCode){
            SipCall sipCall = acc.getCall();

            if(Activity.RESULT_OK == resultCode){
                if(null != sipCall) sipCall.acceptIncomingCall();
            }
            else if(Activity.RESULT_FIRST_USER == resultCode){
                if(null != sipCall && sipCall.isActive()){
                    sipCall.hangUp();
                }
            }
        }
        // 电话中
        else if(REQUEST_CODE_CALLING == requestCode){
            SipCall sipCall = acc.getCall();
            if(Activity.RESULT_FIRST_USER == resultCode){
                if(null != sipCall && sipCall.isActive()){
                    sipCall.hangUp();
                }
            }
        }
        // 电话呼出
        else if(REQUEST_CODE_CALLOUT == requestCode){
            SipCall sipCall = acc.getCall();
            if(Activity.RESULT_FIRST_USER == resultCode){
                if(null != sipCall && sipCall.isActive()){
                    sipCall.hangUp();
                }
            }
        }
        // Push To talking
        else if(REQUEST_CODE_PUSH_TO_TALKING == requestCode){
            SipCall sipCall = acc.getCall();
            if(Activity.RESULT_FIRST_USER == resultCode){
                if(null != sipCall && sipCall.isActive()){
                    sipCall.hangUp();
                }
            }
            List<SipBuddy> buddies = SipBuddyList.getInstance().getSipBuddies();
            for(SipBuddy s : buddies){
                s.setPushToTalk(false);
            }
        }
    }
    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (REQUEST_CODE_PERMISSION == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();
            }
        }
    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", (dialog, which) -> {
                        cancelPermissionDialog();
                        Uri packageURI = Uri.parse("package:" + mPackName);
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", (dialog, which) -> cancelPermissionDialog())
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
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

    // 启动Activity

    // 启动电话呼入Activity
    public void startCallInActivity(String info){
        startActivityForResult(CallInActivity.newIntent(this, info), REQUEST_CODE_CALLIN);
    }
    // 启动user activity
    public void startUserActivity(){
        startActivityForResult(UserActivity.newIntent(this), REQUEST_CODE_USER);
    }

    // 启动setting activity
    public void startSettingActivity(){
        startActivityForResult(SettingActivity.newIntent(this), REQUEST_CODE_SETTING);
    }

    // 启动对讲 activity
    public void startPushToTalkingActivity(){
        startActivityForResult(PushToTalkingActivity.newIntent(this), REQUEST_CODE_PUSH_TO_TALKING);
    }

    // 启动buddy activity
    public void startBuddyActivity(String buddyInfo){
        startActivityForResult(BuddyActivity.newIntent(this, buddyInfo), REQUEST_CODE_BUDDY);
    }

    // 启动message activity
    public void startMessageActivity(String buddyInfo){
        startActivityForResult(MessageActivity.newIntent(this, buddyInfo), REQUEST_CODE_MESSAGE);
    }

    // 启动callingIn activity
    public void startcallingInActivity(String contactName){
        Intent intent;
        if(!acc.getCall().getVideoCall()){
            intent = CallingAudioActivity.newIntent(this, "Call From " + contactName);
        }
        else{
            intent = CallingVideoActivity.newIntent(this, "Call From " + contactName);
        }
        startActivityForResult(intent, REQUEST_CODE_CALLING);
    }

    // 启动callingOut activity
    public void startcallingOutActivity(String contactName){
        Intent intent;
        if(!acc.getCall().getVideoCall()){
            intent = CallingAudioActivity.newIntent(this, "Call To " + contactName);
        }
        else{
            intent = CallingVideoActivity.newIntent(this, "Call To " + contactName);
        }
        startActivityForResult(intent, REQUEST_CODE_CALLING);
    }

    // OnCallstateListener
    @Override
    public void callingIn(String contactName) {
        Logger.error(LOG_TAG, "CallingIn");
        myRequestPermissions();
        MyActivityManager.getManager().finishActivity(CallOutActivity.class);
        MyActivityManager.getManager().finishActivity(CallInActivity.class);
        if(null != acc.getCall()){
            SipCall sipCall = acc.getCall();
            if(!sipCall.isPushToTalk()){
                startcallingInActivity(contactName);
            }
        }
    }

    @Override
    public void callingOut(String contactName) {
        Logger.error(LOG_TAG, "CallingOut");
        myRequestPermissions();
        MyActivityManager.getManager().finishActivity(CallOutActivity.class);
        MyActivityManager.getManager().finishActivity(CallInActivity.class);

        if(null != acc.getCall()){
            SipCall sipCall = acc.getCall();
            if(!sipCall.isPushToTalk()){
                startcallingOutActivity(contactName);
            }
            else{
                startPushToTalkingActivity();
            }
        }
    }

    @Override
    public void callOut(String contactName) {
        Logger.error(LOG_TAG, "CallOut");
        if(!acc.getCall().isPushToTalk()){
            Intent intent = CallOutActivity.newIntent(this,"Call To " + contactName);
            startActivityForResult(intent, REQUEST_CODE_CALLOUT);
        }
    }

    @Override
    public void early() {
        Logger.error(LOG_TAG, "early");
    }

    @Override
    public void connecting() {
        Logger.error(LOG_TAG, "connecting");
    }

    @Override
    public void confirmed() {
        Logger.error(LOG_TAG, "confirmed");
    }

    @Override
    public void disconnected() {
        Logger.error(LOG_TAG, "disconnected");
        MyActivityManager.getManager().finishActivity(CallOutActivity.class);
        MyActivityManager.getManager().finishActivity(CallInActivity.class);
        MyActivityManager.getManager().finishActivity(CallingAudioActivity.class);
        MyActivityManager.getManager().finishActivity(CallingVideoActivity.class);
        MyActivityManager.getManager().finishActivity(PushToTalkingActivity.class);
    }

    @Override
    public void error() {
        Logger.error(LOG_TAG, "error");
    }

    //OnPjSipRegStateListener
    @Override
    public void onSuccess() {
        setToolbarState("REGISTER_SUCCESS");
    }

    @Override
    public void onError() {
        setToolbarState("REGISTER_FAIL");
    }
}