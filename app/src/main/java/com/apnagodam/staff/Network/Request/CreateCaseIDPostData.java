package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCaseIDPostData {
//    @SerializedName("gate_pass")
//    @Expose
//    private String gate_pass;
//
//    @SerializedName("in_out")
//    @Expose
//    private String in_out;
//
//    @SerializedName("weight")
//    @Expose
//    private String weight;
//    @SerializedName("location")
//    @Expose
//    private String location;
//
//    @SerializedName("customer_phone")
//    @Expose
//    private String customer_phone;
//
//    @SerializedName("commodity_id")
//    @Expose
//    private String commodity_id;
//
//    @SerializedName("terminal_id")
//    @Expose
//    private String terminal_id;
//    @SerializedName("vehicle_no")
//    @Expose
//    private String vehicle_no;
//
//    @SerializedName("purpose")
//    @Expose
//    private String purpose;
//    @SerializedName("fpo_users")
//    @Expose
//    private String fpo_users;
//    @SerializedName("stack_id")
//    @Expose
//    private String stack_id;
//    @SerializedName("conv_user_id")
//    @Expose
//    private String conv_user_id;

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
    @SerializedName("lead_generator")
    @Expose
    private String lead_generator;
    @SerializedName("conv_user_id")
    @Expose
    private String conv_user_id;
    @SerializedName("fpo_users")
    @Expose
    private String fpo_users;


    public CreateCaseIDPostData(String terminal_id, String in_out, String customer_uid, String commodity_id, String location, String stack_id,
                                String no_of_bags, String weight, String quantity, String vehicle_no, String spot_token, String lead_generator,
                                String conv_user_id, String fpo_users) {

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
        this.lead_generator = lead_generator;
        this.conv_user_id = conv_user_id;
        this.fpo_users = fpo_users;

    }
}


