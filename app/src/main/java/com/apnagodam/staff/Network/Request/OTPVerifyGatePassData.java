package com.apnagodam.staff.Network.Request;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPVerifyGatePassData extends BaseResponse {
    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("otp")
    @Expose
    private String otp;

    public OTPVerifyGatePassData(String case_id, String otp) {
        this.case_id = case_id;
        this.otp = otp;
    }
}


