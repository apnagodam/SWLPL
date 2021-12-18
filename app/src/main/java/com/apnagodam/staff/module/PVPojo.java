package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class PVPojo extends BaseResponse {
    @SerializedName("case_ids")
    @Expose
    private String case_ids;
    @SerializedName("terminal_id")
    @Expose
    private String terminal_id;
    @SerializedName("commodities_id")
    @Expose
    private String commodities_id;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("stack_no")
    @Expose
    private String stack_no;

    public String getCase_ids() {
        return case_ids;
    }

    public void setCase_ids(String case_ids) {
        this.case_ids = case_ids;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getCommodities_id() {
        return commodities_id;
    }

    public void setCommodities_id(String commodities_id) {
        this.commodities_id = commodities_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStack_no() {
        return stack_no;
    }

    public void setStack_no(String stack_no) {
        this.stack_no = stack_no;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @SerializedName("block_no")
    @Expose
    private JSONObject data;

    public JSONObject getDhang() {
        return dhang;
    }

    public void setDhang(JSONObject dhang) {
        this.dhang = dhang;
    }

    public JSONObject getDanda() {
        return danda;
    }

    public void setDanda(JSONObject danda) {
        this.danda = danda;
    }

    public JSONObject getHeight() {
        return height;
    }

    public void setHeight(JSONObject height) {
        this.height = height;
    }

    public JSONObject getPlus_minus() {
        return plus_minus;
    }

    public void setPlus_minus(JSONObject plus_minus) {
        this.plus_minus = plus_minus;
    }

    public JSONObject getRemark() {
        return remark;
    }

    public void setRemark(JSONObject remark) {
        this.remark = remark;
    }

    @SerializedName("dhang")
    @Expose
    private JSONObject dhang;
    @SerializedName("danda")
    @Expose
    private JSONObject danda;
    @SerializedName("height")
    @Expose
    private JSONObject height;

    @SerializedName("plus_minus")
    @Expose
    private JSONObject plus_minus;
    @SerializedName("remark")
    @Expose
    private JSONObject remark;

    public PVPojo(String case_ids, String terminal_id, String commodities_id, String user_id, String stack_no,  JSONObject block_no,JSONObject dhang, JSONObject danda, JSONObject height, JSONObject plus_minus, JSONObject remark) {
        this.case_ids = case_ids;
        this.terminal_id = terminal_id;
        this.commodities_id = commodities_id;
        this.user_id = user_id;
        this.stack_no = stack_no;
        this.data = block_no;
        this.dhang = dhang;
        this.danda = danda;
        this.height = height;
        this.plus_minus = plus_minus;
        this.remark = remark;
    }
}
