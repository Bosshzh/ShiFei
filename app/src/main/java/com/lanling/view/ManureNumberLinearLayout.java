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
import java.util.Date;

public class ManureNumberLinearLayout extends LinearLayout{
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private LayoutInflater layoutInflater;
    private NumberPickerUtil numberPickerUtil;
    private Context context;

    public ManureNumberLinearLayout(Context context) {
        super(context);
        //创建一个LayoutInflater实例
        layoutInflater =(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        //然后将commen_item.xml所表示的布局加载进来,也就是一个复选框和一个编辑框
        View view=layoutInflater.inflate(R.layout.shifei_item,this,true);
        textView1 = view.findViewById(R.id.shifei_item_textview1);//施肥1
        textView2 = view.findViewById(R.id.shifei_item_textview2);//施肥时间
        textView3 = view.findViewById(R.id.shifei_item_textview3);//施肥量
        textView4 = view.findViewById(R.id.shifei_item_textview4);//单位
        this.context = context;
    }

    public void manureNumberLinearLayout(final int index, NumberPickerUtil numberPickerUtiltest){
        numberPickerUtil = numberPickerUtiltest;
        textView1.setText("第"+(index+1)+"次");
        //为将复选框设置时间监听器
        textView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.chooseDate(context,textView2,index);
            }
        });
        textView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.choose_alert_time(context,0,100, textView3,index);
            }
        });
    }
    public void waterNumberLinearLayout(int index,final int type,NumberPickerUtil numberPickerUtiltest){
        textView4.setText("立方/亩");
        numberPickerUtil = numberPickerUtiltest;
        textView1.setText("第"+(index+1)+"次");
        textView2.setText("浇水时间(农历)");
        textView3.setText("浇水量");
        //为将复选框设置时间监听器
        textView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.chooseDate(context,textView2,type);
            }
        });
        textView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerUtil.choose_alert_time(context,10,50, textView3,type);
            }
        });
    }

}