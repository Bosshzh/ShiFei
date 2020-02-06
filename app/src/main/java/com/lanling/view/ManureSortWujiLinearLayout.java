package com.lanling.view;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lanling.activity.R;
import com.lanling.util.NumberPickerUtil;

import java.util.ArrayList;

public class ManureSortWujiLinearLayout extends LinearLayout{

    private TextView danfei;//氮肥所占百分比
    private TextView linfei;//磷肥所占百分比
    private TextView jiafei;//钾肥所占百分比
    private TextView qitafei;//其它所占百分比
    private LayoutInflater layoutInflater;
    private NumberPickerUtil numberPickerUtil;//数字选择器工具类
    private Context context;
    private LinearLayout linearLayout_danfei;
    private LinearLayout linearLayout_linfei;
    private LinearLayout linearLayout_jiafei;
    private LinearLayout linearLayout_qitafei;

    public ManureSortWujiLinearLayout(final Context context) {
        super(context);
        this.context = context;
        //创建一个LayoutInflater实例
        layoutInflater =(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        //然后将commen_item.xml所表示的布局加载进来,也就是一个复选框和一个编辑框
        View view=layoutInflater.inflate(R.layout.manuresort_wujifeiliao_item,this,true);
        danfei = view.findViewById(R.id.manuresort_wujidanfei_percent);//氮肥所占百分比
        linfei = view.findViewById(R.id.manuresort_wujilinfei_percent);//磷肥所占百分比
        jiafei = view.findViewById(R.id.manuresort_wujijiafei_percent);//钾肥所占百分比
        qitafei = view.findViewById(R.id.manuresort_wujiqita_percent);//其它肥所占百分比
        linearLayout_danfei = view.findViewById(R.id.manuresort_wujidanfei_linearlayout);
        linearLayout_linfei = view.findViewById(R.id.manuresort_wujilinfei_linearlayout);
        linearLayout_jiafei = view.findViewById(R.id.manuresort_wujijiafei_linearlayout);
        linearLayout_qitafei = view.findViewById(R.id.manuresort_wujiqita_linearlayout);
    }

    public void manuresortWujiLinearLayout(NumberPickerUtil numberPickerUtiltest){
        numberPickerUtil = numberPickerUtiltest;
        linearLayout_danfei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.choose_alert_time(context,0,100, danfei,7);
            }
        });
        linearLayout_linfei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.choose_alert_time(context,0,100, linfei,8);
            }
        });
        linearLayout_jiafei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.choose_alert_time(context,0,100, jiafei,9);
            }
        });
        linearLayout_qitafei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.choose_alert_time(context,0,100, qitafei,10);
            }
        });
    }
}
