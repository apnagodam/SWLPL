package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadTruckDetailsPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("transporter")
    @Expose
    private String transporter;

    @SerializedName("vehicle")
    @Expose
    private String vehicle;
    @SerializedName("driver_name")
    @Expose
    private String driver_name;

    @SerializedName("driver_phone")
    @Expose
    private String driver_phone;

    @SerializedName("rate_per_km")
    @Expose
    private String rate_per_km;

    @SerializedName("min_weight")
    @Expose
    private String min_weight;
    @SerializedName("max_weight")
    @Expose
    private String max_weight;

    @SerializedName("turnaround_time")
    @Expose
    private String turnaround_time;
    @SerializedName("total_weight")
    @Expose
    private String total_weight;

    @SerializedName("no_of_bags")
    @Expose
    private String no_of_bags;

    @SerializedName("total_transport_cost")
    @Expose
    private String total_transport_cost;

    @SerializedName("advance_payment")
    @Expose
    private String advance_payment;

    @SerializedName("start_date_time")
    @Expose
    private String start_date_time;
    @SerializedName("final_settlement_amount")
    @Expose
    private String final_settlement_amount;

    @SerializedName("end_date_time")
    @Expose
    private String end_date_time;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("transporter_id")
    @Expose
    private String transporter_id;
    @SerializedName("bilty_image")
    @Expose
    private String bilty_image;
    @SerializedName("truckbook_rate_type")
    @Expose
    private String truckbook_rate_type;
    @SerializedName("truckbook_related_caseId")
    @Expose
    private String truckbook_related_caseId;

    @SerializedName("location")
    @Expose
    private String location;
    public UploadTruckDetailsPostData(String case_id, String transporter, String vehicle, String driver_name,
                                      String driver_phone, String min_weight, String max_weight, String turnaround_time, String total_weight,
                                      String no_of_bags, String total_transport_cost, String advance_payment, String start_date_time,
                                      String final_settlement_amount, String end_date_time, String notes
            , String transporter_id, String bilty_image, String truckbook_rate_type, String truckbook_related_caseId,String location) {
        this.case_id = case_id;
        this.transporter = transporter;
        this.vehicle = vehicle;
        this.driver_name = driver_name;
        this.driver_phone = driver_phone;
        this.min_weight = min_weight;
        this.max_weight = max_weight;
        this.turnaround_time = turnaround_time;
        this.total_weight = total_weight;
        this.no_of_bags = no_of_bags;
        this.total_transport_cost = total_transport_cost;
        this.advance_payment = advance_payment;
        this.start_date_time = start_date_time;
        this.final_settlement_amount = final_settlement_amount;
        this.end_date_time = end_date_time;
        this.notes = notes;
        this.transporter_id = transporter_id;
        this.bilty_image = bilty_image;
        this.truckbook_rate_type = truckbook_rate_type;
        this.truckbook_related_caseId = truckbook_related_caseId;
        this.location = location;
    }
}


