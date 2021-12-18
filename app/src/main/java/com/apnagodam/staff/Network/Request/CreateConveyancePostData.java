package com.apnagodam.staff.Network.Request;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateConveyancePostData extends BaseResponse {
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("start_image")
    @Expose
    private String start_image;

    @SerializedName("end_image")
    @Expose
    private String end_image;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicle_no;

    @SerializedName("from_place")
    @Expose
    private String from_place;

    @SerializedName("to_place")
    @Expose
    private String to_place;

    @SerializedName("start_reading")
    @Expose
    private String start_reading;
    @SerializedName("end_reading")
    @Expose
    private String end_reading;

    @SerializedName("kms")
    @Expose
    private String kms;

    @SerializedName("charges")
    @Expose
    private String charges;

    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("other_expense")
    @Expose
    private String other_expense;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("purpose")
    @Expose
    private String purpose;

    @SerializedName("approved_by")
    @Expose
    private String approved_by;
    @SerializedName("conv_type")
    @Expose
    private String conv_type;

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("other_charge_img")
    @Expose
    private String other_charge_img;

    public CreateConveyancePostData(String date, String start_image, String end_image, String other_charge_img, String vehicle_no,
                                    String from_place, String to_place, String start_reading, String end_reading, String kms,
                                    String charges, String location, String other_expense, String total, String purpose, String approved_by, String conv_type, String terminal_id) {
        this.date = date;
        this.start_image = start_image;
        this.end_image = end_image;
        this.other_charge_img = other_charge_img;
        this.vehicle_no = vehicle_no;
        this.from_place = from_place;
        this.to_place = to_place;
        this.start_reading = start_reading;
        this.end_reading = end_reading;
        this.kms = kms;
        this.charges = charges;
        this.location = location;
        this.other_expense = other_expense;
        this.total = total;
        this.purpose = purpose;
        this.approved_by = approved_by;
        this.conv_type = conv_type;
        this.terminal_id = terminal_id;
    }
}


