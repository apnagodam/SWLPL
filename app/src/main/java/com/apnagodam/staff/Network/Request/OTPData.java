package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPData {

    @SerializedName("otp")
    @Expose
    private String otp;

    @SerializedName("number")
    @Expose
    private String number;


    public OTPData(String otp, String number) {
        this.otp = otp;
        this.number = number;
    }
}


