package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllTruckBookListResponse extends BaseResponse {
    @SerializedName("total_rows")
    @Expose
    private Integer totalRows;
    @SerializedName("total_no_pages")
    @Expose
    private Integer totalNoPages;

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalNoPages() {
        return totalNoPages;
    }

    public void setTotalNoPages(Integer totalNoPages) {
        this.totalNoPages = totalNoPages;
    }

    public List<TruckBookCollection> getTruckBookCollection() {
        return truckBookCollection;
    }

    public void setTruckBookCollection(List<TruckBookCollection> truckBookCollection) {
        this.truckBookCollection = truckBookCollection;
    }

    @SerializedName("truck_book_collection")
    @Expose
    private List<TruckBookCollection> truckBookCollection = null;

    public class TruckBookCollection {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("case_id")
        @Expose
        private String caseId;
        @SerializedName("gate_pass")
        @Expose
        private String gatePass;
        @SerializedName("in_out")
        @Expose
        private String inOut;
        @SerializedName("customer_uid")
        @Expose
        private Integer customerUid;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("commodity_id")
        @Expose
        private String commodityId;
        @SerializedName("terminal_id")
        @Expose
        private Integer terminalId;
        @SerializedName("total_weight")
        @Expose
        private String totalWeight;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("lead_gen_uid")
        @Expose
        private Integer leadGenUid;
        @SerializedName("lead_conv_uid")
        @Expose
        private Integer leadConvUid;
        @SerializedName("purpose")
        @Expose
        private String purpose;
        @SerializedName("fpo_users")
        @Expose
        private String fpoUsers;
        @SerializedName("fpo_user_id")
        @Expose
        private String fpoUserId;
        @SerializedName("gate_pass_cdf_user_name")
        @Expose
        private String gatePassCdfUserName;
        @SerializedName("coldwin_name")
        @Expose
        private String coldwinName;
        @SerializedName("purchase_name")
        @Expose
        private String purchaseName;
        @SerializedName("loan_name")
        @Expose
        private String loanName;
        @SerializedName("sale_name")
        @Expose
        private String saleName;
        @SerializedName("no_of_bags")
        @Expose
        private String noOfBags;
        @SerializedName("cancel_notes")
        @Expose
        private String cancelNotes;
        @SerializedName("approved_remark")
        @Expose
        private String approvedRemark;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("cust_fname")
        @Expose
        private String custFname;
        @SerializedName("cust_lname")
        @Expose
        private String custLname;
        @SerializedName("t_b_case_id")
        @Expose
        private String tBCaseId;
        @SerializedName("transporter")
        @Expose
        private String transporter;
        @SerializedName("vehicle")
        @Expose
        private String vehicle;
        @SerializedName("driver_name")
        @Expose
        private String driverName;
        @SerializedName("driver_phone")
        @Expose
        private String driverPhone;
        @SerializedName("rate_per_km")
        @Expose
        private String ratePerKm;
        @SerializedName("min_weight")
        @Expose
        private String minWeight;
        @SerializedName("max_weight")
        @Expose
        private String maxWeight;
        @SerializedName("turnaround_time")
        @Expose
        private String turnaroundTime;
        @SerializedName("kanta_parchi_no")
        @Expose
        private String kantaParchiNo;
        @SerializedName("gate_pass_no")
        @Expose
        private String gatePassNo;
        @SerializedName("total_transport_cost")
        @Expose
        private String totalTransportCost;
        @SerializedName("advance_payment")
        @Expose
        private String advancePayment;
        @SerializedName("start_date_time")
        @Expose
        private String startDateTime;
        @SerializedName("final_settlement_amount")
        @Expose
        private String finalSettlementAmount;
        @SerializedName("end_date_time")
        @Expose
        private String endDateTime;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("user_price_fname")
        @Expose
        private String userPriceFname;
        @SerializedName("user_price_lname")
        @Expose
        private String userPriceLname;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getGatePass() {
            return gatePass;
        }

        public void setGatePass(String gatePass) {
            this.gatePass = gatePass;
        }

        public String getInOut() {
            return inOut;
        }

        public void setInOut(String inOut) {
            this.inOut = inOut;
        }

        public Integer getCustomerUid() {
            return customerUid;
        }

        public void setCustomerUid(Integer customerUid) {
            this.customerUid = customerUid;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public Integer getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(Integer terminalId) {
            this.terminalId = terminalId;
        }

        public String getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(String totalWeight) {
            this.totalWeight = totalWeight;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public Integer getLeadGenUid() {
            return leadGenUid;
        }

        public void setLeadGenUid(Integer leadGenUid) {
            this.leadGenUid = leadGenUid;
        }

        public Integer getLeadConvUid() {
            return leadConvUid;
        }

        public void setLeadConvUid(Integer leadConvUid) {
            this.leadConvUid = leadConvUid;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getFpoUsers() {
            return fpoUsers;
        }

        public void setFpoUsers(String fpoUsers) {
            this.fpoUsers = fpoUsers;
        }

        public String getFpoUserId() {
            return fpoUserId;
        }

        public void setFpoUserId(String fpoUserId) {
            this.fpoUserId = fpoUserId;
        }

        public String getGatePassCdfUserName() {
            return gatePassCdfUserName;
        }

        public void setGatePassCdfUserName(String gatePassCdfUserName) {
            this.gatePassCdfUserName = gatePassCdfUserName;
        }

        public String getColdwinName() {
            return coldwinName;
        }

        public void setColdwinName(String coldwinName) {
            this.coldwinName = coldwinName;
        }

        public String getPurchaseName() {
            return purchaseName;
        }

        public void setPurchaseName(String purchaseName) {
            this.purchaseName = purchaseName;
        }

        public String getLoanName() {
            return loanName;
        }

        public void setLoanName(String loanName) {
            this.loanName = loanName;
        }

        public String getSaleName() {
            return saleName;
        }

        public void setSaleName(String saleName) {
            this.saleName = saleName;
        }

        public String getNoOfBags() {
            return noOfBags;
        }

        public void setNoOfBags(String noOfBags) {
            this.noOfBags = noOfBags;
        }

        public String getCancelNotes() {
            return cancelNotes;
        }

        public void setCancelNotes(String cancelNotes) {
            this.cancelNotes = cancelNotes;
        }

        public String getApprovedRemark() {
            return approvedRemark;
        }

        public void setApprovedRemark(String approvedRemark) {
            this.approvedRemark = approvedRemark;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCustFname() {
            return custFname;
        }

        public void setCustFname(String custFname) {
            this.custFname = custFname;
        }

        public String getCustLname() {
            return custLname;
        }

        public void setCustLname(String custLname) {
            this.custLname = custLname;
        }

        public String getTBCaseId() {
            return tBCaseId;
        }

        public void setTBCaseId(String tBCaseId) {
            this.tBCaseId = tBCaseId;
        }

        public String getTransporter() {
            return transporter;
        }

        public void setTransporter(String transporter) {
            this.transporter = transporter;
        }

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
        }

        public String getRatePerKm() {
            return ratePerKm;
        }

        public void setRatePerKm(String ratePerKm) {
            this.ratePerKm = ratePerKm;
        }

        public String getMinWeight() {
            return minWeight;
        }

        public void setMinWeight(String minWeight) {
            this.minWeight = minWeight;
        }

        public String getMaxWeight() {
            return maxWeight;
        }

        public void setMaxWeight(String maxWeight) {
            this.maxWeight = maxWeight;
        }

        public String getTurnaroundTime() {
            return turnaroundTime;
        }

        public void setTurnaroundTime(String turnaroundTime) {
            this.turnaroundTime = turnaroundTime;
        }

        public String getKantaParchiNo() {
            return kantaParchiNo;
        }

        public void setKantaParchiNo(String kantaParchiNo) {
            this.kantaParchiNo = kantaParchiNo;
        }

        public String getGatePassNo() {
            return gatePassNo;
        }

        public void setGatePassNo(String gatePassNo) {
            this.gatePassNo = gatePassNo;
        }

        public String getTotalTransportCost() {
            return totalTransportCost;
        }

        public void setTotalTransportCost(String totalTransportCost) {
            this.totalTransportCost = totalTransportCost;
        }

        public String getAdvancePayment() {
            return advancePayment;
        }

        public void setAdvancePayment(String advancePayment) {
            this.advancePayment = advancePayment;
        }

        public String getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(String startDateTime) {
            this.startDateTime = startDateTime;
        }

        public String getFinalSettlementAmount() {
            return finalSettlementAmount;
        }

        public void setFinalSettlementAmount(String finalSettlementAmount) {
            this.finalSettlementAmount = finalSettlementAmount;
        }

        public String getEndDateTime() {
            return endDateTime;
        }

        public void setEndDateTime(String endDateTime) {
            this.endDateTime = endDateTime;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getUserPriceFname() {
            return userPriceFname;
        }

        public void setUserPriceFname(String userPriceFname) {
            this.userPriceFname = userPriceFname;
        }

        public String getUserPriceLname() {
            return userPriceLname;
        }

        public void setUserPriceLname(String userPriceLname) {
            this.userPriceLname = userPriceLname;
        }

    }
}
