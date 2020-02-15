package com.lanling.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import org.litepal.crud.LitePalSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

public class UploadData extends LitePalSupport {

    private String location;//地理位置
    private String province;//省级
    private String city;//市级
    private String district;//县级
    private double latitude = 0.0;//纬度
    private double longitude = 0.0;//经度
    private String crop_sort = "小麦";//作物种类
    private String land_sort = "水田";//土地类型
    private int harvest;//产量
    //肥料的种类
    private String manure_sort = "不施肥";//肥料的种类
    private int danfei;//氮肥
    private int linfei;//磷肥
    private int jiafei;//钾肥
    private int qita;//其它肥
    private String dongwufenbian = "无";//动物粪便
    private String nongyefeiqiwu = "无";//农业废弃物
    private String gongyefeiqiwu = "无";//工业废弃物
    private String shenghuolaji = "无";//生活垃圾
    private String nigou = "无";//泥垢
    //浇水的次数
    private int water_number;//浇水次数
    private Date waterDate_first;//第一次浇水时间
    private Date waterDate_second;//第二次浇水时间
    private Date waterDate_third;//第三次浇水时间
    private int waterNumber_first;//第一次浇水量
    private int waterNumber_second;//第二次浇水量
    private  int waterNumber_third;//第三次浇水量
    //第一次施肥的时间
    private int manure_number;//施肥次数
    private Date manureDate_first;//第一次施肥时间
    private Date manureDate_second;//第二次施肥时间
    private Date manureDate_third;//第三次施肥时间
    private int manureNumber_first;//第一次施肥量
    private int manureNumber_second;//第二次施肥量
    private int manureNumber_third;//第三次施肥量

    private String spray = "不打药";//是否打药
    private String weed = "不除草";//是否除草
    private String land_image1;//土地景观图1
    private String land_image2;//土地景观图2
    private String interview_image;//用户访谈图
    private Date date;//上传时间
    private String username;//邮箱账号
    private String openid;//openid
    private boolean uploadOrNot;//用户是否已经上传数据

    public boolean isUploadOrNot() {
        return uploadOrNot;
    }

