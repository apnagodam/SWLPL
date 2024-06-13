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

    @SerializedName("old_file")
    @Expose
    private String oldFile;


    @SerializedName("old_kanta_wgt")
    @Expose
    private String oldKantaWeight;

    @SerializedName("old_kanta_bags")
    @Expose
    private String oldKantaBags;
    @SerializedName("old_kp_number")
    @Expose
    private String oldKantaParchiNumber;

    @SerializedName("old_kanta_name")
    @Expose
    private String oldKantaName;

    @SerializedName("net_weight")
    @Expose
    private String netWeight;

    @SerializedName("tare_weight")
    @Expose
    private String tareWeight;

    @SerializedName("gross_weight")
    @Expose
    private String grossWeight;

    @SerializedName("kanta_place")
    @Expose
    private String oldKantaLocation;




    public UploadFirstkantaParchiPostData(String case_id, String notes, String frist_kanta_parchi,
                                          String truck_file, String truck_file2, String dharamKantaId, String kantaParchiNum, String oldFile, String oldKpNumber, String oldKantaWeight, String oldKantaBags,String oldKantaParchiNumber,
                                          String oldKantaName,String netweight,String tareWeight,String grossWeight,String oldKantaLocation) {
        this.case_id = case_id;
        this.notes = notes;
        this.frist_kanta_parchi = frist_kanta_parchi;
        this.truck_file = truck_file;
        this.truck_file2 = truck_file2;
        this.dharamKantaId = dharamKantaId;
        this.kantaParchiNum = kantaParchiNum;
        this.oldKantaWeight = oldKantaWeight;
        this.oldKantaBags = oldKantaBags;
        this.oldFile = oldFile;
        this.oldKantaParchiNumber = oldKantaParchiNumber;
        this.oldKantaName = oldKantaName;
        this.netWeight = netweight;
        this.tareWeight = tareWeight;
        this.grossWeight= grossWeight;
        this.oldKantaLocation = oldKantaLocation;
    }
}


