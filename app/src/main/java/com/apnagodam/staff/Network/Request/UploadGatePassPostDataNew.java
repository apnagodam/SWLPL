package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadGatePassPostDataNew {
    @SerializedName("case_id")
    @Expose
    private String case_id;
    @SerializedName("gatepass_img")
    @Expose
    private String gatepass_img;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("no_of_bags")
    @Expose
    private String no_of_bags;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("stack_no")
    @Expose
    private String stack_no;

    @SerializedName("transporter_name")
    @Expose
    private String transporter_name;
    @SerializedName("transporter_phone_no")
    @Expose
    private String transporter_phone_no;

    @SerializedName("average_weight")
    @Expose
    private String average_weight;
    @SerializedName("kanta_parchi_no")
    @Expose
    private String kanta_parchi_no;
    @SerializedName("truck_facility")
    @Expose
    private String truck_facility;

    @SerializedName("bags_facility")
    @Expose
    private String bags_facility;
    @SerializedName("old_kanta_parchi")
    @Expose
    private String old_kanta_parchi;

    @SerializedName("old_total_weight")
    @Expose
    private String old_total_weight;
    @SerializedName("old_original_weight")
    @Expose
    private String old_original_weight;

    @SerializedName("old_kanta_name")
    @Expose
    private String old_kanta_name;

    @SerializedName("driver_name")
    @Expose
    private String driver_name;
    @SerializedName("driver_phone")
    @Expose
    private String driver_phone;
    @SerializedName("dharam_kanta")
    @Expose
    private String dharam_kanta;
    public UploadGatePassPostDataNew(String case_id,  String weight, String no_of_bags, String notes,
                                     String stack_no, String transporter_name, String transporter_phone_no,
                                     String average_weight, String kanta_parchi_no, String truck_facility, String bags_facility, String old_kanta_parchi,
                                     String old_total_weight, String old_original_weight, String old_kanta_name, String driver_name, String driver_phone, String dharam_kanta) {
        this.case_id = case_id;
        this.weight = weight;
        this.no_of_bags = no_of_bags;
        this.notes = notes;
        this.stack_no = stack_no;
        this.transporter_name = transporter_name;
        this.transporter_phone_no = transporter_phone_no;
        this.average_weight = average_weight;
        this.kanta_parchi_no = kanta_parchi_no;
        this.truck_facility = truck_facility;
        this.bags_facility = bags_facility;
        this.old_kanta_parchi = old_kanta_parchi;
        this.old_total_weight = old_total_weight;
        this.old_original_weight = old_original_weight;
        this.old_kanta_name = old_kanta_name;
        this.driver_name = driver_name;
        this.driver_phone = driver_phone;
        this.dharam_kanta = dharam_kanta;
    }
}


