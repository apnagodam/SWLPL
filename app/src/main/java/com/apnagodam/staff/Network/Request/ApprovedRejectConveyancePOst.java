package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovedRejectConveyancePOst {

    @SerializedName("conveyance_id")
    @Expose
    private String conveyance_id;
    @SerializedName("approve_notes")
    @Expose
    private String approve_notes;
    public ApprovedRejectConveyancePOst(String conveyance_id, String approve_notes) {
        this.conveyance_id = conveyance_id;
        this.approve_notes = approve_notes;
    }
}


