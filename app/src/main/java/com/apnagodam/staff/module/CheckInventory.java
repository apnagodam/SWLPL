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
