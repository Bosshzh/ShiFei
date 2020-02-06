package com.lanling.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.core.content.ContextCompat;

import com.lanling.activity.R;

/**
 * Created by Administrator on 2018-01-06.
 */
//该类是一个数字选择器，其实NumberPicker继承自LinearLayout布局类,该类同时继承了它的三个重载的方法
public class MNumberPicker extends NumberPicker {


    public MNumberPicker(Context context) {
        super(context);
    }
    public MNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //这三个重载的方法都调用了本类的updateView方法
    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    //该方法，如果view是EditText组件及其子类的话，我们将view改变它的颜色和字体大小
    public void updateView(View view) {
        if (view instanceof EditText) {
            // 文字颜色、大小
            ((EditText) view).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            ((EditText) view).setTextSize(20f);
        }
    }
}
