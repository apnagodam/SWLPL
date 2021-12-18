package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendancePostData {
    @SerializedName("clock_status")
    @Expose
    private String clock_status;
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("long")
    @Expose
    private String Long;

    @SerializedName("lat")
    @Expose
    private String lat ;
    public AttendancePostData(String lat, String Long,String clock_status, String image) {
        this.lat = lat ;
        this.Long = Long;
        this.clock_status = clock_status;
        this.image = image ;
    }
}


