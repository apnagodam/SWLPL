package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPassID extends BaseResponse {
    public String getGate_pass() {
        return gate_pass;
    }

    public void setGate_pass(String gate_pass) {
        this.gate_pass = gate_pass;
    }

    @SerializedName("gate_pass")
    @Expose
    private String gate_pass;


}
