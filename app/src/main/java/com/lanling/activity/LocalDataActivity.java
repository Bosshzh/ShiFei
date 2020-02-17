package com.lanling.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.lanling.adapter.LocalDataAdapter;
import com.lanling.bean.UploadData;
import com.lanling.util.Util;
import com.lanling.view.TitlebarView;
import org.litepal.LitePal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 用户已上传的数据会自动存储在本地
 * 用户未上传的数据也会在这里保存
 */
public class LocalDataActivity extends AppCompatActivity {

    private TitlebarView titlebarView;//标题栏
    private List<UploadData> uploadDatas;//集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data);
        titlebarView = findViewById(R.id.local_titlebarview_title);//本地数据标题栏
        //标题栏监听事件
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {
                //返回主活动
                finish();
            }
            @Override
            public void rightClick() {
                Util.toastShort(LocalDataActivity.this,"你点击了一键上传按钮");
            }
        });

        uploadDatas = LitePal.findAll(UploadData.class);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.local_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        LocalDataAdapter localDataAdapter = new LocalDataAdapter(uploadDatas,new SimpleDateFormat("yyyy年MM月dd日"));
        //适配器点击事件
        localDataAdapter.setOnClickView(new LocalDataAdapter.OnClickView() {
            @Override
            public void onLongClickView(int position) {//长按某一项的点击事件
                //弹出框询问是否删除
                AlertDialog alertDialog = new AlertDialog.Builder(LocalDataActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除本条数据")
                        .setIcon(R.mipmap.app_icon)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Util.toastShort(LocalDataActivity.this,"你点击了删除，暂时还没有实现");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                alertDialog.show();
            }

            @Override
            public void onClickView(int position) {//点击某一项的点击事件
                //点击查看该具体数据
                Intent intent = new Intent(LocalDataActivity.this,LocalDataItemActivity.class);
                intent.putExtra("uploadData", uploadDatas.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(localDataAdapter);
    }
}