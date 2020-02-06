package com.lanling.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnChangeLisener;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.lanling.activity.R;
import com.lanling.bean.UploadData;
import java.lang.reflect.Field;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NumberPickerUtil extends View  implements OnChangeLisener, OnSureLisener {

    private static MNumberPicker numberPicker;//数字选择器
    private static Button affirm;//确定按钮
    private Dialog bottomDialog;//底部弹出框
    private TextView upload_textview;//展示的TextView
    private DateFormat dateFormat;//日期显示格式
    private int type;//类型
    private UploadData uploadData;//上传的数据
    private DatePickDialog Datedialog;//日期选择框

    public NumberPickerUtil(Context context,UploadData uploadData) {
        super(context);
        //数字选择框
        bottomDialog = new Dialog(context,R.style.BottomDialog);
        this.uploadData = uploadData;
        dateFormat = new SimpleDateFormat("农历MM月dd日");
        View contentView = LayoutInflater.from(context).inflate(R.layout.number_picker, null);
        numberPicker = (MNumberPicker) contentView.findViewById(R.id.numberPicker);
        affirm = (Button) contentView.findViewById(R.id.affirm);
        bottomDialog.setContentView(contentView);
        bottomDialog.setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        //日期选择框
        Datedialog = new DatePickDialog(context);
        //设置上下年分限制
        Datedialog.setYearLimt(5);
        //设置标题
        Datedialog.setTitle("选择时间(农历)");
        //设置类型
        Datedialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        Datedialog.setMessageFormat("MM月dd日");
        //设置选择回调
//        Datedialog.setOnChangeLisener(this);
        //设置点击确定按钮回调
        Datedialog.setOnSureLisener(this);
    }

    public void choose_alert_time(final Context context, int min, int max, TextView textView, final int type){
//        this.type = type;
        upload_textview = textView;
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        bottomDialog.show();
        affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.hide();
                int value = numberPicker.getValue();
                switch (type){
                    case 0:
                        uploadData.setManureNumber_first(value);//第一次施肥量
                        break;
                    case 1:
                        uploadData.setManureNumber_second(value);//第二次施肥量
                        break;
                    case 2:
                        uploadData.setManureNumber_third(value);//第三次施肥量
                        break;
                    case 3:
                        uploadData.setWaterNumber_first(value);//第一次浇水量
                        break;
                    case 4:
                        uploadData.setWaterNumber_second(value);//第二次浇水量
                        break;
                    case 5:
                        uploadData.setWaterNumber_third(value);//第三次浇水量
                        break;
                    case 6:
                        uploadData.setHarvest(value);//产量
                        break;
                    case 7:
                        uploadData.setDanfei(value);//氮肥
                        break;
                    case 8:
                        uploadData.setLinfei(value);//磷肥
                        break;
                    case 9:
                        uploadData.setJiafei(value);//钾肥
                        break;
                    case 10:
                        uploadData.setQita(value);//其它
                        break;
                }
                upload_textview.setText(value+"");
            }
        });
    }
    //关掉dialog，避免窗口泄露
    public void closeDialog(){
        bottomDialog.dismiss();
        Datedialog.dismiss();
    }

    public void chooseDate(Context context,TextView textView,int type){
        this.type = type;
        upload_textview = textView;
        Datedialog.show();
    }

    @Override
    public void onChanged(Date date) {

    }

    @Override
    public void onSure(Date date) {
        switch (type) {
            case 0:
                uploadData.setManureDate_first(date);//第一次施肥的时间
                break;
            case 1:
                uploadData.setManureDate_second(date);//第二次施肥的时间
                break;
            case 2:
                uploadData.setManureDate_third(date);//第三次施肥的时间
                break;
            case 3:
                uploadData.setWaterDate_first(date);//第一次浇水的时间
                break;
            case 4:
                uploadData.setWaterDate_second(date);//第二次浇水的时间
                break;
            case 5:
                uploadData.setWaterDate_third(date);//第三次浇水的时间
                break;
        }
        upload_textview.setText(dateFormat.format(date));
    }
}