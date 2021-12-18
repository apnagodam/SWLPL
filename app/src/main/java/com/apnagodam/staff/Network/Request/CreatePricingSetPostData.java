package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePricingSetPostData {
    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("processing_fees")
    @Expose
    private String processing_fees;
    @SerializedName("interest_rate")
    @Expose
    private String interest_rate;

    @SerializedName("loan_t_per_amount")
    @Expose
    private String loan_t_per_amount;

    @SerializedName("transaction_type")
    @Expose
    private String transaction_type;

    @SerializedName("rent")
    @Expose
    private String rent;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicle_no;

    @SerializedName("labour_rate")
    @Expose
    private String labour_rate;
    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("fpo_user_id")
    @Expose
    private String fpo_user_id;

    @SerializedName("gate_pass_cdf_user_name")
    @Expose
    private String gate_pass_cdf_user_name;

    @SerializedName("coldwin_name")
    @Expose
    private String coldwin_name;

    @SerializedName("purchase_name")
    @Expose
    private String purchase_name;
    @SerializedName("loan_name")
    @Expose
    private String loan_name;

    @SerializedName("sale_name")
    @Expose
    private String sale_name;


    public CreatePricingSetPostData(String case_id, String price, String processing_fees, String interest_rate,
                                    String loan_t_per_amount, String transaction_type, String rent, String vehicle_no, String labour_rate, String notes,
                                    String fpo_user_id, String gate_pass_cdf_user_name, String coldwin_name, String purchase_name,
                                    String loan_name, String sale_name) {
        this.case_id = case_id;
        this.price = price;
        this.processing_fees = processing_fees;
        this.interest_rate = interest_rate;
        this.loan_t_per_amount = loan_t_per_amount;
        this.transaction_type = transaction_type;
        this.rent = rent;
        this.vehicle_no = vehicle_no;
        this.labour_rate = labour_rate;
        this.notes = notes;
        this.fpo_user_id = fpo_user_id;
        this.gate_pass_cdf_user_name = gate_pass_cdf_user_name;
        this.coldwin_name = coldwin_name;
        this.purchase_name = purchase_name;
        this.loan_name = loan_name;
        this.sale_name = sale_name;
    }
}


