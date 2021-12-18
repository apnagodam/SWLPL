package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLoginResponse extends BaseResponse {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("seed")
    @Expose
    private String seed;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}
