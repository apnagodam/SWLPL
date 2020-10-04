package com.apnagodam.staff.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bank {
    @SerializedName("bank_name")
    @Expose
    private String bankName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
