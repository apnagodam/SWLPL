package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClosedCasesPostData {

    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("notes")
    @Expose
    private String notes;

    public ClosedCasesPostData(String case_id, String notes) {
        this.case_id = case_id;
        this.notes = notes ;
    }
}


