package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehcilePricingCheeck extends BaseResponse {
    public String getVichel_status() {
        return vichel_status;
    }

    public void setVichel_status(String vichel_status) {
        this.vichel_status = vichel_status;
    }

    @SerializedName("vichel_status")
    @Expose
    private String vichel_status;


}
