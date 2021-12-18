package com.apnagodam.staff.Network.Request;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateVendorConveyancePostData extends BaseResponse {
    @SerializedName("vendor_phno")
    @Expose
    private String vendor_phno;

    @SerializedName("exp_amount")
    @Expose
    private String exp_amount;

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("expense_id")
    @Expose
    private String expense_id;

    @SerializedName("expense_date")
    @Expose
    private String expense_date;

    @SerializedName("purpose")
    @Expose
    private String purpose;

    @SerializedName("expense_img1")
    @Expose
    private String expense_img1;
    @SerializedName("expense_img2")
    @Expose
    private String expense_img2;

    @SerializedName("approve_by")
    @Expose
    private String approve_by;
    public CreateVendorConveyancePostData(String vendor_phno, String exp_amount, String terminal_id, String expense_id,
                                          String expense_date, String purpose, String expense_img1, String expense_img2, String approve_by) {
        this.vendor_phno = vendor_phno;
        this.exp_amount = exp_amount;
        this.terminal_id = terminal_id;
        this.expense_id = expense_id;
        this.expense_date = expense_date;
        this.purpose = purpose;
        this.expense_img1 = expense_img1;
        this.expense_img2 = expense_img2;
        this.approve_by = approve_by;
    }
}


