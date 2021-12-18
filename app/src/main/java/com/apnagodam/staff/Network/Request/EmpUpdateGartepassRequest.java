package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmpUpdateGartepassRequest {

    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("edit_s_k_weight")
    @Expose
    private String edit_s_k_weight;
    @SerializedName("edit_s_k_bags")
    @Expose
    private String edit_s_k_bags;
    public EmpUpdateGartepassRequest(String case_id, String edit_s_k_weight, String edit_s_k_bags) {
        this.case_id = case_id;
        this.edit_s_k_weight = edit_s_k_weight;
        this.edit_s_k_bags = edit_s_k_bags;
    }
}


