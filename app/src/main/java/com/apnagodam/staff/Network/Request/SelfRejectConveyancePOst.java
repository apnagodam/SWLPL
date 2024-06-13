package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfRejectConveyancePOst {

    @SerializedName("conveyance_id")
    @Expose
    private String conveyance_id;

    public SelfRejectConveyancePOst(String conveyance_id) {
        this.conveyance_id = conveyance_id;
    }
}


