package com.apnagodam.staff.Network.Response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StackRequestResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("inward_request_data")
    @Expose
    private ArrayList<InwardRequestDatum> inwardRequestData;
    @SerializedName("outward_request_data")
    @Expose
    private ArrayList<OutwardRequestDatum> outwardRequestData;
    @SerializedName("inward_count")
    @Expose
    private Integer inwardCount;
    @SerializedName("outward_count")
    @Expose
    private Integer outwardCount;


    private final static long serialVersionUID = 4503894588630732652L;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<InwardRequestDatum> getInwardRequestData() {
        return inwardRequestData;
    }

    public void setInwardRequestData(ArrayList<InwardRequestDatum> inwardRequestData) {
        this.inwardRequestData = inwardRequestData;
    }

    public ArrayList<OutwardRequestDatum> getOutwardRequestData() {
        return outwardRequestData;
    }

    public void setOutwardRequestData(ArrayList<OutwardRequestDatum> outwardRequestData) {
        this.outwardRequestData = outwardRequestData;
    }

    public Integer getInwardCount() {
        return inwardCount;
    }

    public void setInwardCount(Integer inwardCount) {
        this.inwardCount = inwardCount;
    }

    public Integer getOutwardCount() {
        return outwardCount;
    }

    public void setOutwardCount(Integer outwardCount) {
        this.outwardCount = outwardCount;
    }

    public class InwardRequestDatum implements Serializable {

        @SerializedName("terminal_name")
        @Expose
        private String terminalName;
        @SerializedName("terminal_id")
        @Expose
        private Integer terminalId;
        @SerializedName("in_out_type")
        @Expose
        private String inOutType;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("commodity")
        @Expose
        private String commodity;
        @SerializedName("commodity_id")
        @Expose
        private Integer commodityId;
        @SerializedName("stack_number")
        @Expose
        private String stackNumber;
        @SerializedName("stack_id")
        @Expose
        private Integer stackId;
        @SerializedName("stack_request_rowid")
        @Expose
        private Integer stackRequestRowid;
        @SerializedName("wgt_in_kg")
        @Expose
        private Integer wgtInKg;
        @SerializedName("wgt_in_qtl")
        @Expose
        private String wgtInQtl;
        @SerializedName("driver_number")
        @Expose
        private String driverNumber;
        @SerializedName("vehicle_number")
        @Expose
        private String vehicleNumber;


        @SerializedName("user_number")
        @Expose
        private String userNumber;

        public String getUserNumber() {
            return userNumber;
        }

        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
        }

        private final static long serialVersionUID = 2286038879691105104L;

        public String getTerminalName() {
            return terminalName;
        }

        public void setTerminalName(String terminalName) {
            this.terminalName = terminalName;
        }

        public Integer getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(Integer terminalId) {
            this.terminalId = terminalId;
        }

        public String getInOutType() {
            return inOutType;
        }

        public void setInOutType(String inOutType) {
            this.inOutType = inOutType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public Integer getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(Integer commodityId) {
            this.commodityId = commodityId;
        }

        public String getStackNumber() {
            return stackNumber;
        }

        public void setStackNumber(String stackNumber) {
            this.stackNumber = stackNumber;
        }

        public Integer getStackId() {
            return stackId;
        }

        public void setStackId(Integer stackId) {
            this.stackId = stackId;
        }

        public Integer getStackRequestRowid() {
            return stackRequestRowid;
        }

        public void setStackRequestRowid(Integer stackRequestRowid) {
            this.stackRequestRowid = stackRequestRowid;
        }

        public Integer getWgtInKg() {
            return wgtInKg;
        }

        public void setWgtInKg(Integer wgtInKg) {
            this.wgtInKg = wgtInKg;
        }

        public String getWgtInQtl() {
            return wgtInQtl;
        }

        public void setWgtInQtl(String wgtInQtl) {
            this.wgtInQtl = wgtInQtl;
        }

        public String getDriverNumber() {
            return driverNumber;
        }

        public void setDriverNumber(String driverNumber) {
            this.driverNumber = driverNumber;
        }

        public String getVehicleNumber() {
            return vehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            this.vehicleNumber = vehicleNumber;
        }

    }

    public class OutwardRequestDatum implements Serializable {

        @SerializedName("terminal_name")
        @Expose
        private String terminalName;
        @SerializedName("terminal_id")
        @Expose
        private Integer terminalId;
        @SerializedName("in_out_type")
        @Expose
        private String inOutType;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("commodity")
        @Expose
        private String commodity;
        @SerializedName("commodity_id")
        @Expose
        private Integer commodityId;
        @SerializedName("stack_number")
        @Expose
        private String stackNumber;
        @SerializedName("stack_id")
        @Expose
        private String stackId;
        @SerializedName("stack_request_rowid")
        @Expose
        private String stackRequestRowid;
        @SerializedName("wgt_in_kg")
        @Expose
        private Integer wgtInKg;
        @SerializedName("wgt_in_qtl")
        @Expose
        private String wgtInQtl;
        @SerializedName("driver_number")
        @Expose
        private String driverNumber;
        @SerializedName("vehicle_number")
        @Expose
        private String vehicleNumber;

        @SerializedName("user_number")
        @Expose
        private String userNumber;


//        @SerializedName("release_bags")
//        @Expose
//        private Integer releaseBags;
//
//        @SerializedName("release_weight")
//        @Expose
//        private String releaseWeight;

//        public Integer getReleaseBags() {
//            return releaseBags;
//        }
//
//        public void setReleaseBags(Integer releaseBags) {
//            this.releaseBags = releaseBags;
//        }
//
//        public String getReleaseWeight() {
//            return releaseWeight;
//        }
//
//        public void setReleaseWeight(String releaseWeight) {
//            this.releaseWeight = releaseWeight;
//        }

        public String getUserNumber() {
            return userNumber;
        }

        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
        }

        private final static long serialVersionUID = -506955537973854362L;

        public String getTerminalName() {
            return terminalName;
        }

        public void setTerminalName(String terminalName) {
            this.terminalName = terminalName;
        }

        public Integer getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(Integer terminalId) {
            this.terminalId = terminalId;
        }

        public String getInOutType() {
            return inOutType;
        }

        public void setInOutType(String inOutType) {
            this.inOutType = inOutType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public Integer getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(Integer commodityId) {
            this.commodityId = commodityId;
        }

        public String getStackNumber() {
            return stackNumber;
        }

        public void setStackNumber(String stackNumber) {
            this.stackNumber = stackNumber;
        }

        public String getStackId() {
            return stackId;
        }

        public void setStackId(String stackId) {
            this.stackId = stackId;
        }

        public String getStackRequestRowid() {
            return stackRequestRowid;
        }

        public void setStackRequestRowid(String stackRequestRowid) {
            this.stackRequestRowid = stackRequestRowid;
        }

        public Integer getWgtInKg() {
            return wgtInKg;
        }

        public void setWgtInKg(Integer wgtInKg) {
            this.wgtInKg = wgtInKg;
        }

        public String getWgtInQtl() {
            return wgtInQtl;
        }

        public void setWgtInQtl(String wgtInQtl) {
            this.wgtInQtl = wgtInQtl;
        }

        public String getDriverNumber() {
            return driverNumber;
        }

        public void setDriverNumber(String driverNumber) {
            this.driverNumber = driverNumber;
        }

        public String getVehicleNumber() {
            return vehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            this.vehicleNumber = vehicleNumber;
        }

    }

}


