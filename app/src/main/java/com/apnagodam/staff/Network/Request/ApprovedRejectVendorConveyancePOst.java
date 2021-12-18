package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovedRejectVendorConveyancePOst {

    @SerializedName("row_id")
    @Expose
    private String row_id;
    @SerializedName("reject_notes")
    @Expose
    private String reject_notes;
    public ApprovedRejectVendorConveyancePOst(String row_id,String reject_notes) {
        this.row_id = row_id;
        this.reject_notes = reject_notes;
    }
}


