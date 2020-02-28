package com.lanling.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.lanling.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HUPENG on 2016/9/21.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏以及状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //在欢迎界面的时候判断是否登录
        if (Util.isLogin(WelcomeActivity.this) == 2){
            //如果是第三方登录的话
            final String openid = sharedPreferences.getString("openid","0");
            //开启一个新的线程获取头像信息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Map<String,String> params = new HashMap<>();
                    params.put("type","1");
                    params.put("openid",openid);
                    try {
                        String result = Util.sendMessage("http://www.zhengzhoudaxue.cn:8080/SaveData/photo",params,"utf-8");
                        if (!"0".equals(result)){
                            editor.putString("photoqq","http://www.zhengzhoudaxue.cn:8080"+result).apply();//存入头像信息
                            handler.sendEmptyMessage(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            handler.sendEmptyMessageDelayed(0,1000);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getHome();
        }
    };

    public void getHome(){
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}