package com.lanling.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanling.bean.UploadData;
import com.lanling.util.Util;
import com.lanling.view.TitlebarView;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.File;

public class LocalDataItemActivity extends AppCompatActivity {


    private UploadData uploadData;//从LocalDataActivity传过来的数据
    private int position;
    private TitlebarView titlebarView;//标题栏
    //位置
    private TextView localdataitem_address_textview;//土地具体位置信息
    private TextView localdataitem_latitude_longtitude;//经纬度坐标
    //耕地
    private TextView localdataitem_zuowu_textview;//作物类型
    private TextView localdataitem_leixing_textview;//耕地类型
    private TextView localdataitem_chanliang_textview;//粮食产量
    //肥料的种类
    private TextView localdataitem_feiliaozhonglei_textview;//肥料的种类
    private TextView danfei;//氮肥
    private TextView linfei;//磷肥
    private TextView jiafei;//钾肥
    private TextView qitafei;//其它肥
    private TextView dongwufenbian;//动物粪便
    private TextView nongyefeiqiwu;//农业废弃物
    private TextView gongyefeiqiwu;//工业废弃物
    private TextView shenghuolaji;//生活垃圾
    private TextView nigou;//泥垢
    //施肥的次数
    private TextView localdataitem_shifeicishu_textview;//施肥的次数
    private TextView shifei1;//第一次施肥
    private TextView shifei2;//第二次施肥
    private TextView shifei3;//第三次施肥
    //浇水的次数
    private TextView localdataitem_jiaoshuicishu_linearlayout;//浇水的次数
    private TextView jiaoshui1;//第一次浇水
    private TextView jiaoshui2;//第二次浇水
    private TextView jiaoshui3;//第三次浇水

    //其它
    private TextView localdataitem_dayao_textview;//是否打药
    private TextView localdataitem_chucao_textview;//是否除草

    //土地景观图
    private LinearLayout localdataitem_image1_linearlayout;//土地景观图
    private LinearLayout localdataitem_image2_linearlayout;//现场访谈图
    private Button deleteButton;//删除按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data_item);
        Util.log("LocalDataItemActivity","执行到这里了");
        Intent intent = getIntent();
        uploadData = (UploadData) intent.getSerializableExtra("uploadData");//获取到传递过来的UploadData对象
        position = intent.getIntExtra("position",0);
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

        //位置
        localdataitem_address_textview = findViewById(R.id.localdataitem_address_textview);//具体位置
        localdataitem_latitude_longtitude = findViewById(R.id.localdataitem_latitude_longtitude);//经纬度
        //耕地
        localdataitem_zuowu_textview = findViewById(R.id.localdataitem_zuowu_textview);//种植的作业
        localdataitem_leixing_textview = findViewById(R.id.localdataitem_leixing_textview);//耕地类型
        localdataitem_chanliang_textview = findViewById(R.id.localdataitem_chanliang_textview);//作物产量
        //肥料的种类
        localdataitem_feiliaozhonglei_textview = findViewById(R.id.localdataitem_feiliaozhonglei_textview);//肥料的种类
        danfei = findViewById(R.id.localdataitem_danfei_textview);//氮肥
        linfei = findViewById(R.id.localdataitem_linfei_textview);//磷肥
        jiafei = findViewById(R.id.localdataitem_jiafei_textview);//钾肥
        qitafei = findViewById(R.id.localdataitem_qitafei_textview);//其它肥
        dongwufenbian = findViewById(R.id.localdataitem_dongwufenbian_textview);//动物粪便
        nongyefeiqiwu = findViewById(R.id.localdataitem_nongyefeiqiwu_textview);//农业废弃物
        gongyefeiqiwu = findViewById(R.id.localdataitem_gongyefeiqiwu_textview);//工业废弃物
        shenghuolaji = findViewById(R.id.localdataitem_shenghuolaji_textview);//生活垃圾
        nigou = findViewById(R.id.localdataitem_nigou_textview);//泥垢
        //施肥的次数
        localdataitem_shifeicishu_textview = findViewById(R.id.localdataitem_shifeicishu_textview);//施肥次数
        shifei1 = findViewById(R.id.localdataitem_shifei1_textview);//第一次施肥
        shifei2 = findViewById(R.id.localdataitem_shifei2_textview);//第二次施肥
        shifei3 = findViewById(R.id.localdataitem_shifei3_textview);//第三次施肥
        //浇水的次数
        localdataitem_jiaoshuicishu_linearlayout = findViewById(R.id.localdataitem_jiaoshuicishu_linearlayout);//浇水的次数
        jiaoshui1 = findViewById(R.id.localdataitem_jiaoshui1_textview);//第一次浇水
        jiaoshui2 = findViewById(R.id.localdataitem_jiaoshui2_textview);//第二次浇水
        jiaoshui3 = findViewById(R.id.localdataitem_jiaoshui3_textview);//第三次浇水
        //其它
        localdataitem_dayao_textview = findViewById(R.id.localdataitem_dayao_textview);//是否打药
        localdataitem_chucao_textview = findViewById(R.id.localdataitem_chucao_textview);//是否除草
        //土地景观图
        localdataitem_image1_linearlayout = findViewById(R.id.localdataitem_image1_linearlayout);//土地景观图
        //现场访谈图
        localdataitem_image2_linearlayout = findViewById(R.id.localdataitem_image2_linearlayout);//现场访谈图
        //删除按钮
        deleteButton = findViewById(R.id.localdataitem_delete_button);

