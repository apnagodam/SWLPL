package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("message")
    @Expose
    protected String message;



    @SerializedName("phone")
    @Expose
    protected String phone;
    @SerializedName("status")
    @Expose
    protected String status;
    @SerializedName("active")
    @Expose
    protected Integer activeStatus;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
