package com.lanling.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.lanling.bean.UploadData;
import com.lanling.util.NumberPickerUtil;
import com.lanling.util.UploadDataUtil;
import com.lanling.util.Util;
import com.lanling.view.ManureNumberLinearLayout;
import com.lanling.view.ManureSortWujiLinearLayout;
import com.lanling.view.ManureSortYoujiLinearLayout;
import com.lanling.view.TitlebarView;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UploadDataActivity extends Activity{
    //所在地址
    private TextView mTvAddress;//地理位置
    private TextView latitude_longtitude;//经纬度
    private static UploadData uploadData;//将要上传的数据
    private Intent intent2;//获取数据

    //数字选择器
    private Button affirm;//确定按钮
    private NumberPickerUtil numberPickerUtil;//数字选择器工具类

    //标题栏
    private TitlebarView upload_titlebarView;//标题栏

    //作物类型
    private Spinner upload_cropsort_spinner;//作物类型

    //土地类型
    private Spinner upload_landsort_spinner;//土地类型

    //粮食产量
    private TextView upload_chanliang_textview;//显示粮食产量

    //肥料种类
    private Spinner manuresort_spinner;//肥料种类
    private LinearLayout upload_manuresort_linearlaout;//单个肥料种类布局
    private ManureSortWujiLinearLayout manureSortWujiLinearLayout;//单个无机肥料种类的控件
    private ManureSortYoujiLinearLayout manureSortYoujiLinearLayout;//单个有机肥料种类的控件

    //一亩地施肥量
    private Spinner manurenumber_spinner;//作物施肥次数
    private ManureNumberLinearLayout shiFeiLinearLayout;//跟肥料相关的布局文件
    private LinearLayout upload_shifeicishu_linearlayout;//装每一次施肥布局的Linearlayout

    //浇水
    private Spinner upload_waternumber_spinner;//浇水下拉列表框
    private LinearLayout upload_waternumber_linearlayout;//放每一次浇水组件的父布局

    //是否除草和打药
    private CheckBox upload_spray_checkbox;//是否打药
    private CheckBox upload_weed_checkbox;//是否除草

    //拍照
    public static final int UPLOAD_LANDPHOTO = 1;// 土地景观图
    public static final int UPLOAD_INTERVIEWPHOTO = 2;//现场访谈图
    private ImageView upload_add_landphoto;//添加照片的ImageView
    private ImageView upload_add_interviewphoto;//添加现场访谈图片
    private Uri imageUri;
    private ContentValues contentValues;//内容
    private ImageView landImageView1;//土地景观图1
    private ImageView landImageView2;//土地景观图2
    private ImageView interviewImageView;//访谈照片图
    public static File tempFile;//图片文件对象
    private LinearLayout upload_landPhoto_linearlayout;//存放土地景观图父容器
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout upload_interviewphoto_linearlayout;//访谈现场图片容器
    private ProgressDialog progressDialog;//上传进度条
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        uploadData = new UploadData();//上传数据的封装的类
        intent2 = getIntent();//通过Intent接收到数据
        //数字选择器工具类
        numberPickerUtil = new NumberPickerUtil(UploadDataActivity.this,uploadData);//数字选择器工具类
        //标题栏
        upload_titlebarView = findViewById(R.id.upload_titlebarview_title);//标题栏
        upload_titlebarView.setOnViewClick(new TitlebarView.onViewClick() {//标题栏监听器
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });//标题栏监听器
        //编号
        uploadData.setUploadid(System.currentTimeMillis()+""+(int)(Math.random()*10));
        //地址
        mTvAddress = (TextView) findViewById(R.id.tv_address);//地址
        mTvAddress.setText(intent2.getStringExtra("addrStr"));
        latitude_longtitude = (TextView) findViewById(R.id.latitude_longtitude);//经纬度
        latitude_longtitude.setText("纬度:"+intent2.getDoubleExtra("latitude",0.0d)+",经度:"+intent2.getDoubleExtra("longtitude",0.0d));//经纬度
//        location.setRightText(intent2.getStringExtra("addrStr"));
        uploadData.setLocation(intent2.getStringExtra("addrStr"));
        uploadData.setProvince(intent2.getStringExtra("province"));
        uploadData.setCity(intent2.getStringExtra("city"));
        uploadData.setDistrict(intent2.getStringExtra("district"));
        uploadData.setLatitude(intent2.getDoubleExtra("latitude",0.0d));
        uploadData.setLongitude(intent2.getDoubleExtra("longtitude",0.0d));
        uploadData.setUsername(intent2.getStringExtra("username"));
        uploadData.setOpenid(intent2.getStringExtra("openid"));
        //作物类型
        upload_cropsort_spinner = findViewById(R.id.upload_cropsort_spinner);//作物类型
        upload_cropsort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String[] cropsorts = getResources().getStringArray(R.array.zuowu);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uploadData.setCrop_sort(cropsorts[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //土地类型
        upload_landsort_spinner = findViewById(R.id.upload_landsort_spinner);//土地类型
        upload_landsort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String[] landsorts = getResources().getStringArray(R.array.tudi);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uploadData.setLand_sort(landsorts[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //粮食产量
        upload_chanliang_textview = findViewById(R.id.upload_chanliang_textview);//显示粮食产量的TextView
        //肥料种类
        upload_manuresort_linearlaout = findViewById(R.id.upload_manuresort_linearlayout);//装在肥料每一个种类的布局文件
        manuresort_spinner = findViewById(R.id.upload_manuresort_spinner);//肥料种类
        manuresort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] manuresorts = getResources().getStringArray(R.array.manure);//肥料种类
                uploadData.setManure_sort(manuresorts[position]);
                uploadData.clearManureSort();//清理掉之前选择的数据
                if(position == 0){
                    upload_manuresort_linearlaout.removeAllViews();
                }else if (position == 1){//无机肥料
                    upload_manuresort_linearlaout.removeAllViews();
                    manureSortWujiLinearLayout = new ManureSortWujiLinearLayout(UploadDataActivity.this);//无机肥料控件
                    manureSortWujiLinearLayout.manuresortWujiLinearLayout(numberPickerUtil);
                    upload_manuresort_linearlaout.addView(manureSortWujiLinearLayout);
                }else if (position == 2){//有机肥料
                    upload_manuresort_linearlaout.removeAllViews();
                    manureSortYoujiLinearLayout = new ManureSortYoujiLinearLayout(UploadDataActivity.this,uploadData);
                    upload_manuresort_linearlaout.addView(manureSortYoujiLinearLayout);
                }else {//无机有机肥料
                    upload_manuresort_linearlaout.removeAllViews();
                    manureSortWujiLinearLayout = new ManureSortWujiLinearLayout(UploadDataActivity.this);//无机肥料控件
                    manureSortWujiLinearLayout.manuresortWujiLinearLayout(numberPickerUtil);
                    upload_manuresort_linearlaout.addView(manureSortWujiLinearLayout);
                    manureSortYoujiLinearLayout = new ManureSortYoujiLinearLayout(UploadDataActivity.this,uploadData);
                    upload_manuresort_linearlaout.addView(manureSortYoujiLinearLayout);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//肥料种类监听事件

        //肥料次数
        manurenumber_spinner = findViewById(R.id.upload_manurenumber_spinner);//施肥次数
        upload_shifeicishu_linearlayout = findViewById(R.id.upload_shifeicishu_linearlayout);//装每次施肥具体信息的布局
        manurenumber_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upload_shifeicishu_linearlayout.removeAllViews();
                uploadData.clearManureNumber();//清理掉之前选择的数据
                uploadData.setManure_number(position);
                for (int i=0;i<position;i++){
                    shiFeiLinearLayout = new ManureNumberLinearLayout(UploadDataActivity.this);//跟肥料相关的布局文件
                    shiFeiLinearLayout.manureNumberLinearLayout(i, numberPickerUtil);
                    upload_shifeicishu_linearlayout.addView(shiFeiLinearLayout);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//肥料次数下拉选择框
        //浇水次数
        upload_waternumber_spinner = findViewById(R.id.upload_waternumber_spinner);//浇水次数
        upload_waternumber_linearlayout = findViewById(R.id.upload_waternumber_linearlayout);//放每一次浇水组件的父布局
        upload_waternumber_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upload_waternumber_linearlayout.removeAllViews();
                uploadData.clearWaterNumber();//清理掉之前选择的数据
                uploadData.setWater_number(position);
                for (int i=0;i<position;i++){
                    shiFeiLinearLayout = new ManureNumberLinearLayout(UploadDataActivity.this);//单个控件
                    shiFeiLinearLayout.waterNumberLinearLayout(i,(i+3),numberPickerUtil);
                    upload_waternumber_linearlayout.addView(shiFeiLinearLayout);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //是否除草和打药
        upload_spray_checkbox = findViewById(R.id.upload_spray_checkbox);//是否打药
        upload_weed_checkbox = findViewById(R.id.upload_weed_checkbox);//是否除草
        upload_spray_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uploadData.setSpray(isChecked);//是否除草
            }
        });
        upload_weed_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uploadData.setWeed(isChecked);
            }
        });
        //拍照和从相册选择图片
        upload_add_landphoto = findViewById(R.id.upload_add_landphoto);//添加照片的ImageView
        upload_landPhoto_linearlayout = findViewById(R.id.upload_landphoto_linearlayout);//土地景观图父容器
        upload_add_landphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (landImageView1 == null || landImageView2 == null){
                    openCamera(UploadDataActivity.this,UPLOAD_LANDPHOTO);
                }
            }
        });
        //访谈图
        upload_add_interviewphoto = findViewById(R.id.upload_add_interviewphoto);//添加现场访谈图
        upload_interviewphoto_linearlayout = findViewById(R.id.upload_interviewphoto_linearlayout);//访谈现场图容器
        upload_add_interviewphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interviewImageView == null){
                    openCamera(UploadDataActivity.this,UPLOAD_INTERVIEWPHOTO);
                }
            }
        });
    }
    public void liangshichanliang(View view){
        numberPickerUtil.choose_alert_time(UploadDataActivity.this,300,1000,upload_chanliang_textview,6);
    }//粮食产量监听事件
    //先存本地
    public void uploadSaveButton(View view){
        if (landImageView1 != null && landImageView2 != null && interviewImageView != null){
            uploadData.setDate(new Date(System.currentTimeMillis()));
            Util.toastShort(UploadDataActivity.this,uploadData.save()?"存储成功，下次再进入的时候会自动进行询问":"存储失败");
            Intent intent = new Intent(UploadDataActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Util.toastShort(UploadDataActivity.this,"需要拍照上传数据");
        }
    }
    //提交数据按钮
    public void uploadSubmitButton(View view){
        if (landImageView1 != null && landImageView2 != null && interviewImageView != null){
            uploadData.setDate(new Date(System.currentTimeMillis()));
            uploadData.save();//将数据先存入本地
            UploadDataUtil.upoload("http://www.zhengzhoudaxue.cn:8080/SaveData/uploaddata",uploadData,UploadDataActivity.this);
            finish();
        }else{
            Util.toastShort(UploadDataActivity.this,"需要拍照上传数据");
        }
    }
    //返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }//返回键
    //打开相机
    public void openCamera(Activity activity,int code) {
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            String filename = System.currentTimeMillis()+"";
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity.startActivityForResult(intent1, code);
            } else {
                //兼容android7.0 使用共享文件的形式
                contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                activity.startActivityForResult(intent1, code);
            }
        }
    }
    //是否有sd卡
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            switch (requestCode) {
                case UPLOAD_LANDPHOTO:
                    layoutParams = new LinearLayout.LayoutParams(200,LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(10,0,10,0);
                    if (resultCode == RESULT_OK) {
                        if (landImageView1 == null){
                            landImageView1 = new ImageView(UploadDataActivity.this);
                            landImageView1.setLayoutParams(layoutParams);
                            landImageView1.setImageBitmap(bitmap);
                            upload_landPhoto_linearlayout.addView(landImageView1);
                            uploadData.setLand_image1(tempFile.getPath());
                        }else if ( landImageView1 != null &&landImageView2 == null){
                            landImageView2 = new ImageView(UploadDataActivity.this);
                            landImageView2.setLayoutParams(layoutParams);
                            landImageView2.setImageBitmap(bitmap);
                            upload_landPhoto_linearlayout.addView(landImageView2);
                            uploadData.setLand_image2(tempFile.getPath());
                        }
                    }
                    break;
                case UPLOAD_INTERVIEWPHOTO:
                    interviewImageView = new ImageView(UploadDataActivity.this);
                    interviewImageView.setLayoutParams(layoutParams);
                    interviewImageView.setImageBitmap(bitmap);
                    upload_interviewphoto_linearlayout.addView(interviewImageView);
                    uploadData.setInterview_image(tempFile.getPath());
            }
        }else {
            Util.log("UploadDataActivity","activityResult方法失败了");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openCamera(UploadDataActivity.this,requestCode);
    }
}