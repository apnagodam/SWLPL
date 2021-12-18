package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckInventory extends BaseResponse {
    @SerializedName("bags_total")
    @Expose
    private Integer bagsTotal;
    @SerializedName("weight_total")
    @Expose
    private Double weightTotal;

    public String getS_k_weight() {
        return s_k_weight;
    }

    public void setS_k_weight(String s_k_weight) {
        this.s_k_weight = s_k_weight;
    }

    public String getS_k_bags() {
        return s_k_bags;
    }

    public void setS_k_bags(String s_k_bags) {
        this.s_k_bags = s_k_bags;
    }

    @SerializedName("s_k_weight")
    @Expose
    private String s_k_weight;
    @SerializedName("s_k_bags")
    @Expose
    private String s_k_bags;




    public String getRequest_weight() {
        return request_weight;
    }

    public void setRequest_weight(String request_weight) {
        this.request_weight = request_weight;
    }

    @SerializedName("request_weight")
    @Expose
    private String request_weight;
    public Integer getBagsTotal() {
        return bagsTotal;
    }

    public void setBagsTotal(Integer bagsTotal) {
        this.bagsTotal = bagsTotal;
    }

    public Double getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(Double weightTotal) {
        this.weightTotal = weightTotal;
    }

}
