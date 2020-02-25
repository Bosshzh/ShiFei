package com.lanling.util;

import android.content.Context;

import com.lanling.bean.UploadData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadDataUtil {

    public static void upoload( String url, UploadData uploadData){
        OkHttpClient client = new OkHttpClient();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File landImage1 = new File(uploadData.getLand_Iamge1_String());
        File landImage2 = new File(uploadData.getLand_Iamge2_String());
        File interviewImage = new File(uploadData.getInterview_image_String());
        String sql = "insert into message(" +
                "id,location,province,city,district,latitude,longitude,land_sort," +
                "crop_sort,harvest,manure_sort,manuresortdanfei,manuresortlinfei,manuresortjiafei,manuresortqita,dongwufenbian," +
                "nongyefeiqiwu,gongyefeiqiwu,shenghuolaji,nigou,water_number,watertimefirst,watertimesecond,watertimethird," +
                "waternumberfirst,waternumbersecond,waternumberthird,manure_number,manuretimefirst,manuretimesecond,manuretimethird,manurenumberfirst," +
                "manurenumbersecond,manurenumberthird,spray,weed,upload_time,username,opneid,landimage1,landimage2,interviewimage) values " +
                "('"+uploadData.getUploadid()+"','"+uploadData.getLocation()+"','"+uploadData.getProvince()+"','"+uploadData.getCity()+"'," +
                "'"+uploadData.getDistrict()+"','"+uploadData.getLatitude()+"','"+uploadData.getLongitude()+"','"+uploadData.getLand_sort()+"'," +
                "'"+uploadData.getCrop_sort()+"','"+uploadData.getHarvest()+"','"+uploadData.getManure_sort()+"','"+uploadData.getDanfei()+"'," +
                "'"+uploadData.getLinfei()+"','"+uploadData.getJiafei()+"','"+uploadData.getQita()+"','"+uploadData.getDongwufenbian()+"'," +
                "'"+uploadData.getNongyefeiqiwu()+"','"+uploadData.getGongyefeiqiwu()+"','"+uploadData.getShenghuolaji()+"','"+uploadData.getNigou()+"'," +
                "'"+uploadData.getWater_number()+"','"+uploadData.getWaterDate_first()+"','"+uploadData.getWaterDate_second()+"','"+uploadData.getWaterDate_third()+"'," +
                "'"+uploadData.getWaterNumber_first()+"','"+uploadData.getWaterNumber_second()+"','"+uploadData.getWaterNumber_third()+"','"+uploadData.getManure_number()+"'," +
                "'"+uploadData.getManureDate_first()+"','"+uploadData.getManureDate_second()+"','"+uploadData.getManureDate_third()+"','"+uploadData.getManureNumber_first()+"'," +
                "'"+uploadData.getManureNumber_second()+"','"+uploadData.getManureNumber_third()+"','"+uploadData.getSpray()+"','"+uploadData.getWeed()+"'," +
                "'"+uploadData.getDate()+"','"+uploadData.getUsername()+"','"+uploadData.getOpenid()+"','/images/uploaddata/"+landImage1.getName()+"'," +
                "'/images/uploaddata/"+landImage2.getName()+"','/images/uploaddata/"+interviewImage.getName()+"')";
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sql",sql)
                .addFormDataPart("landimage1",landImage1.getName(),RequestBody.create(MediaType.parse("image/jpg"),landImage1))
                .addFormDataPart("landimage2",landImage2.getName(),RequestBody.create(MediaType.parse("image/jpg"),landImage2))
                .addFormDataPart("interviewimage",interviewImage.getName(),RequestBody.create(MediaType.parse("image/jpg"),interviewImage))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接失败
                Util.log("UploadDataUtil","网络连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){//服务器响应成功
                    Util.log("UploadDataUtil","服务器响应成功");
                }else{//请求失败
                    Util.log("UploadDataUtil","服务器响应失败");
                }
            }
        });
    }
}



//                .addFormDataPart("location",uploadData.getLocation())
//                .addFormDataPart("province",uploadData.getProvince())
//                .addFormDataPart("city",uploadData.getCity())
//                .addFormDataPart("district",uploadData.getDistrict())
//                .addFormDataPart("latitude",uploadData.getLatitude()+"")
//                .addFormDataPart("longitude",uploadData.getLongitude()+"")
//                .addFormDataPart("crop_sort",uploadData.getCrop_sort())
//                .addFormDataPart("land_sort",uploadData.getLand_sort())
//                .addFormDataPart("harvest",uploadData.getHarvest()+"")
//                .addFormDataPart("danfei",uploadData.getDanfei()+"")
//                .addFormDataPart("linfei",uploadData.getLinfei()+"")
//                .addFormDataPart("jiafei",uploadData.getJiafei()+"")
//                .addFormDataPart("qita",uploadData.getQita()+"")
//                .addFormDataPart("dongwufenbian",uploadData.getDongwufenbian())
//                .addFormDataPart("nongyefeiqiwu",uploadData.getNongyefeiqiwu())
//                .addFormDataPart("gongyefeiqiwu",uploadData.getGongyefeiqiwu())
//                .addFormDataPart("shenghuolaji",uploadData.getShenghuolaji())
//                .addFormDataPart("nigou",uploadData.getNigou())
//                .addFormDataPart("water_number",uploadData.getWater_number()+"")
//                .addFormDataPart("waterDate_first",simpleDateFormat.format(uploadData.getWaterDate_first()))
//                .addFormDataPart("waterDate_sedond",simpleDateFormat.format(uploadData.getWaterDate_second()))
//                .addFormDataPart("waterDate_third",simpleDateFormat.format(uploadData.getWaterDate_third()))
//                .addFormDataPart("waterNumber_first",uploadData.getWaterNumber_first()+"")
//                .addFormDataPart("waterNumber_sedond",uploadData.getWaterNumber_second()+"")
//                .addFormDataPart("waterNumber_third",uploadData.getWaterNumber_third()+"")
//                .addFormDataPart("ManureDate_first",simpleDateFormat.format(uploadData.getManureDate_first()))
//                .addFormDataPart("ManureDate_sedond",simpleDateFormat.format(uploadData.getManureDate_second()))
//                .addFormDataPart("ManureDate_third",simpleDateFormat.format(uploadData.getManureDate_third()))
//                .addFormDataPart("ManureNumber_first",uploadData.getManureNumber_first()+"")
//                .addFormDataPart("ManureNumber_sedond",uploadData.getManureNumber_second()+"")
//                .addFormDataPart("ManureNumber_third",uploadData.getManureNumber_third()+"")
//                .addFormDataPart("spray",uploadData.getSpray())
//                .addFormDataPart("weed",uploadData.getWeed())
//                .addFormDataPart("land_image1",landImage1.getName(), RequestBody.create(MediaType.parse("image/jpg"),landImage1))
//                .addFormDataPart("land_iamge2",landImage2.getName(), RequestBody.create(MediaType.parse("image/jpg"),landImage2))
//                .addFormDataPart("interview_image",interviewImage.getName(), RequestBody.create(MediaType.parse("image/jpg"),interviewImage))
//                .addFormDataPart("date",simpleDateFormat.format(uploadData.getDate()))
//                .addFormDataPart("username",uploadData.getUsername())
//                .addFormDataPart("openid",uploadData.getOpenid())