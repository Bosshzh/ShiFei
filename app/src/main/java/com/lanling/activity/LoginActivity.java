package com.lanling.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lanling.util.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username;//用户账号输入框
    private EditText password;//密码输入框
    private Button login_button;//登录按钮
    private String username_text;//用户账号
    private String password_text;//密码
    private TextView forgetpassword_text;//忘记密码按钮
    private static Tencent mTencent;//腾讯
    private boolean isServerSideLogin = false;//变量
    private SharedPreferences.Editor editor;//SharedPreferences存储
    private static UserInfo userInfo;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    Util.toastShort(LoginActivity.this,"登录成功！");
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("loginsuccess",1);//登录成功，在MainActivity界面中刷新WodeFragment
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    Util.toastShort(LoginActivity.this,"密码错误，请重试");
                    break;
                case 2:
                    Util.toastShort(LoginActivity.this,"该账号可能不存在，请重试");
                    break;
                case 3:
                    Util.toastShort(LoginActivity.this,"登录出错，请重试");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.login_username);//用户账号输入框
        password = findViewById(R.id.login_password);//密码输入框
        login_button = findViewById(R.id.login_button);//登录按钮
        forgetpassword_text = findViewById(R.id.forgetpassword_text);//忘记密码文本
        //登录按钮的点击事件
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_text = username.getText().toString();//拿到用户账号
                password_text = password.getText().toString();//拿到密码
                if(password_text.length() == 0 && username_text.length() == 0){
                    Util.toastShort(LoginActivity.this,"请输入邮箱");
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            final Map<String,String> params = new HashMap<>();
                            params.put("username",username_text);//存入用户账号
                            params.put("password",password_text);//存入密码
                            try {
                                String result = Util.sendMessage("http://www.zhengzhoudaxue.cn:8080/SaveData/login",params,"utf-8");
                                if("1".equals(result)){
                                    mHandler.sendEmptyMessage(1);//密码错误
                                }else if("2".equals(result)){
                                    mHandler.sendEmptyMessage(2);//该账号可能不存在
                                }else if("3".equals(result)){
                                    mHandler.sendEmptyMessage(3);//登录出错
                                }else{
                                    editor.putString("username",username_text).putString("photouser",result.split("&")[1]).apply();
                                    mHandler.sendEmptyMessage(0);//如果登录成功，那么发送消息0
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
        //忘记按钮点击事件
        forgetpassword_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    //QQ快速登录
    public void Login_QQ(View view) {
        mTencent = Tencent.createInstance("1109968603", this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            isServerSideLogin = false;
            Util.log("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                Util.log("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        password.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Util.log("TAG", "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Login_Register_Button(View v){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    public void Login_Back_Button(View v){
        finish();
    }

    //初始化OPENID和TOKEN值（为了得了用户信息）
    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            final String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
                QQToken qqToken = mTencent.getQQToken();
                userInfo = new UserInfo(getApplicationContext(),qqToken);
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        final JSONObject oo= (JSONObject) response;
                            new Thread(){
                                @Override
                                public void run() {
                                    final Map<String,String> params = new HashMap<>();
                                    params.put("openid",openId);//用户ID
                                    try {
                                        params.put("name",oo.getString("nickname"));//用户昵称
                                        params.put("sex",oo.getString("gender"));//用户性别
                                        params.put("province",oo.getString("province"));//省份
                                        params.put("city",oo.getString("city"));//城市
                                        params.put("photo",oo.getString("figureurl_2"));//图片路径
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        String result = Util.sendMessage("http://www.zhengzhoudaxue.cn:8080/SaveData/loginqq",params,"utf-8");
                                        if ("0".equals(result)) {//第三方登录成功
                                            editor.putString("openid",mTencent.getOpenId())
                                                    .putString("photoqq",oo.getString("figureurl_2"))
                                                    .putString("name",oo.getString("nickname")).apply();
                                            mHandler.sendEmptyMessage(0);
                                        }else{//第三方登录失败
                                            mHandler.sendEmptyMessage(3);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                    }
                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        } catch(Exception e) {
        }
    }

    IUiListener loginListener = new LoginBaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Util.log("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);



        }
    };

    //实现回调
    public class LoginBaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.toastShort(LoginActivity.this,"返回为空，登录失败！");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.toastShort(LoginActivity.this,"返回为空，登录失败！");
                return;
            }
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            Util.toastShort(LoginActivity.this,"onError："+e.errorDetail);
        }

        @Override
        public void onCancel() {
            Util.toastShort(LoginActivity.this,"取消了第三方登录");
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }
}