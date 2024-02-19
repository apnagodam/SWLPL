package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadFirstkantaParchiPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;



    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("frist_kanta_parchi")
    @Expose
    private String frist_kanta_parchi;

    @SerializedName("truck_file")
    @Expose
    private String truck_file;

    @SerializedName("truck_file2")
    @Expose
    private String truck_file2;
    @SerializedName("dharam_kanta")
    @Expose
    private String dharamKantaId;

    @SerializedName("kanta_parchi_number")
    @Expose
    private String kantaParchiNum;

    public UploadFirstkantaParchiPostData(String case_id, String notes, String frist_kanta_parchi,
                                          String truck_file,String truck_file2,String dharamKantaId,String kantaParchiNum) {
        this.case_id = case_id;

        this.notes = notes;
        this.frist_kanta_parchi = frist_kanta_parchi;
        this.truck_file = truck_file;
        this.truck_file2= truck_file2;
        this.dharamKantaId = dharamKantaId;
        this.kantaParchiNum = kantaParchiNum;
    }
}


