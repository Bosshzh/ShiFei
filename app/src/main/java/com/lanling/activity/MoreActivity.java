package com.lanling.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lanling.listener.BaseUiListener;
import com.lanling.util.Util;
import com.lanling.view.PerSelectView;
import com.lanling.view.TitlebarView;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoreActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private PerSelectView fankui;//用户反馈
    private PerSelectView xiugai;//资料修改
    private PerSelectView youxiang;//绑定邮箱
    private PerSelectView guanyu;//关于我们
    private PerSelectView lianxi;//联系我们
    private PerSelectView tuichu;//退出登录
    private TitlebarView titlebarView;//标题栏
    private PerSelectView fenxiang;//分享qq
    private Tencent mTencent;// 新建Tencent实例用于调用分享方法
    private BaseUiListener baseUiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        fankui = findViewById(R.id.more_fankui);//用户反馈
        xiugai = findViewById(R.id.more_xiugai);//资料修改
        youxiang = findViewById(R.id.more_youxiang);//绑定邮箱操作
        guanyu = findViewById(R.id.more_guanyu);//关于我们
        lianxi = findViewById(R.id.more_lianxi);//联系我们
        tuichu = findViewById(R.id.more_tuichu);//退出登录操作
        fenxiang = findViewById(R.id.more_fengxiang);//分享qq
        baseUiListener = new BaseUiListener();
        mTencent = Tencent.createInstance("1109968603", getApplicationContext());
        titlebarView = findViewById(R.id.more_titlebarview_title);//标题栏
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        //分享软件
        fenxiang.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                shareToQQ();
            }
        });

        //反馈事件
        fankui.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                if(Util.isLogin(MoreActivity.this) == 0){//如果没有登录的话
                    Util.toastShort(MoreActivity.this,"您暂时没有登录，请先登录");
                    Intent intent = new Intent(MoreActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{//如果已经登录的话
                    Intent intent = new Intent(MoreActivity.this, WebViewActivity.class);
                    intent.putExtra("title", "用户反馈");
                    intent.putExtra("url", "http://www.zhengzhoudaxue.cn:8080/SaveData/feedback.jsp");
                    intent.putExtra("username", sharedPreferences.getString("username","0"));
                    intent.putExtra("openid",sharedPreferences.getString("openid","0"));
                    startActivity(intent);
                }
            }
        });
        //资料修改事件
        xiugai.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                if(Util.isLogin(MoreActivity.this) == 0){
                    //如果没有登录的话
                    Util.toastShort(MoreActivity.this,"您暂时没有登录，请先登录");
                    Intent intent = new Intent(MoreActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MoreActivity.this,WebViewActivity.class);
                    intent.putExtra("title", "修改资料");
                    intent.putExtra("url", "http://www.zhengzhoudaxue.cn:8080/SaveData/xiugai.jsp");
                    intent.putExtra("username", sharedPreferences.getString("username", "0"));
                    intent.putExtra("openid",sharedPreferences.getString("openid","0"));
                    startActivity(intent);
                }
            }
        });
        //绑定邮箱
        youxiang.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                if (Util.isLogin(MoreActivity.this)  != 0){//如果已经登录了的话
                    Intent intent = new Intent(MoreActivity.this, WebViewActivity.class);
                    intent.putExtra("title","绑定邮箱账号");
                    intent.putExtra("url","http://www.zhengzhoudaxue.cn:8080/SaveData/youxiang.jsp");
                    intent.putExtra("username",sharedPreferences.getString("username","0"));
                    intent.putExtra("openid",sharedPreferences.getString("openid","0"));
                    startActivity(intent);
                }else{
                    Util.toastShort(MoreActivity.this,"您暂时没有登录，请先登录");
                    Intent intent = new Intent(MoreActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        //关于我们
        guanyu.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                Intent intent = new Intent(MoreActivity.this, WebViewActivity.class);
                intent.putExtra("title","关于我们");
                intent.putExtra("url","http://www.zhengzhoudaxue.cn:8080/SaveData/aboutus.html");
                intent.putExtra("email",sharedPreferences.getString("email","0"));
                intent.putExtra("openid",sharedPreferences.getString("openid","0"));
                startActivity(intent);
            }
        });
        //联系我们
        lianxi.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                joinQQGroup("WesTIpYQtbu-d-coxm-IC9PlgolmFMyw");
            }
        });

        //退出登录
        tuichu.setOnClickView(new PerSelectView.OnClickView() {
            @Override
            public void onclick() {
                if (Util.isLogin(MoreActivity.this)  != 0){//如果已经登录了的话
                    sharedPreferences.edit().remove("username").remove("openid").remove("photouser").remove("photoqq").apply();
                    Util.toastShort(MoreActivity.this,"退出登录成功");
                    finish();
                }else{
                    Util.toastShort(MoreActivity.this,"您暂时没有登录");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, baseUiListener);
            }
        }
    }

    private Bundle params;
    private void shareToQQ() {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "【土壤施肥信息】");// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "下载【土壤施肥信息采集app】");// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://www.zhengzhoudaxue.cn:8080/SaveData/shifei.apk");// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://www.zhengzhoudaxue.cn:8080/SaveData/images/app_icon.png");// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        mTencent.shareToQQ(MoreActivity.this, params, baseUiListener);
    }

    /****************
     *
     * 发起添加群流程。群号：【施肥app帮助群】(666301956) 的 key 为： WesTIpYQtbu-d-coxm-IC9PlgolmFMyw
     * 调用 joinQQGroup(WesTIpYQtbu-d-coxm-IC9PlgolmFMyw) 即可发起手Q客户端申请加群 【施肥app帮助群】(666301956)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
