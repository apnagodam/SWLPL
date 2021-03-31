package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCaseIDPostData {
    @SerializedName("gate_pass")
    @Expose
    private String gate_pass;

    @SerializedName("in_out")
    @Expose
    private String in_out;

    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("customer_phone")
    @Expose
    private String customer_phone;

    @SerializedName("commodity_id")
    @Expose
    private String commodity_id;

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicle_no;

    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("fpo_users")
    @Expose
    private String fpo_users;
    @SerializedName("stack_id")
    @Expose
    private String stack_id;
    @SerializedName("conv_user_id")
    @Expose
    private String conv_user_id;

    public CreateCaseIDPostData(String gate_pass, String in_out, String weight, String location,
                                String customer_phone, String commodity_id, String terminal_id, String vehicle_no, String purpose, String fpo_users, String stack_id, String conv_user_id) {
        this.gate_pass = gate_pass;
        this.in_out = in_out;
        this.weight = weight;
        this.location = location;
        this.customer_phone = customer_phone;
        this.commodity_id = commodity_id;
        this.terminal_id = terminal_id;
        this.vehicle_no = vehicle_no;
        this.purpose = purpose;
        this.fpo_users = fpo_users;
        this.stack_id = stack_id;
        this.conv_user_id = conv_user_id;
    }
}


