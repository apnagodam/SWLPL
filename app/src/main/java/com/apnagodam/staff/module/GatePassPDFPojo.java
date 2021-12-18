package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GatePassPDFPojo extends BaseResponse {
    @SerializedName("data")
    @Expose
    private Data data;
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public dataDhang getData_dhang() {
        return data_dhang;
    }

    public void setData_dhang(dataDhang data_dhang) {
        this.data_dhang = data_dhang;
    }

    @SerializedName("data_dhang")
    @Expose
    private dataDhang data_dhang;
    public class Data {
        public String getDispledge_bags() {
            return displedge_bags;
        }

        public void setDispledge_bags(String displedge_bags) {
            this.displedge_bags = displedge_bags;
        }

        @SerializedName("displedge_bags")
        @Expose
        private String displedge_bags;
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
        private Integer noOfBags;
        @SerializedName("driver_otp")
        @Expose
        private Integer driverOtp;
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
        @SerializedName("commodity_name")
        @Expose
        private String commodityName;
        @SerializedName("terminal_name")
        @Expose
        private String terminalName;
        @SerializedName("cust_fname")
        @Expose
        private String custFname;
        @SerializedName("cust_lname")
        @Expose
        private String custLname;
        @SerializedName("g_p_case_id")
        @Expose
        private String gPCaseId;
        @SerializedName("gate_pass_no")
        @Expose
        private String gatePassNo;
        @SerializedName("bags")
        @Expose
        private Integer bags;
        @SerializedName("stack_no")
        @Expose
        private String stackNo;
        @SerializedName("lot_no")
        @Expose
        private String lotNo;
        @SerializedName("update_weight")
        @Expose
        private String updateWeight;
        @SerializedName("file")
        @Expose
        private String file;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("dharam_kanta_name")
        @Expose
        private String dharamKantaName;
        @SerializedName("transporter_name")
        @Expose
        private String transporterName;
        @SerializedName("transporter_phone_no")
        @Expose
        private String transporterPhoneNo;
        @SerializedName("kanta_parchi_no")
        @Expose
        private Integer kantaParchiNo;
        @SerializedName("average_weight")
        @Expose
        private String averageWeight;
        @SerializedName("truck_facility")
        @Expose
        private String truckFacility;
        @SerializedName("bags_facility")
        @Expose
        private String bagsFacility;
        @SerializedName("old_kanta_parchi")
        @Expose
        private String oldKantaParchi;
        @SerializedName("old_total_weight")
        @Expose
        private String oldTotalWeight;
        @SerializedName("old_original_weight")
        @Expose
        private String oldOriginalWeight;
        @SerializedName("old_kanta_name")
        @Expose
        private String oldKantaName;
        @SerializedName("driver_name")
        @Expose
        private String driverName;
        @SerializedName("gatepass_date")
        @Expose
        private String gatepassDate;
        @SerializedName("user_price_fname")
        @Expose
        private String userPriceFname;
        @SerializedName("user_price_lname")
        @Expose
        private String userPriceLname;
        @SerializedName("user_price_phone")
        @Expose
        private String userPricePhone;
        @SerializedName("transaction_type")
        @Expose
        private String transactionType;
        @SerializedName("s_q_case_id")
        @Expose
        private String sQCaseId;
        @SerializedName("s_k_p_case_id")
        @Expose
        private String sKPCaseId;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;

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

        public Integer getNoOfBags() {
            return noOfBags;
        }

        public void setNoOfBags(Integer noOfBags) {
            this.noOfBags = noOfBags;
        }

        public Integer getDriverOtp() {
            return driverOtp;
        }

        public void setDriverOtp(Integer driverOtp) {
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

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public String getTerminalName() {
            return terminalName;
        }

        public void setTerminalName(String terminalName) {
            this.terminalName = terminalName;
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

        public String getGPCaseId() {
            return gPCaseId;
        }

        public void setGPCaseId(String gPCaseId) {
            this.gPCaseId = gPCaseId;
        }

        public String getGatePassNo() {
            return gatePassNo;
        }

        public void setGatePassNo(String gatePassNo) {
            this.gatePassNo = gatePassNo;
        }

        public Integer getBags() {
            return bags;
        }

        public void setBags(Integer bags) {
            this.bags = bags;
        }

        public String getStackNo() {
            return stackNo;
        }

        public void setStackNo(String stackNo) {
            this.stackNo = stackNo;
        }

        public String getLotNo() {
            return lotNo;
        }

        public void setLotNo(String lotNo) {
            this.lotNo = lotNo;
        }

        public String getUpdateWeight() {
            return updateWeight;
        }

        public void setUpdateWeight(String updateWeight) {
            this.updateWeight = updateWeight;
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

        public String getDharamKantaName() {
            return dharamKantaName;
        }

        public void setDharamKantaName(String dharamKantaName) {
            this.dharamKantaName = dharamKantaName;
        }

        public String getTransporterName() {
            return transporterName;
        }

        public void setTransporterName(String transporterName) {
            this.transporterName = transporterName;
        }

        public String getTransporterPhoneNo() {
            return transporterPhoneNo;
        }

        public void setTransporterPhoneNo(String transporterPhoneNo) {
            this.transporterPhoneNo = transporterPhoneNo;
        }

        public Integer getKantaParchiNo() {
            return kantaParchiNo;
        }

        public void setKantaParchiNo(Integer kantaParchiNo) {
            this.kantaParchiNo = kantaParchiNo;
        }

        public String getAverageWeight() {
            return averageWeight;
        }

        public void setAverageWeight(String averageWeight) {
            this.averageWeight = averageWeight;
        }

        public String getTruckFacility() {
            return truckFacility;
        }

        public void setTruckFacility(String truckFacility) {
            this.truckFacility = truckFacility;
        }

        public String getBagsFacility() {
            return bagsFacility;
        }

        public void setBagsFacility(String bagsFacility) {
            this.bagsFacility = bagsFacility;
        }

        public String getOldKantaParchi() {
            return oldKantaParchi;
        }

        public void setOldKantaParchi(String oldKantaParchi) {
            this.oldKantaParchi = oldKantaParchi;
        }

        public String getOldTotalWeight() {
            return oldTotalWeight;
        }

        public void setOldTotalWeight(String oldTotalWeight) {
            this.oldTotalWeight = oldTotalWeight;
        }

        public String getOldOriginalWeight() {
            return oldOriginalWeight;
        }

        public void setOldOriginalWeight(String oldOriginalWeight) {
            this.oldOriginalWeight = oldOriginalWeight;
        }

        public String getOldKantaName() {
            return oldKantaName;
        }

        public void setOldKantaName(String oldKantaName) {
            this.oldKantaName = oldKantaName;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getGatepassDate() {
            return gatepassDate;
        }

        public void setGatepassDate(String gatepassDate) {
            this.gatepassDate = gatepassDate;
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

        public String getUserPricePhone() {
            return userPricePhone;
        }

        public void setUserPricePhone(String userPricePhone) {
            this.userPricePhone = userPricePhone;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getSQCaseId() {
            return sQCaseId;
        }

        public void setSQCaseId(String sQCaseId) {
            this.sQCaseId = sQCaseId;
        }

        public String getSKPCaseId() {
            return sKPCaseId;
        }

        public void setSKPCaseId(String sKPCaseId) {
            this.sKPCaseId = sKPCaseId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }
    public class dataDhang {
        public String getDhang_1() {
            return dhang_1;
        }

        public void setDhang_1(String dhang_1) {
            this.dhang_1 = dhang_1;
        }

        public String getDhang_2() {
            return dhang_2;
        }

        public void setDhang_2(String dhang_2) {
            this.dhang_2 = dhang_2;
        }

        public String getDhang_3() {
            return dhang_3;
        }

        public void setDhang_3(String dhang_3) {
            this.dhang_3 = dhang_3;
        }

        public String getDhang_4() {
            return dhang_4;
        }

        public void setDhang_4(String dhang_4) {
            this.dhang_4 = dhang_4;
        }

        public String getDhang_5() {
            return dhang_5;
        }

        public void setDhang_5(String dhang_5) {
            this.dhang_5 = dhang_5;
        }

        public String getDhang_6() {
            return dhang_6;
        }

        public void setDhang_6(String dhang_6) {
            this.dhang_6 = dhang_6;
        }

        public String getDhang_7() {
            return dhang_7;
        }

        public void setDhang_7(String dhang_7) {
            this.dhang_7 = dhang_7;
        }

        public String getDhang_8() {
            return dhang_8;
        }

        public void setDhang_8(String dhang_8) {
            this.dhang_8 = dhang_8;
        }

        public String getDhang_9() {
            return dhang_9;
        }

        public void setDhang_9(String dhang_9) {
            this.dhang_9 = dhang_9;
        }

        public String getDhang_10() {
            return dhang_10;
        }

        public void setDhang_10(String dhang_10) {
            this.dhang_10 = dhang_10;
        }

        public String getDhang_11() {
            return dhang_11;
        }

        public void setDhang_11(String dhang_11) {
            this.dhang_11 = dhang_11;
        }

        public String getDhang_12() {
            return dhang_12;
        }

        public void setDhang_12(String dhang_12) {
            this.dhang_12 = dhang_12;
        }

        public String getDhang_13() {
            return dhang_13;
        }

        public void setDhang_13(String dhang_13) {
            this.dhang_13 = dhang_13;
        }

        public String getDhang_14() {
            return dhang_14;
        }

        public void setDhang_14(String dhang_14) {
            this.dhang_14 = dhang_14;
        }

        public String getDhang_15() {
            return dhang_15;
        }

        public void setDhang_15(String dhang_15) {
            this.dhang_15 = dhang_15;
        }

        @SerializedName("dhang_1")
        @Expose
        private String dhang_1;
        @SerializedName("dhang_2")
        @Expose
        private String dhang_2;
        @SerializedName("dhang_3")
        @Expose
        private String dhang_3;
        @SerializedName("dhang_4")
        @Expose
        private String dhang_4;
        @SerializedName("dhang_5")
        @Expose
        private String dhang_5;
        @SerializedName("dhang_6")
        @Expose
        private String dhang_6;
        @SerializedName("dhang_7")
        @Expose
        private String dhang_7;
        @SerializedName("dhang_8")
        @Expose
        private String dhang_8;
        @SerializedName("dhang_9")
        @Expose
        private String dhang_9;
        @SerializedName("dhang_10")
        @Expose
        private String dhang_10;
        @SerializedName("dhang_11")
        @Expose
        private String dhang_11;
        @SerializedName("dhang_12")
        @Expose
        private String dhang_12;
        @SerializedName("dhang_13")
        @Expose
        private String dhang_13;
        @SerializedName("dhang_14")
        @Expose
        private String dhang_14;
        @SerializedName("dhang_15")
        @Expose
        private String dhang_15;

    }
}
