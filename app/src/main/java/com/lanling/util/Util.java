package com.lanling.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

public class Util {

    private static String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static URL url;
    private static HttpURLConnection conn;
    private static SharedPreferences sharedPreferences;

    public static void log(String key,String msg){
        Log.d(key,msg);
    }

    public static void toastShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }
        return Pattern.compile(regEx1).matcher(email).matches();
    }

    public static String sendMessage(String path, Map<String, String> params, String encode) throws IOException {
        url = new URL(path);
        StringBuffer stringBuffer = new StringBuffer();
        if (params != null && !params.isEmpty()){
            for (Map.Entry<String,String> entry:params.entrySet()){
                try {
                    stringBuffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(),encode))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);//去掉最后一个字符
            Util.log("LoginActivity",stringBuffer.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            //设置允许输入输出
            conn.setDoInput(true);
            conn.setDoOutput(true);
            byte[] mydata = stringBuffer.toString().getBytes();
            //设置请求报文头，设定请求数据类型
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            //设置请求数据长度
            conn.setRequestProperty("Content-Length",
                    String.valueOf(mydata.length));
            //设置POST方式请求数据
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(mydata);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200){
                log("RegisterActivity","执行到这里了1111");
                return changeInputStream(conn.getInputStream(),encode);
            }
        }
        return "";
    }

    public static String changeInputStream(InputStream inputStream,String encode){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result="";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data,0,len);
                }
                result=new String(outputStream.toByteArray(),encode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //判断用农户是否已经登录
    public static int isLogin(Context context){
        sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        if(!"0".equals(sharedPreferences.getString("openid","0"))){
            return 1;//第三方qq登录
        }else if(!"0".equals(sharedPreferences.getString("username","0"))){
            return 2;//用户账号登录
        }else{
            return 0;//没有登录
        }
    }

}