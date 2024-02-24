package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCaseIDPostData {

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("in_out")
    @Expose
    private String in_out;
    @SerializedName("customer_uid")
    @Expose
    private String customer_uid;
    @SerializedName("commodity_id")
    @Expose
    private String commodity_id;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("stack_id")
    @Expose
    private String stack_id;
    @SerializedName("no_of_bags")
    @Expose
    private String no_of_bags;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicle_no;
    @SerializedName("spot_token")
    @Expose
    private String spot_token;


    public CreateCaseIDPostData(String terminal_id, String in_out, String customer_uid, String commodity_id, String location, String stack_id,
                                String no_of_bags, String weight, String quantity, String vehicle_no, String spot_token) {

        this.terminal_id = terminal_id;
        this.in_out = in_out;
        this.customer_uid = customer_uid;
        this.commodity_id = commodity_id;
        this.location = location;
        this.stack_id = stack_id;
        this.no_of_bags = no_of_bags;
        this.weight = weight;
        this.quantity = quantity;
        this.vehicle_no = vehicle_no;
        this.spot_token = spot_token;


    }
}


