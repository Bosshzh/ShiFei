package com.lanling.util;

import android.app.Activity;

import com.lanling.bean.UploadData;
import com.mysql.jdbc.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadDataUtil {

    public static void upoload(String url, final UploadData uploadData, final Activity activity){
        OkHttpClient client = new OkHttpClient();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String manureTimeFirst = uploadData.getManureDate_first() == null?"1997-01-01":simpleDateFormat.format(uploadData.getManureDate_first());
        String manureTimeSecond = uploadData.getManureDate_second() == null?"1997-01-01":simpleDateFormat.format(uploadData.getManureDate_first());
        String manureTimeThird = uploadData.getManureDate_third() == null?"1997-01-01":simpleDateFormat.format(uploadData.getManureDate_first());
        String waterTimeFirst = uploadData.getWaterDate_first() == null?"1997-01-01":simpleDateFormat.format(uploadData.getManureDate_first());
        String waterTimeSecond = uploadData.getWaterDate_second() == null?"1997-01-01":simpleDateFormat.format(uploadData.getManureDate_first());
        String waterTimeThird = uploadData.getWaterDate_third() == null?"1997-01-01":simpleDateFormat.format(uploadData.getManureDate_first());
        String upload_time = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        File landImage1 = new File(uploadData.getLand_Iamge1_String());
        File landImage2 = new File(uploadData.getLand_Iamge2_String());
        File interviewImage = new File(uploadData.getInterview_image_String());
        String sql = "insert into message(id,location,province,city,district,latitude,longitude,land_sort,crop_sort,harvest,manure_sort,manuresortdanfei,manuresortlinfei,manuresortjiafei,manuresortqita,dongwufenbian,nongyefeiqiwu,gongyefeiqiwu,shenghuolaji,nigou,water_number,watertimefirst,watertimesecond,watertimethird,waternumberfirst,waternumbersecond,waternumberthird,manure_number,manuretimefirst,manuretimesecond,manuretimethird,manurenumberfirst,manurenumbersecond,manurenumberthird,spray,weed,upload_time, username,openid,landimage1,landimage2,interviewimage)values("+
                "'"+uploadData.getUploadid()+"','"+uploadData.getLocation()+"','"+uploadData.getProvince()+"','"+uploadData.getCity()+"'," +
                "'"+uploadData.getDistrict()+"','"+uploadData.getLatitude()+"','"+uploadData.getLongitude()+"','"+uploadData.getLand_sort()+"'," +
                "'"+uploadData.getCrop_sort()+"','"+uploadData.getHarvest()+"','"+uploadData.getManure_sort()+"','"+uploadData.getDanfei()+"'," +
                "'"+uploadData.getLinfei()+"','"+uploadData.getJiafei()+"','"+uploadData.getQita()+"','"+uploadData.getDongwufenbian()+"'," +
                "'"+uploadData.getNongyefeiqiwu()+"','"+uploadData.getGongyefeiqiwu()+"','"+uploadData.getShenghuolaji()+"','"+uploadData.getNigou()+"'," +
                "'"+uploadData.getWater_number()+"','"+waterTimeFirst+"','"+waterTimeSecond+"','"+waterTimeThird+"'," +
                "'"+uploadData.getWaterNumber_first()+"','"+uploadData.getWaterNumber_second()+"','"+uploadData.getWaterNumber_third()+"','"+uploadData.getManure_number()+"'," +
                "'"+manureTimeFirst+"','"+manureTimeSecond+"','"+manureTimeThird+"','"+uploadData.getManureNumber_first()+"'," +
                "'"+uploadData.getManureNumber_second()+"','"+uploadData.getManureNumber_third()+"','"+uploadData.getSpray()+"','"+uploadData.getWeed()+"'," +
                "'"+upload_time+"','"+uploadData.getUsername()+"','"+uploadData.getOpenid()+"','/images/uploaddata/"+landImage1.getName()+"'," +
                "'/images/uploaddata/"+landImage2.getName()+"','/images/uploaddata/"+interviewImage.getName()+"');";

        MultipartBody multipartBody = null;
        try {
            multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("sql", URLEncoder.encode(sql,"utf-8"))
                    .addFormDataPart("landimage1",landImage1.getName(), RequestBody.create(MediaType.parse("image/jpg"),landImage1))
                    .addFormDataPart("landimage2",landImage2.getName(),RequestBody.create(MediaType.parse("image/jpg"),landImage2))
                    .addFormDataPart("interviewimage",interviewImage.getName(),RequestBody.create(MediaType.parse("image/jpg"),interviewImage))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent","android")
                .header("Content-Type","text/html; charset=utf-8;")
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.toastShort(activity,"网络连接失败");
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();//接收从服务器返回过来的数据
                if (response.isSuccessful()){//服务器响应成功
                    if (!uploadData.isUpload()){
                        uploadData.setUpload(true);
                        uploadData.updateAll("uploadid=?",uploadData.getUploadid());
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.toastShort(activity,"数据采集成功");
                        }
                    });
                }else{//请求失败
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.toastShort(activity,"数据采集失败");
                        }
                    });
                }
            }
        });
    }

    public static void uploadDatas(){

    }


}