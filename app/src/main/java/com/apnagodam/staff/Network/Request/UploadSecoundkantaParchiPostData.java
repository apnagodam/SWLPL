package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadSecoundkantaParchiPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("kanta_parchi")
    @Expose
    private String kanta_parchi;

    @SerializedName("truck_file")
    @Expose
    private String truck_file;

    public UploadSecoundkantaParchiPostData(String case_id, String notes, String kanta_parchi,
                                            String truck_file) {
        this.case_id = case_id;
        this.notes = notes;
        this.kanta_parchi = kanta_parchi;
        this.truck_file = truck_file;
    }
}


