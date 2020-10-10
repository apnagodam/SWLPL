package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoardData extends BaseResponse {
    public String getClock_status() {
        return clock_status;
    }

    public void setClock_status(String clock_status) {
        this.clock_status = clock_status;
    }

    @SerializedName("clock_status")
    @Expose
    protected String clock_status;
    @SerializedName("remaning_case")
    @Expose
    private String remaning_case;

    @SerializedName("complite_case")
    @Expose
    private String complite_case;
    @SerializedName("in_case")
    @Expose
    private String in_case;
    @SerializedName("out_case")
    @Expose
    private String out_case;

    public String getRemaning_case() {
        return remaning_case;
    }

    public void setRemaning_case(String remaning_case) {
        this.remaning_case = remaning_case;
    }

    public String getComplite_case() {
        return complite_case;
    }

    public void setComplite_case(String complite_case) {
        this.complite_case = complite_case;
    }

    public String getIn_case() {
        return in_case;
    }

    public void setIn_case(String in_case) {
        this.in_case = in_case;
    }

    public String getOut_case() {
        return out_case;
    }

    public void setOut_case(String out_case) {
        this.out_case = out_case;
    }

    public String getAtten_month_data() {
        return atten_month_data;
    }

    public void setAtten_month_data(String atten_month_data) {
        this.atten_month_data = atten_month_data;
    }

    @SerializedName("atten_month_data")
    @Expose
    private String atten_month_data;

}