        localdataitem_address_textview.setText(uploadData.getLocation());
        localdataitem_latitude_longtitude.setText("纬度："+uploadData.getLatitude()+"，经度："+uploadData.getLongitude());
        localdataitem_zuowu_textview.setText("作物类型："+uploadData.getCrop_sort());
        localdataitem_leixing_textview.setText("土地类型："+uploadData.getLand_sort());
        localdataitem_chanliang_textview.setText("产量："+uploadData.getHarvest());
        localdataitem_feiliaozhonglei_textview.setText("施肥种类："+uploadData.getManure_sort());
        danfei.setText("氮肥："+uploadData.getDanfei()+" %");
        linfei.setText("磷肥："+uploadData.getLinfei()+" %");
        jiafei.setText("钾肥："+uploadData.getJiafei()+" %");
        qitafei.setText("其它肥："+uploadData.getQita()+" %");
        dongwufenbian.setText("动物粪便："+uploadData.getDongwufenbian());
        nongyefeiqiwu.setText("农业废弃物："+uploadData.getNongyefeiqiwu());
        gongyefeiqiwu.setText("工业废弃物："+uploadData.getGongyefeiqiwu());
        shenghuolaji.setText("生活垃圾："+uploadData.getShenghuolaji());
        nigou.setText("泥垢："+uploadData.getNigou());
        localdataitem_shifeicishu_textview.setText("施肥次数："+uploadData.getManure_sort());
        shifei1.setText("第一次时间:"+uploadData.getManureDate_first()+"\t施肥量:"+uploadData.getManureNumber_first()+"公斤/亩");
        shifei2.setText("第二次时间:"+uploadData.getManureDate_second()+"\t施肥量:"+uploadData.getManureNumber_second()+"公斤/亩");
        shifei3.setText("第三次时间:"+uploadData.getManureDate_third()+"\t施肥量:"+uploadData.getManureNumber_third()+"公斤/亩");
        localdataitem_jiaoshuicishu_linearlayout.setText("浇水次数："+uploadData.getWater_number());
        jiaoshui1.setText("第一次时间:"+uploadData.getWaterDate_first()+"\t施肥量:"+uploadData.getManureNumber_first()+"立方/亩");
        jiaoshui2.setText("第二次时间:"+uploadData.getWaterDate_second()+"\t施肥量:"+uploadData.getManureNumber_second()+"立方/亩");
        jiaoshui3.setText("第三次时间:"+uploadData.getWaterDate_third()+"\t施肥量:"+uploadData.getManureNumber_third()+"立方/亩");
        localdataitem_dayao_textview.setText(uploadData.getSpray());
        localdataitem_chucao_textview.setText(uploadData.getWeed());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200,LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10,0,10,0);

        ImageView imageView1 = new ImageView(LocalDataItemActivity.this);
        imageView1.setLayoutParams(layoutParams);
        Bitmap bitmap1 = BitmapFactory.decodeStream(uploadData.getLand_image1());
        imageView1.setImageBitmap(bitmap1);

        ImageView imageView2 = new ImageView(LocalDataItemActivity.this);
        imageView2.setLayoutParams(layoutParams);
        Bitmap bitmap2 = BitmapFactory.decodeStream(uploadData.getLand_image2());
        imageView2.setImageBitmap(bitmap2);

        ImageView imageView3 = new ImageView(LocalDataItemActivity.this);
        imageView3.setLayoutParams(layoutParams);
        Bitmap bitmap3 = BitmapFactory.decodeStream(uploadData.getInterview_image());
        imageView3.setImageBitmap(bitmap3);

        localdataitem_image1_linearlayout.addView(imageView1);
        localdataitem_image1_linearlayout.addView(imageView2);
        localdataitem_image2_linearlayout.addView(imageView3);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(LitePal.deleteAll(UploadData.class,"uploadid=?",""+uploadData.getUploadid()) != 0){
                   Util.toastShort(LocalDataItemActivity.this,"删除成功");
                   Intent intent1 = new Intent(LocalDataItemActivity.this,LocalDataActivity.class);
                   intent1.putExtra("position",position);
                   startActivity(intent1);
               }else{
                   Util.toastShort(LocalDataItemActivity.this,"删除失败");
               }
            }
        });
    }
}