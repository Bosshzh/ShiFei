package com.lanling.view;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.lanling.activity.R;
import com.lanling.bean.UploadData;

public class ManureSortYoujiLinearLayout extends LinearLayout{

    private CheckBox dongwufenbian;//动物粪便
    private CheckBox nongyefeiqiwu;//农业废弃物
    private CheckBox gongyefeiqiwu;//工业废弃物
    private CheckBox shenghuolaji;//生活垃圾
    private CheckBox nigou;//泥垢
    private LayoutInflater layoutInflater;

    public ManureSortYoujiLinearLayout(Context context, final UploadData uploadData) {
        super(context);
        layoutInflater =(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        //然后将commen_item.xml所表示的布局加载进来,也就是一个复选框和一个编辑框
        View view=layoutInflater.inflate(R.layout.manuresort_youjifeiliao_item,this,true);
        dongwufenbian = view.findViewById(R.id.manuresort_youji_dongwufenbian);
        nongyefeiqiwu = view.findViewById(R.id.manuresort_youji_nongyefeiqiwu);
        gongyefeiqiwu = view.findViewById(R.id.manuresort_youji_gongyefeiqiwu);
        shenghuolaji = view.findViewById(R.id.manuresort_youji_shenghuolaji);
        nigou = view.findViewById(R.id.manuresort_youji_nigou);
        dongwufenbian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    uploadData.setDongwufenbian("有");
                }else {
                    uploadData.setDongwufenbian("无");
                }
            }
        });
        nongyefeiqiwu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    uploadData.setNongyefeiqiwu("有");
                }else {
                    uploadData.setNongyefeiqiwu("无");
                }
            }
        });
        gongyefeiqiwu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    uploadData.setGongyefeiqiwu("有");
                }else {
                    uploadData.setGongyefeiqiwu("无");
                }
            }
        });
        shenghuolaji.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    uploadData.setShenghuolaji("有");
                }else {
                    uploadData.setShenghuolaji("无");
                }
            }
        });
        nigou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    uploadData.setNigou("有");
                }else {
                    uploadData.setNigou("无");
                }
            }
        });
    }
}
