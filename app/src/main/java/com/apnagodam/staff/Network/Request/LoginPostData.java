package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPostData {

    public String getMobile() {
        return number;
    }

    public void setMobile(String mobile) {
        this.number = mobile;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    @SerializedName("number")
    @Expose
    private String number;

  /*  @SerializedName("Username")
    @Expose
    private String Username;*/

    @SerializedName("latitude")
    @Expose
    private String lat;
    @SerializedName("longitude")
    @Expose
    private String Long;

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    @SerializedName("app_type")
    @Expose
    private String app_type ;
    public LoginPostData(String mobile,  String app_type) {
        this.number = mobile;
        this.app_type = app_type ;
    }
}


