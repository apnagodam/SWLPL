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

    @SerializedName("truck_file2")
    @Expose
    private String truck_file2;
    @SerializedName("no_of_bags")
    @Expose
    private String noOfBags;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("displedge_bags")
    @Expose
    private String displeasedBages;

    @SerializedName("kanta_parchi_number")
    @Expose
    private String kantaParchiNumber;
    @SerializedName("kanta_name")
    @Expose
    private String kantaName;

    @SerializedName("kanta_id")
    @Expose
    private Integer kantaId;


    @SerializedName("truck_facility")
    @Expose
    private Integer truckFacility;


    public Integer getTruckFacility() {
        return truckFacility;
    }

    public void setTruckFacility(Integer truckFacility) {
        this.truckFacility = truckFacility;
    }

    public Integer getBagsFacility() {
        return bagsFacility;
    }

    public void setBagsFacility(Integer bagsFacility) {
        this.bagsFacility = bagsFacility;
    }

    @SerializedName("bags_facility")
    @Expose
    private Integer bagsFacility;

    public String getKantaParchiNumber() {
        return kantaParchiNumber;
    }

    public void setKantaParchiNumber(String kantaParchiNumber) {
        this.kantaParchiNumber = kantaParchiNumber;
    }

    public String getKantaName() {
        return kantaName;
    }

    public void setKantaName(String kantaName) {
        this.kantaName = kantaName;
    }


    public Integer getKantaId() {
        return kantaId;
    }

    public void setKantaId(Integer kantaId) {
        this.kantaId = kantaId;
    }



    public UploadSecoundkantaParchiPostData(String case_id, String notes, String kanta_parchi,
                                            String truck_file,String truck_file2,String noOfBags,String weight,String displeasedBages,Integer kantaId, String kantaName,String kantaParchiNumber,
                                            Integer truckFacility,Integer bagsFacility) {
        this.case_id = case_id;
        this.notes = notes;
        this.kanta_parchi = kanta_parchi;
        this.truck_file = truck_file;
        this.truck_file2 = truck_file2;
        this.noOfBags=noOfBags;
        this.weight=weight;
        this.displeasedBages = displeasedBages;
        this.kantaId = kantaId;
        this.kantaParchiNumber = kantaParchiNumber;
        this.kantaName = kantaName;
        this.truckFacility = truckFacility;
        this.bagsFacility = bagsFacility;
    }
}


