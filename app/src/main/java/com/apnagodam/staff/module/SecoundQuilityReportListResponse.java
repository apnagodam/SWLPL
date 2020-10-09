package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecoundQuilityReportListResponse extends BaseResponse {
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


    public List<QuilityReport> getData() {
        return data;
    }

    public void setData(List<QuilityReport> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<QuilityReport> data = null;

    public class QuilityReport {

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

        public String getS_q_r_case_id() {
            return s_q_r_case_id;
        }

        public void setS_q_r_case_id(String s_q_r_case_id) {
            this.s_q_r_case_id = s_q_r_case_id;
        }

        @SerializedName("s_q_r_case_id")
        @Expose
        private String s_q_r_case_id;
        @SerializedName("moisture_level")
        @Expose
        private String moistureLevel;
        @SerializedName("thousand_crown_w")
        @Expose
        private String thousandCrownW;
        @SerializedName("broken")
        @Expose
        private String broken;
        @SerializedName("foreign_matter")
        @Expose
        private String foreignMatter;
        @SerializedName("thin")
        @Expose
        private String thin;
        @SerializedName("damage")
        @Expose
        private String damage;
        @SerializedName("black_smith")
        @Expose
        private String blackSmith;
        @SerializedName("infested")
        @Expose
        private String infested;
        @SerializedName("live_insects")
        @Expose
        private String liveInsects;
        @SerializedName("packaging_type")
        @Expose
        private String packagingType;
        @SerializedName("imge")
        @Expose
        private String imge;

        public String getCommodity_img() {
            return commodity_img;
        }

        public void setCommodity_img(String commodity_img) {
            this.commodity_img = commodity_img;
        }

        @SerializedName("commodity_img")
        @Expose
        private String commodity_img;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("user_price_fname")
        @Expose
        private String userPriceFname;
        @SerializedName("user_price_lname")
        @Expose
        private String userPriceLname;

        public String getS_k_p_case_id() {
            return s_k_p_case_id;
        }

        public void setS_k_p_case_id(String s_k_p_case_id) {
            this.s_k_p_case_id = s_k_p_case_id;
        }

        @SerializedName("s_k_p_case_id")
        @Expose
        private String s_k_p_case_id;

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



        public String getMoistureLevel() {
            return moistureLevel;
        }

        public void setMoistureLevel(String moistureLevel) {
            this.moistureLevel = moistureLevel;
        }

        public String getThousandCrownW() {
            return thousandCrownW;
        }

        public void setThousandCrownW(String thousandCrownW) {
            this.thousandCrownW = thousandCrownW;
        }

        public String getBroken() {
            return broken;
        }

        public void setBroken(String broken) {
            this.broken = broken;
        }

        public String getForeignMatter() {
            return foreignMatter;
        }

        public void setForeignMatter(String foreignMatter) {
            this.foreignMatter = foreignMatter;
        }

        public String getThin() {
            return thin;
        }

        public void setThin(String thin) {
            this.thin = thin;
        }

        public String getDamage() {
            return damage;
        }

        public void setDamage(String damage) {
            this.damage = damage;
        }

        public String getBlackSmith() {
            return blackSmith;
        }

        public void setBlackSmith(String blackSmith) {
            this.blackSmith = blackSmith;
        }

        public String getInfested() {
            return infested;
        }

        public void setInfested(String infested) {
            this.infested = infested;
        }

        public String getLiveInsects() {
            return liveInsects;
        }

        public void setLiveInsects(String liveInsects) {
            this.liveInsects = liveInsects;
        }

        public String getPackagingType() {
            return packagingType;
        }

        public void setPackagingType(String packagingType) {
            this.packagingType = packagingType;
        }

        public String getImge() {
            return imge;
        }

        public void setImge(String imge) {
            this.imge = imge;
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
