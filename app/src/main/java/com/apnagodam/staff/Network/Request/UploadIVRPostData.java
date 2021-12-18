package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadIVRPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("ivr_file")
    @Expose
    private String ivr_file;

    @SerializedName("s_k_weight")
    @Expose
    private String s_k_weight;
    @SerializedName("s_k_bags")
    @Expose
    private String s_k_bags;
    public UploadIVRPostData(String case_id, String notes, String ivr_file, String s_k_weight, String s_k_bags) {
        this.case_id = case_id;
        this.notes = notes;
        this.ivr_file = ivr_file;
        this.s_k_weight = s_k_weight;
        this.s_k_bags = s_k_bags;

    }
}


