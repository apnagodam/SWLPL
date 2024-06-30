package com.apnagodam.staff.Network.Response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceResponse extends BaseResponse {
    public int getClock_status() {
        return clock_status;
    }

    public void setClock_status(int clock_status) {
        this.clock_status = clock_status;
    }

    @SerializedName("clock_status")
    @Expose
    protected int clock_status;
}

