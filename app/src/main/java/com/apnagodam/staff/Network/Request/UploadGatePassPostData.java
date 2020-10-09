package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadGatePassPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("gatepass_img")
    @Expose
    private String gatepass_img;
    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("no_of_bags")
    @Expose
    private String no_of_bags;
    @SerializedName("notes")
    @Expose
    private String notes;

    public UploadGatePassPostData(String case_id, String gatepass_img, String weight,
                                  String no_of_bags,String notes) {
        this.case_id = case_id;
        this.gatepass_img = gatepass_img;
        this.weight = weight;
        this.no_of_bags = no_of_bags;
        this.notes = notes;
    }
}


