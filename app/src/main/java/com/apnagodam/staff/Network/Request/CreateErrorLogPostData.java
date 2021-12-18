package com.apnagodam.staff.Network.Request;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateErrorLogPostData extends BaseResponse {
    @SerializedName("error_name")
    @Expose
    private String error_name;

    @SerializedName("log_error")
    @Expose
    private String log_error;

    @SerializedName("correction")
    @Expose
    private String correction;
    @SerializedName("approve_by")
    @Expose
    private String approve_by;

    @SerializedName("upload_image")
    @Expose
    private String upload_image;

    @SerializedName("upload_image2")
    @Expose
    private String upload_image2;



    public CreateErrorLogPostData(String error_name, String log_error, String correction, String approve_by,
                                  String upload_image, String upload_image2) {
        this.error_name = error_name;
        this.log_error = log_error;
        this.correction = correction;
        this.approve_by = approve_by;
        this.upload_image = upload_image;
        this.upload_image2 = upload_image2;
    }
}


