package com.lanling.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.lanling.util.CountDownTimerUtils;
import com.lanling.util.Util;
import java.io.IOException;
import java.util.Calendar;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText register_username;//用户账号输入框
    private EditText register_password;//密码输入框
    private Button register_button;//点击注册的按钮
    private CheckBox register_isview;//密码框的显示与隐藏
    private SharedPreferences.Editor editor;//保存登录信息
    private String username;//用户账号
    private String password;//密码
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            switch (msg.what){
                case 0:
                    Util.toastShort(RegisterActivity.this,"恭喜注册成功!请登录");
                    editor.putString("username",username);
                    editor.apply();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    intent.putExtra("loginsuccess",1);//登录成功，在MainActivity界面中刷新WodeFragment
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    Util.toastShort(RegisterActivity.this,"该账号可能已经被注册过了，请重试");
                    break;
                case 2:
                    Util.toastShort(RegisterActivity.this,"注册失败");
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        editor = getSharedPreferences("user",MODE_PRIVATE).edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_username = findViewById(R.id.register_username);//用户账号输入框
        register_password = findViewById(R.id.register_password);//密码输入框
        register_button = findViewById(R.id.register_register_button);//注册按钮
        register_isview = findViewById(R.id.register_isview);//控制密码框的显示与隐藏
        //密码框的显示与隐藏
        register_isview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                    register_isview.setButtonDrawable(R.mipmap.xianshi);
                }else{
                    register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
                    register_isview.setButtonDrawable(R.mipmap.yincang);
                }
            }
        });

        //注册按钮
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果用户输入的验证码和我们产生的验证码相等的话
                username = register_username.getText().toString();//拿到用户账号
                password = register_password.getText().toString();//拿到密码
                Util.log("RegisterActivity",username+"     "+password);
                if(password.length() == 0 && username.length() == 0){
                    Util.toastShort(RegisterActivity.this,"用户名或密码不能为空");
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", username);
                            params.put("password", password);

                            try {
                                String result = Util.sendMessage("http://www.zhengzhoudaxue.cn:8080/SaveData/register",params,"utf-8");
                                if ("0".equals(result)){
                                    handler.sendEmptyMessage(0);//如果注册成功，那么发送消息0
                                }else if("1".equals(result)){
                                    handler.sendEmptyMessage(1);//没有注册成功，可能该账号已经被注册过了
                                }else{
                                    handler.sendEmptyMessage(2);//注册失败
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
    //随机生成账号密码
    public void Register_Suiji(View view){
        Calendar calendar = Calendar.getInstance();
        String suiji_string_username = ""+calendar.get(Calendar.YEAR)+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)+(int)(Math.random()*10);
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<6;i++){//生成六位随机密码
            stringBuffer.append((int)(Math.random()*10));
        }
        Util.toastShort(RegisterActivity.this,"生成随机账号，注册后可自动登录!");
        register_username.setText(suiji_string_username);//显示用户名
        register_password.setText(stringBuffer.toString());//显示密码
    }
    public void Register_Back_Button(View v){
        finish();
    }
}