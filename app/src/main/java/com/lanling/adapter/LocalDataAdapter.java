package com.lanling.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lanling.activity.LocalDataItemActivity;
import com.lanling.activity.R;
import com.lanling.bean.UploadData;
import com.lanling.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;

public class LocalDataAdapter extends RecyclerView.Adapter<LocalDataAdapter.ViewHolder> {

    private List<UploadData> uploadDatas;
    private SimpleDateFormat simpleDateFormat;
    private OnClickView onClickView;//点击事件监听

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        View local_view;
        public ViewHolder(View view){
            super(view);
            local_view = view;
            title = (TextView) view.findViewById(R.id.local_data_item_title);
            time = (TextView)  view.findViewById(R.id.local_data_item_time);
        }
    }

    public interface OnClickView{
        void onLongClickView(int position);
        void onClickView(int position);
    }

    //监听方法
    public void setOnClickView(OnClickView onClickView){
        this.onClickView = onClickView;
    }

    public LocalDataAdapter(List<UploadData> uploadDatas, SimpleDateFormat simpleDateFormat){
        this.uploadDatas = uploadDatas;
        this.simpleDateFormat = simpleDateFormat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_data_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        UploadData uploadData = uploadDatas.get(position);
        if (uploadData.isUploadOrNot()){//如果数据已经上传，显示的是绿色
            holder.title.setTextColor(Color.parseColor("#00CD00"));
            holder.time.setTextColor(Color.parseColor("#00CD00"));
        }else{//如果数据还没有上传，那显示数据是红色
            holder.title.setTextColor(Color.parseColor("#FF4500"));
            holder.time.setTextColor(Color.parseColor("#FF4500"));
        }
        holder.title.setText(uploadData.getLocation());
        holder.time.setText(simpleDateFormat.format(uploadData.getDate()));
        //点击查看具体信息
        holder.local_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickView.onClickView(position);//调用接口的点击事件
            }
        });
        //长按删除
        holder.local_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickView.onLongClickView(position);//调用接口的点击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadDatas.size();
    }
}
