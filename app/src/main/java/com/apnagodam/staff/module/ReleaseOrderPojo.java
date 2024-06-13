package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReleaseOrderPojo extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("current_page")
        @Expose
        private Integer currentPage;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;
        @SerializedName("first_page_url")
        @Expose
        private String firstPageUrl;
        @SerializedName("from")
        @Expose
        private Integer from;
        @SerializedName("last_page")
        @Expose
        private Integer lastPage;
        @SerializedName("last_page_url")
        @Expose
        private String lastPageUrl;
        @SerializedName("next_page_url")
        @Expose
        private String nextPageUrl;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("per_page")
        @Expose
        private String perPage;
        @SerializedName("prev_page_url")
        @Expose
        private String prevPageUrl;
        @SerializedName("to")
        @Expose
        private Integer to;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public String getFirstPageUrl() {
            return firstPageUrl;
        }

        public void setFirstPageUrl(String firstPageUrl) {
            this.firstPageUrl = firstPageUrl;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getLastPage() {
            return lastPage;
        }

        public void setLastPage(Integer lastPage) {
            this.lastPage = lastPage;
        }

        public String getLastPageUrl() {
            return lastPageUrl;
        }

        public void setLastPageUrl(String lastPageUrl) {
            this.lastPageUrl = lastPageUrl;
        }

        public String getNextPageUrl() {
            return nextPageUrl;
        }

        public void setNextPageUrl(String nextPageUrl) {
            this.nextPageUrl = nextPageUrl;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPerPage() {
            return perPage;
        }

        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        public String getPrevPageUrl() {
            return prevPageUrl;
        }

        public void setPrevPageUrl(String prevPageUrl) {
            this.prevPageUrl = prevPageUrl;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }


    public class Datum {
        public String getPricing_caseId() {
            return pricing_caseId;
        }

        public void setPricing_caseId(String pricing_caseId) {
            this.pricing_caseId = pricing_caseId;
        }

        @SerializedName("pricing_caseId")
        @Expose
        private String pricing_caseId;

        public String getRelese_case_id() {
            return relese_case_id;
        }

        public void setRelese_case_id(String relese_case_id) {
            this.relese_case_id = relese_case_id;
        }

        @SerializedName("relese_case_id")
        @Expose
        private String relese_case_id;

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
        private String customerUid;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("commodity_id")
        @Expose
        private String commodityId;
        @SerializedName("terminal_id")
        @Expose
        private String terminalId;
        @SerializedName("stack_number")
        @Expose
        private String stackNumber;
        @SerializedName("total_weight")
        @Expose
        private String totalWeight;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("lead_gen_uid")
        @Expose
        private String leadGenUid;
        @SerializedName("lead_conv_uid")
        @Expose
        private String leadConvUid;
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
        @SerializedName("driver_otp")
        @Expose
        private String driverOtp;
        @SerializedName("driver_phone")
        @Expose
        private String driverPhone;
        @SerializedName("cancel_notes")
        @Expose
        private String cancelNotes;
        @SerializedName("approved_remark")
        @Expose
        private String approvedRemark;
        @SerializedName("status")
        @Expose
        private String status;
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
        @SerializedName("r_o_case_id")
        @Expose
        private String rOCaseId;

        public String getdOCaseId() {
            return dOCaseId;
        }

        public void setdOCaseId(String dOCaseId) {
            this.dOCaseId = dOCaseId;
        }

        @SerializedName("d_o_case_id")
        @Expose
        private String dOCaseId;
        @SerializedName("file")
        @Expose
        private String file;
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

        public String getCustomerUid() {
            return customerUid;
        }

        public void setCustomerUid(String customerUid) {
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

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getStackNumber() {
            return stackNumber;
        }

        public void setStackNumber(String stackNumber) {
            this.stackNumber = stackNumber;
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

        public String getLeadGenUid() {
            return leadGenUid;
        }

        public void setLeadGenUid(String leadGenUid) {
            this.leadGenUid = leadGenUid;
        }

        public String getLeadConvUid() {
            return leadConvUid;
        }

        public void setLeadConvUid(String leadConvUid) {
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

        public String getDriverOtp() {
            return driverOtp;
        }

        public void setDriverOtp(String driverOtp) {
            this.driverOtp = driverOtp;
        }

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

        public String getROCaseId() {
            return rOCaseId;
        }

        public void setROCaseId(String rOCaseId) {
            this.rOCaseId = rOCaseId;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
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
