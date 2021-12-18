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


    @SerializedName("dhang_1")
    @Expose
    private String dhang_1;
    @SerializedName("dhang_2")
    @Expose
    private String dhang_2;
    @SerializedName("dhang_3")
    @Expose
    private String dhang_3;
    @SerializedName("dhang_4")
    @Expose
    private String dhang_4;
    @SerializedName("dhang_5")
    @Expose
    private String dhang_5;
    @SerializedName("dhang_6")
    @Expose
    private String dhang_6;
    @SerializedName("dhang_7")
    @Expose
    private String dhang_7;
    @SerializedName("dhang_8")
    @Expose
    private String dhang_8;
    @SerializedName("dhang_9")
    @Expose
    private String dhang_9;
    @SerializedName("dhang_10")
    @Expose
    private String dhang_10;
    @SerializedName("dhang_11")
    @Expose
    private String dhang_11;
    @SerializedName("dhang_12")
    @Expose
    private String dhang_12;
    @SerializedName("dhang_13")
    @Expose
    private String dhang_13;
    @SerializedName("dhang_14")
    @Expose
    private String dhang_14;
    @SerializedName("dhang_15")
    @Expose
    private String dhang_15;

    @SerializedName("displedge_bags")
    @Expose
    private String displedge_bags;

    @SerializedName("old_kanta_parchi_file")
    @Expose
    private String old_kanta_parchi_file;
    @SerializedName("old_gp_kanta_parchi_file")
    @Expose
    private String old_gp_kanta_parchi_file;
    public UploadGatePassPostDataNew(String case_id, String weight, String no_of_bags, String notes,
                                     String stack_no, String transporter_name, String transporter_phone_no,
                                     String average_weight, String kanta_parchi_no, String truck_facility, String bags_facility, String old_kanta_parchi,
                                     String old_total_weight, String old_original_weight, String old_kanta_name, String driver_name, String driver_phone, String dharam_kanta
            , String dhang_1, String dhang_2, String dhang_3, String dhang_4, String dhang_5, String dhang_6, String dhang_7, String dhang_8, String dhang_9, String dhang_10, String dhang_11, String dhang_12, String dhang_13, String dhang_14, String dhang_15
            , String displedge_bags,String old_kanta_parchi_file,String old_gp_kanta_parchi_file) {
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
        this.dhang_1 = dhang_1;
        this.dhang_2 = dhang_2;
        this.dhang_3 = dhang_3;
        this.dhang_4 = dhang_4;
        this.dhang_5 = dhang_5;
        this.dhang_6 = dhang_6;
        this.dhang_7 = dhang_7;
        this.dhang_8 = dhang_8;
        this.dhang_9 = dhang_9;
        this.dhang_10 = dhang_10;
        this.dhang_11 = dhang_11;
        this.dhang_12 = dhang_12;
        this.dhang_13 = dhang_13;
        this.dhang_14 = dhang_14;
        this.dhang_15 = dhang_15;
        this.displedge_bags = displedge_bags;
        this.old_kanta_parchi_file = old_kanta_parchi_file;
        this.old_gp_kanta_parchi_file = old_gp_kanta_parchi_file;
    }
}


