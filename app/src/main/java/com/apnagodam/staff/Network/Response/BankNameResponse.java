package com.apnagodam.staff.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankNameResponse {
    @SerializedName("bankName")
    @Expose
    protected String bankName;

    @SerializedName("id")
    @Expose
    protected Integer id;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }





}
