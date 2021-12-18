package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateLeadsPostData {

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("customer_name")
    @Expose
    private String customer_name;

    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("commodity_id")
    @Expose
    private String commodity_id;

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("commodity_date")
    @Expose
    private String commodity_date;

    @SerializedName("purpose")
    @Expose
    private String purpose;

    public CreateLeadsPostData(String user_id, String customer_name, String quantity, String location,
                               String phone, String commodity_id, String terminal_id, String commodity_date, String purpose) {
        this.user_id = user_id;
        this.customer_name = customer_name;
        this.quantity = quantity;
        this.location = location;
        this.phone = phone;
        this.commodity_id = commodity_id;
        this.terminal_id = terminal_id;
        this.commodity_date = commodity_date;
        this.purpose = purpose;
    }
}


