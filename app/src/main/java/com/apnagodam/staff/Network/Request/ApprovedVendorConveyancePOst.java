package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovedVendorConveyancePOst {

    @SerializedName("request_rowId")
    @Expose
    private String request_rowId;
    @SerializedName("final_prize")
    @Expose
    private String final_prize;
    @SerializedName("approve_notes")
    @Expose
    private String approve_notes;
    public ApprovedVendorConveyancePOst(String request_rowId, String final_prize, String approve_notes) {
        this.request_rowId = request_rowId;
        this.final_prize = final_prize;
        this.approve_notes = approve_notes;
    }
}


