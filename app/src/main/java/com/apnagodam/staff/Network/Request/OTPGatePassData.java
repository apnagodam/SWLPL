package com.apnagodam.staff.Network.Request;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.apnagodam.staff.Network.Response.GatePassBaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPGatePassData extends GatePassBaseResponse {
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @SerializedName("case_id")
    @Expose
    private String case_id;

    public OTPGatePassData(String phone_number,String case_id) {
        this.phone_number = phone_number;
        this.case_id = case_id;
    }
}


