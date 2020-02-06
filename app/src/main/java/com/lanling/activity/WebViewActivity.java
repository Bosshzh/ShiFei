package com.lanling.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lanling.listener.BaseUiListener;
import com.lanling.view.TitlebarView;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;//webView容器
    private Intent intent;//意图
    private String title;//标题
    private String url;//地址
    private String email = "";//邮箱账号
    private String openid = "";//openid
    private TitlebarView titlebarView;//标题栏
    private WebSettings webSettings;
    ProgressBar progressbar;
    private Tencent mTencent;// 新建Tencent实例用于调用分享方法
    private BaseUiListener baseUiListener;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        baseUiListener = new BaseUiListener();
        mTencent = Tencent.createInstance("1109968603", getApplicationContext());
        webView = findViewById(R.id.webview_id);//WebView容器
        titlebarView = findViewById(R.id.webview_titlevarview);//标题栏
        intent = getIntent();

        progressbar = new ProgressBar(WebViewActivity.this);//进度
        title = intent.getStringExtra("title");//标题
        url = intent.getStringExtra("url");//地址
        email = intent.getStringExtra("email");
        openid = intent.getStringExtra("openid");
        titlebarView.setTitle(title);
        if(url == null || url.equals("")){
            url = "http://47.97.193.179:8080/SaveData/error.jsp";//如果url为空的话，显示错误页面
        }else{
            url = url+"?email="+email+"&openid="+openid;
        }
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "【土壤施肥信息采集】");//必填
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "嗨！我在【土壤施肥信息采集app】中发现了一个好东西，开来看看吧");//选填
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
                ArrayList<String> imageUrls = new ArrayList<>();
                imageUrls.add("http://www.zhengzhoudaxue.cn:8080/SaveData/images/app_icon.png");
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);// 必填，否则无法启动分享
                // 分享操作要在主线程中完成
                mTencent.shareToQzone(WebViewActivity.this,params,baseUiListener);
            }
        });//返回按钮

        init();
    }

    //初始化方法
    private void init(){
        //WebView加载Web资源
        webSettings = webView.getSettings();
        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);//启用加速，否则滑动界面不流畅
        webSettings.setJavaScriptEnabled(true);//支持js脚本
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 1.设置WebChromeClient，重写文件上传回调
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }
            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }
            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }
            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(url);// 强制在当前 WebView 中加载 url
//                return true;
//            }
        });
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//            }
//        });
        webView.loadUrl(url);
    }
    // 2.回调方法触发本地选择文件
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");//图片上传
//        i.setType("file/*");//文件上传
        i.setType("*/*");//文件上传
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }
    // 3.选择图片后处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            // Uri result = (((data == null) || (resultCode != RESULT_OK)) ? null : data.getData());
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else {
            //这里uploadMessage跟uploadMessageAboveL在不同系统版本下分别持有了
            //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
            //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            } else if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }
    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }
}
