package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovedIntantionPOst {

    @SerializedName("row_id")
    @Expose
    private String row_id;
    @SerializedName("notes")
    @Expose
    private String notes;
    public ApprovedIntantionPOst(String row_id, String notes) {
        this.row_id = row_id;
        this.notes = notes;
    }
}


