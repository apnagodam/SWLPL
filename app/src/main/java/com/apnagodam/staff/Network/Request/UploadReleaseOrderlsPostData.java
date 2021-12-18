package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadReleaseOrderlsPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("release_order_img")
    @Expose
    private String release_order_img;

    @SerializedName("delivery_order_img")
    @Expose
    private String delivery_order_img;
    public UploadReleaseOrderlsPostData(String case_id, String notes, String release_order_img, String delivery_order_img) {
        this.case_id = case_id;
        this.notes = notes;
        this.release_order_img = release_order_img;
        this.delivery_order_img = delivery_order_img;
    }
}


