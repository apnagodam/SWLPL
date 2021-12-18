package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginCoWinPostData  {

    public String getMobile() {
        return mobile_no;
    }
    public void setMobile(String mobile) {
        this.mobile_no = mobile;
    }

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;
    public LoginCoWinPostData(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}


