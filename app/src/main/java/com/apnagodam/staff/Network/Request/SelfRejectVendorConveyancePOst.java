package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfRejectVendorConveyancePOst {

    @SerializedName("id")
    @Expose
    private String id;

    public SelfRejectVendorConveyancePOst(String id) {
        this.id = id;
    }
}


