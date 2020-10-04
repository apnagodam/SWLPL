package com.apnagodam.staff.Network.Response;

import com.apnagodam.staff.module.Bank;
import com.apnagodam.staff.module.UserDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OTPvarifedResponse extends BaseResponse {

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @SerializedName("Authorization")
    @Expose
    private String accessToken;

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    @SerializedName("banks")
    @Expose
    private List<Bank> banks = null;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}

