package com.lanling.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lanling.bean.UploadData;
import com.lanling.util.Util;
import com.lanling.view.TitlebarView;

public class LocalDataItemActivity extends AppCompatActivity {

    private TitlebarView titlebarView;//标题栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data_item);
        Intent intent = getIntent();
        UploadData uploadData = (UploadData) intent.getSerializableExtra("uploadData");//获取到传递过来的UploadData对象
        titlebarView = findViewById(R.id.localdataitem_titlebarview_title);//标题栏
        titlebarView.setTitle(uploadData.getLocation());
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {//点击标题栏左侧返回按钮
                Intent intent = new Intent(LocalDataItemActivity.this,LocalDataActivity.class);
                startActivity(intent);
            }

            @Override
            public void rightClick() {//点击标题栏右侧一键上传按钮
                Util.toastShort(LocalDataItemActivity.this,"你点击了一键上传，还没有成功");
            }
        });
    }
}
