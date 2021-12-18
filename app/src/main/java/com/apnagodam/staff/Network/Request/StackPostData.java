package com.apnagodam.staff.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StackPostData {
    @SerializedName("commodity")
    @Expose
    private String commodity;

    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("in_out")
    @Expose
    private String in_out ;
    public StackPostData(String commodity, String terminal_id,String customer_id, String in_out) {
        this.commodity = commodity;
        this.terminal_id = terminal_id ;
        this.customer_id = customer_id;
        this.in_out = in_out ;
    }
}