    public void setUploadOrNot(boolean uploadOrNot) {
        this.uploadOrNot = uploadOrNot;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void clearManureSort(){
        this.danfei = 0;
        this.linfei = 0;
        this.jiafei = 0;
        this.qita = 0;
        this.dongwufenbian = "无";
        this.nongyefeiqiwu = "无";
        this.gongyefeiqiwu = "无";
        this.shenghuolaji = "无";
        this.nigou = "无";
    }//清空掉uploadData对象肥料类型的数据
    public void clearWaterNumber(){
        this.waterDate_first = null;
        this.waterDate_second = null;
        this.waterDate_third = null;
        this.waterNumber_first = 0;
        this.waterNumber_second = 0;
        this.waterNumber_third = 0;
    }//清空掉uploadData对象浇水次数的数据
    public void clearManureNumber(){
        this.manureDate_first = null;
        this.manureDate_second = null;
        this.manureDate_third = null;
        this.manureNumber_first = 0;
        this.manureNumber_second = 0;
        this.manureNumber_third = 0;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public int getManure_number() {
        return manure_number;
    }
    public void setManure_number(int manure_number) {
        this.manure_number = manure_number;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getCrop_sort() {
        return crop_sort;
    }
    public void setCrop_sort(String crop_sort) {
        this.crop_sort = crop_sort;
    }
    public String getLand_sort() {
        return land_sort;
    }
    public void setLand_sort(String land_sort) {
        this.land_sort = land_sort;
    }
    public int getHarvest() {
        return harvest;
    }
    public void setHarvest(int harvest) {
        this.harvest = harvest;
    }
    public String getManure_sort() {
        return manure_sort;
    }
    public void setManure_sort(String manure_sort) {
        this.manure_sort = manure_sort;
    }
    public String getSpray() {
        return spray;
    }
    public void setSpray(boolean spray) {
        if (spray){
            this.spray = "打药";
        }else {
            this.spray = "不打药";
        }
    }
    public String getWeed() {
        return weed;
    }
    public void setWeed(boolean weed) {
        if (weed){
            this.weed = "除草";
        }else {
            this.weed = "不除草";
        }
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public void setSpray(String spray) {
        this.spray = spray;
    }

    public void setWeed(String weed) {
        this.weed = weed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InputStream getLand_image1(){
        try {
            File outputFile = new File(Environment.getExternalStorageDirectory(),
                    "compressPic.jpg");
            final String compressImage = compressImage(land_image1, outputFile, 30);
            return new FileInputStream(new File(compressImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setLand_image1(String land_image1) {
        this.land_image1 = land_image1;
    }
    public InputStream getLand_image2(){
        try {
            File outputFile = new File(Environment.getExternalStorageDirectory(),
                    "compressPic.jpg");
            final String compressImage = compressImage(land_image2, outputFile, 30);
            return new FileInputStream(new File(compressImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setLand_image2(String land_image2) {
        this.land_image2 = land_image2;
    }
    public InputStream getInterview_image(){
        try {
            File outputFile = new File(Environment.getExternalStorageDirectory(),
                    "compressPic.jpg");
            final String compressImage = compressImage(interview_image, outputFile, 30);
            return new FileInputStream(new File(compressImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setInterview_image(String interview_image) {
        this.interview_image = interview_image;
    }

    public int getDanfei() {
        return danfei;
    }
    public void setDanfei(int danfei) {
        this.danfei = danfei;
    }
    public int getLinfei() {
        return linfei;
    }
    public void setLinfei(int linfei) {
        this.linfei = linfei;
    }
    public int getJiafei() {
        return jiafei;
    }
    public void setJiafei(int jiafei) {
        this.jiafei = jiafei;
    }
    public int getQita() {
        return qita;
    }
    public void setQita(int qita) {
        this.qita = qita;
    }
    public String getNigou() {
        return nigou;
    }
    public void setNigou(String nigou) {
        this.nigou = nigou;
    }
    public int getManureNumber_first() {
        return manureNumber_first;
    }
    public void setManureNumber_first(int manureNumber_first) {
        this.manureNumber_first = manureNumber_first;
    }
    public int getManureNumber_second() {
        return manureNumber_second;
    }
    public void setManureNumber_second(int manureNumber_second) {
        this.manureNumber_second = manureNumber_second;
    }
    public int getManureNumber_third() {
        return manureNumber_third;
    }
    public void setManureNumber_third(int manureNumber_third) {
        this.manureNumber_third = manureNumber_third;
    }
    public Date getManureDate_first() {
        return manureDate_first;
    }
    public void setManureDate_first(Date manureDate_first) {
        this.manureDate_first = manureDate_first;
    }
    public Date getManureDate_second() {
        return manureDate_second;
    }
    public void setManureDate_second(Date manureDate_second) {
        this.manureDate_second = manureDate_second;
    }
    public Date getManureDate_third() {
        return manureDate_third;
    }
    public void setManureDate_third(Date manureDate_third) {
        this.manureDate_third = manureDate_third;
    }
    public int getWaterNumber_first() {
        return waterNumber_first;
    }
    public void setWaterNumber_first(int waterNumber_first) {
        this.waterNumber_first = waterNumber_first;
    }
    public int getWaterNumber_second() {
        return waterNumber_second;
    }
    public void setWaterNumber_second(int waterNumber_second) {
        this.waterNumber_second = waterNumber_second;
    }
    public int getWaterNumber_third() {
        return waterNumber_third;
    }
    public void setWaterNumber_third(int waterNumber_third) {
        this.waterNumber_third = waterNumber_third;
    }
    public Date getWaterDate_first() {
        return waterDate_first;
    }
    public void setWaterDate_first(Date waterDate_first) {
        this.waterDate_first = waterDate_first;
    }
    public Date getWaterDate_second() {
        return waterDate_second;
    }
    public void setWaterDate_second(Date waterDate_second) {
        this.waterDate_second = waterDate_second;
    }
    public Date getWaterDate_third() {
        return waterDate_third;
    }
    public void setWaterDate_third(Date waterDate_third) {
        this.waterDate_third = waterDate_third;
    }
    public static String compressImage(String filePath, File outputFile, int quality)  {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            }else{
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        }catch (Exception e){}
        return outputFile.getPath();
    }
    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);//计算缩放比
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public String getDongwufenbian() {
        return dongwufenbian;
    }

    public void setDongwufenbian(String dongwufenbian) {
        this.dongwufenbian = dongwufenbian;
    }

    public String getNongyefeiqiwu() {
        return nongyefeiqiwu;
    }

    public void setNongyefeiqiwu(String nongyefeiqiwu) {
        this.nongyefeiqiwu = nongyefeiqiwu;
    }

    public String getGongyefeiqiwu() {
        return gongyefeiqiwu;
    }

    public void setGongyefeiqiwu(String gongyefeiqiwu) {
        this.gongyefeiqiwu = gongyefeiqiwu;
    }

    public String getShenghuolaji() {
        return shenghuolaji;
    }

    public void setShenghuolaji(String shenghuolaji) {
        this.shenghuolaji = shenghuolaji;
    }

    public int getWater_number() {
        return water_number;
    }

    public void setWater_number(int water_number) {
        this.water_number = water_number;
    }
}