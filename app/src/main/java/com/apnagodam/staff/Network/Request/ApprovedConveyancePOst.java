package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovedConveyancePOst {

    @SerializedName("conveyance_id")
    @Expose
    private String conveyance_id;
    @SerializedName("final_prize")
    @Expose
    private String final_prize;
    @SerializedName("approve_notes")
    @Expose
    private String approve_notes;
    public ApprovedConveyancePOst(String conveyance_id, String final_prize, String approve_notes) {
        this.conveyance_id = conveyance_id;
        this.final_prize = final_prize;
        this.approve_notes = approve_notes;
    }
}


