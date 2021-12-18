package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadCCTVPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("cctv_img_1")
    @Expose
    private String cctv_img_1;

    @SerializedName("cctv_img_2")
    @Expose
    private String cctv_img_2;

    public UploadCCTVPostData(String case_id, String notes, String cctv_img_1,
                              String cctv_img_2) {
        this.case_id = case_id;
        this.notes = notes;
        this.cctv_img_1 = cctv_img_1;
        this.cctv_img_2 = cctv_img_2;
    }
}


