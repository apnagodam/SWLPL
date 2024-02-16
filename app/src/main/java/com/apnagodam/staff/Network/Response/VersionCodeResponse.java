package com.apnagodam.staff.Network.Response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionCodeResponse extends BaseResponse {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @SerializedName("version")
    @Expose
    protected String version;
}

