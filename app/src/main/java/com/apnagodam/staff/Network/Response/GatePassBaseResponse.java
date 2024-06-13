package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GatePassBaseResponse {
    @SerializedName("message")
    @Expose
    protected String message;
    @SerializedName("status")
    @Expose
    protected Integer status;
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
