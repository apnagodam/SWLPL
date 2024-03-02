package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadLabourDetailsPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("contractor_id")
    @Expose
    private String contractor_id;

    @SerializedName("labour_contractor")
    @Expose
    private String labour_contractor;
    @SerializedName("contractor_no")
    @Expose
    private String contractor_no;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("booking_date")
    @Expose
    private String booking_date;

    @SerializedName("labour_rate_per_bags")
    @Expose
    private String labour_rate_per_bags;
    @SerializedName("total_labour")
    @Expose
    private String total_labour;

    @SerializedName("total_bags")
    @Expose
    private String total_bags;
    @SerializedName("notes")
    @Expose
    private String notes;


    public UploadLabourDetailsPostData(String case_id, String contractor_id,  String labour_contractor, String contractor_no,
                                       String location, String labour_rate_per_bags, String total_labour, String total_bags, String notes
            , String booking_date) {
        this.case_id = case_id;
        this.contractor_id= contractor_id;
        this.labour_contractor = labour_contractor;
        this.contractor_no = contractor_no;
        this.location = location;
        this.labour_rate_per_bags = labour_rate_per_bags;
        this.total_labour = total_labour;
        this.total_bags = total_bags;
        this.notes = notes;
        this.booking_date=booking_date;
    }
}


