package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FirstkanthaParchiListResponse extends BaseResponse {
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


    public List<FirstKataParchiDatum> getFirstKataParchiData() {
        return firstKataParchiData;
    }

    public void setFirstKataParchiData(List<FirstKataParchiDatum> firstKataParchiData) {
        this.firstKataParchiData = firstKataParchiData;
    }

    @SerializedName("first_kataParchi_data")
    @Expose
    private List<FirstKataParchiDatum> firstKataParchiData = null;

    public class FirstKataParchiDatum {

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
        private Integer commodityId;
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
        @SerializedName("k_p_case_id")
        @Expose
        private String kPCaseId;
        @SerializedName("rst_no")
        @Expose
        private String rstNo;
        @SerializedName("bags")
        @Expose
        private String bags;
        @SerializedName("gross_weight")
        @Expose
        private String grossWeight;
        @SerializedName("tare_weight")
        @Expose
        private String tareWeight;
        @SerializedName("net_weight")
        @Expose
        private String netWeight;
        @SerializedName("gross_date_time")
        @Expose
        private String grossDateTime;
        @SerializedName("tare_date_time")
        @Expose
        private String tareDateTime;
        @SerializedName("charges")
        @Expose
        private String charges;
        @SerializedName("kanta_name")
        @Expose
        private String kantaName;
        @SerializedName("kanta_place")
        @Expose
        private String kantaPlace;
        @SerializedName("file")
        @Expose
        private String file;
        @SerializedName("file_2")
        @Expose
        private String file2;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("user_price_fname")
        @Expose
        private String userPriceFname;
        @SerializedName("user_price_lname")
        @Expose
        private String userPriceLname;
        @SerializedName("l_b_case_id")
        @Expose
        private String lBCaseId;

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

        public Integer getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(Integer commodityId) {
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

        public String getKPCaseId() {
            return kPCaseId;
        }

        public void setKPCaseId(String kPCaseId) {
            this.kPCaseId = kPCaseId;
        }

        public String getRstNo() {
            return rstNo;
        }

        public void setRstNo(String rstNo) {
            this.rstNo = rstNo;
        }

        public String getBags() {
            return bags;
        }

        public void setBags(String bags) {
            this.bags = bags;
        }

        public String getGrossWeight() {
            return grossWeight;
        }

        public void setGrossWeight(String grossWeight) {
            this.grossWeight = grossWeight;
        }

        public String getTareWeight() {
            return tareWeight;
        }

        public void setTareWeight(String tareWeight) {
            this.tareWeight = tareWeight;
        }

        public String getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(String netWeight) {
            this.netWeight = netWeight;
        }

        public String getGrossDateTime() {
            return grossDateTime;
        }

        public void setGrossDateTime(String grossDateTime) {
            this.grossDateTime = grossDateTime;
        }

        public String getTareDateTime() {
            return tareDateTime;
        }

        public void setTareDateTime(String tareDateTime) {
            this.tareDateTime = tareDateTime;
        }

        public String getCharges() {
            return charges;
        }

        public void setCharges(String charges) {
            this.charges = charges;
        }

        public String getKantaName() {
            return kantaName;
        }

        public void setKantaName(String kantaName) {
            this.kantaName = kantaName;
        }

        public String getKantaPlace() {
            return kantaPlace;
        }

        public void setKantaPlace(String kantaPlace) {
            this.kantaPlace = kantaPlace;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getFile2() {
            return file2;
        }

        public void setFile2(String file2) {
            this.file2 = file2;
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

        public String getLBCaseId() {
            return lBCaseId;
        }

        public void setLBCaseId(String lBCaseId) {
            this.lBCaseId = lBCaseId;
        }

    }
}
