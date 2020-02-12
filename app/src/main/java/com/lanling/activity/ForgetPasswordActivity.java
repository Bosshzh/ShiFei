package com.lanling.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lanling.util.CountDownTimerUtils;
import com.lanling.util.Util;
import com.lanling.view.TitlebarView;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TitlebarView titlebarView;//标题栏
    private EditText email_edit;//邮箱输入框
    private Button verication_button;//获取验证码按钮
    private EditText verication_edit;//验证码输入框
    private EditText password_edit;//密码输入框
    private Button updatePassword_button;//修改密码按钮
    private String verification_code;//验证码
    private String email;//邮箱账号
    private String password;//密码
    private SharedPreferences.Editor editor;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            switch (msg.what){
                case 0:
                    Util.toastShort(ForgetPasswordActivity.this,"修改密码成功，已自动登录");
                    Intent intent = new Intent(ForgetPasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();//结束当前活动
                    break;
                case 1:
                    Util.toastShort(ForgetPasswordActivity.this,"修改密码失败");
                    break;
                case 2:
                    Util.toastShort(ForgetPasswordActivity.this,"该邮箱账号不存在");
                    break;
                case 3:
                    Util.toastShort(ForgetPasswordActivity.this,"服务器报错，请重试");
                    break;
                case 4:
                    Util.toastShort(ForgetPasswordActivity.this,"验证码发送成功");
                    break;
                case 5:
                    Util.toastShort(ForgetPasswordActivity.this,"验证码发送失败");
                    break;
                case 6:
                    Util.toastShort(ForgetPasswordActivity.this,"验证码发送失败，请重试");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        titlebarView = findViewById(R.id.forget_titlebarview);//标题栏
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {
                finish();//结束当前修改密码的活动
            }

            @Override
            public void rightClick() {

            }
        });
        email_edit = findViewById(R.id.forget_email);//邮箱输入框
        verication_button = findViewById(R.id.forget_verification_button);//获取验证码按钮
        verication_edit = findViewById(R.id.forget_verification);//验证码输入框
        password_edit = findViewById(R.id.forget_password);//密码输入框
        updatePassword_button = findViewById(R.id.forget_button);//修改密码按钮
        updatePassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_edit.getText().toString();//拿到邮箱账号
                password = password_edit.getText().toString();//拿到密码

                //如果用户输入的验证码和我们产生的验证码不相等的话
                if (!verication_edit.getText().toString().equals(verification_code)){
                    Util.toastShort(ForgetPasswordActivity.this,"验证码验证失败，请重试");
                }else if (password.length() == 0 && email.length() == 0){//密码不能为空
                    Util.toastShort(ForgetPasswordActivity.this,"邮箱或密码不能为空");
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email);
                            params.put("password", password);
                            try {
                                String result = Util.sendMessage("http://www.zhengzhoudaxue.cn:8080/SaveData/forget",params,"utf-8");
                                if("1".equals(result)){
                                    handler.sendEmptyMessage(1);//修改密码失败，那么发送消息1
                                }else if("2".equals(result)){
                                    handler.sendEmptyMessage(2);//该账号可能不存在
                                }else if("3".equals(result)){
                                    handler.sendEmptyMessage(3);//服务器报错，请重试
                                }else{
                                   editor.putString("username",result.split("&")[1]).putString("photouser",result.split("&")[2]).apply();
                                    handler.sendEmptyMessage(0);//修改密码成功，那么发送消息0
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }
    public void Forget_Verification_Button(View view){
        verification_code = "";
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(verication_button, 60000, 1000); //倒计时1分钟
        mCountDownTimerUtils.start();
        email = email_edit.getText().toString();//获取收件人的邮箱
        //如果用户输入的是一个email的话
        if (Util.isEmail(email)){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Random ra =new Random();
                    for (int i=0;i<6;i++){
                        verification_code+=ra.nextInt(10);
                    }
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email",email);
                    params.put("verification_code", verification_code);
                    params.put("type","1");
                    try {
                        String result = Util.sendMessage("http://www.zhengzhoudaxue.cn:8080/SaveData/verification",params,"utf-8");
                        if ("4".equals(result)){
                            handler.sendEmptyMessage(4);//验证码发送成功
                        }else if("5".equals(result)){
                            handler.sendEmptyMessage(5);//验证码发送失败
                        }else{
                            handler.sendEmptyMessage(6);//验证码发送失败，未知原因
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }else {
            Util.toastShort(ForgetPasswordActivity.this,"邮箱格式输入有误，请重试");
        }
    }
}