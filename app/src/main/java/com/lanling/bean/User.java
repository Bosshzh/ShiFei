package com.lanling.bean;

public class User {

    private String email;//邮箱账号
    private String password;//密码
    private String name;//姓名
    private String sex;//性别
    private String province;//省份
    private String ctiy;//城市
    private String photourl;//图片url
    private String openid;//用户ID

    public User(String name, String sex, String province, String ctiy, String photourl, String openid) {
        this.name = name;
        this.sex = sex;
        this.province = province;
        this.ctiy = ctiy;
        this.photourl = photourl;
        this.openid = openid;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCtiy() {
        return ctiy;
    }

    public void setCtiy(String ctiy) {
        this.ctiy = ctiy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
