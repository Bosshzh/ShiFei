package com.lanling.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.lanling.bean.UploadData;
import com.lanling.fragment.WodeFragment;
import com.lanling.fragment.ZhuyaoFragment;
import com.lanling.util.UploadDataUtil;
import com.lanling.util.Util;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ZhuyaoFragment.OnFragmentInteractionListener,
        WodeFragment.OnFragmentInteractionListener{

    private RadioGroup main_radiogroup;//主活动下面的radiogroup
    private FragmentManager mFragmentManager;//FragmentManager
    private ZhuyaoFragment zhuyaoFragment;//主要Fragment
    private WodeFragment wodeFragment;//我的信息Fragment
    private FragmentTransaction fragmentTransaction;
    //申请两个权限，录音和文件读写
    //1、首先声明一个数组permissions，将需要的权限都放在里面
    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_radiogroup = (RadioGroup) findViewById(R.id.main_radio_group);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        }else {
            initFragments();
        }
        //判断本地是否没有上传的数据，如果有的话，那就询问是否上传
        final List<UploadData> notUploadDatas = LitePal.where("uploadOrNot = ?", "0").find(UploadData.class);
        if (notUploadDatas.size() != 0){
            //弹出框询问是否删除
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("上传")
                    .setMessage("本地还有 "+notUploadDatas.size()+" 条数据未上传，是否上传？")
                    .setIcon(R.mipmap.app_icon)
                    .setPositiveButton("上传", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UploadDataUtil.uploadDatas("http://www.zhengzhoudaxue.cn:8080/SaveData/uploaddata",notUploadDatas,MainActivity.this);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        //如果用户是刚刚登录成功的话，跳转到MainActivity
        if(intent.getIntExtra("loginsuccess",0) == 1){
            wodeFragment.Login();
            intent.removeExtra("loginsuccess");
        }
    }
    //如果intent发生改变的话
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction){
        if (null != wodeFragment){
            fragmentTransaction.hide(wodeFragment);
        }
        if(null != zhuyaoFragment){
            fragmentTransaction.hide(zhuyaoFragment);
        }
    }//隐藏掉所有Fragment
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void initPermission(){
        //权限判断和申请
        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }else{
            initFragments();
        }
    }//申请权限

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case mRequestCode:
                initPermission();
                Util.toastShort(MainActivity.this,"您已同意定位权限");
                break;
        }
    }
    private void initFragments(){
        //说明权限都已经通过，可以做你想做的事情去
        mFragmentManager = getSupportFragmentManager();
        zhuyaoFragment = new ZhuyaoFragment();//主要Fragment
        wodeFragment = new WodeFragment();//我的信息Fragment
        fragmentTransaction = mFragmentManager.beginTransaction();//开启一个事务
        fragmentTransaction.add(R.id.fragment_container,zhuyaoFragment,null);//加入主要Fragment
        fragmentTransaction.add(R.id.fragment_container,wodeFragment,null);//加入我的信息Fragment
        hideFragments(fragmentTransaction);//隐藏掉所有Fragment
        fragmentTransaction.show(zhuyaoFragment);//展示主要Fragment
        fragmentTransaction.commit();//提交事务
        main_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = mFragmentManager.beginTransaction();//开启事务
                hideFragments(fragmentTransaction);
                switch (checkedId){
                    case R.id.main_fuwuchanping:
                        fragmentTransaction.show(zhuyaoFragment);//展示主要Fragment
                        break;
                    case R.id.main_wode:
                        fragmentTransaction.show(wodeFragment);//展示我的信息Fragment
                }
                fragmentTransaction.commit();//提交事务
            }
        });
    }//初始化所有Fragment
}