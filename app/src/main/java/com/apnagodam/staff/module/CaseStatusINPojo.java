package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CaseStatusINPojo extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    public List<Datum> getData() {
        return data;
    }
    public void setData(List<Datum> data) {
        this.data = data;
    }
    public class Datum {
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
        @SerializedName("cust_fname")
        @Expose
        private String custFname;
        @SerializedName("cust_lname")
        @Expose
        private String custLname;
        @SerializedName("quality_report_case_id")
        @Expose
        private String qualityReportCaseId;
        @SerializedName("pricing_case_id")
        @Expose
        private String pricingCaseId;
        @SerializedName("release_order_case_id")
        @Expose
        private String releaseOrderCaseId;
        @SerializedName("delivery_order_case_id")
        @Expose
        private String deliveryOrderCaseId;
        @SerializedName("truck_book_case_id")
        @Expose
        private String truckBookCaseId;
        @SerializedName("transaction_type")
        @Expose
        private String transactionType;
        @SerializedName("labour_book_case_id")
        @Expose
        private String labourBookCaseId;
        @SerializedName("kanta_parchi_case_id")
        @Expose
        private String kantaParchiCaseId;
        @SerializedName("second_quality_report_case_id")
        @Expose
        private String secondQualityReportCaseId;
        @SerializedName("second_kanta_parchi_case_id")
        @Expose
        private String secondKantaParchiCaseId;
        @SerializedName("gate_pass_case_id")
        @Expose
        private String gatePassCaseId;
        @SerializedName("e_mandi_case_id")
        @Expose
        private String eMandiCaseId;
        @SerializedName("cctv_case_id")
        @Expose
        private String cctvCaseId;
        @SerializedName("commodity_withdrawal_case_id")
        @Expose
        private String commodityWithdrawalCaseId;
        @SerializedName("accounts_case_id")
        @Expose
        private String accountsCaseId;
        @SerializedName("coldwin_case_id")
        @Expose
        private String coldwinCaseId;
        @SerializedName("case_inventory_case_id")
        @Expose
        private String caseInventoryCaseId;
        @SerializedName("shipping_start_case_id")
        @Expose
        private String shippingStartCaseId;
        @SerializedName("ivr_tagging_case_id")
        @Expose
        private String ivrTaggingCaseId;
        @SerializedName("shipping_end_case_id")
        @Expose
        private String shippingEndCaseId;
        @SerializedName("quality_claim_case_id")
        @Expose
        private String qualityClaimCaseId;
        @SerializedName("truck_payment_case_id")
        @Expose
        private String truckPaymentCaseId;
        @SerializedName("labour_payment_case_id")
        @Expose
        private String labourPaymentCaseId;
        @SerializedName("payment_received_case_id")
        @Expose
        private String paymentReceivedCaseId;
        @SerializedName("quality_report_update_time")
        @Expose
        private String qualityReportUpdateTime;
        @SerializedName("pricing_update_time")
        @Expose
        private String pricingUpdateTime;
        @SerializedName("release_order_update_time")
        @Expose
        private String releaseOrderUpdateTime;
        @SerializedName("delivery_order_update_time")
        @Expose
        private String deliveryOrderUpdateTime;
        @SerializedName("truck_book_update_time")
        @Expose
        private String truckBookUpdateTime;
        @SerializedName("labour_book_update_time")
        @Expose
        private String labourBookUpdateTime;
        @SerializedName("kanta_parchi_update_time")
        @Expose
        private String kantaParchiUpdateTime;
        @SerializedName("second_quality_report_update_time")
        @Expose
        private String secondQualityReportUpdateTime;
        @SerializedName("second_kanta_parchi_update_time")
        @Expose
        private String secondKantaParchiUpdateTime;
        @SerializedName("gate_pass_update_time")
        @Expose
        private String gatePassUpdateTime;
        @SerializedName("e_mandi_update_time")
        @Expose
        private String eMandiUpdateTime;
        @SerializedName("cctv_update_time")
        @Expose
        private String cctvUpdateTime;
        @SerializedName("commodity_withdrawal_update_time")
        @Expose
        private String commodityWithdrawalUpdateTime;
        @SerializedName("case_inventory_update_time")
        @Expose
        private String caseInventoryUpdateTime;
        @SerializedName("accounts_update_time")
        @Expose
        private String accountsUpdateTime;
        @SerializedName("coldwin_update_time")
        @Expose
        private String coldwinUpdateTime;
        @SerializedName("ivr_tagging_update_time")
        @Expose
        private String ivrTaggingUpdateTime;
        @SerializedName("shipping_start_update_time")
        @Expose
        private String shippingStartUpdateTime;
        @SerializedName("shipping_end_update_time")
        @Expose
        private String shippingEndUpdateTime;
        @SerializedName("quality_claim_update_time")
        @Expose
        private String qualityClaimUpdateTime;
        @SerializedName("truck_payment_update_time")
        @Expose
        private String truckPaymentUpdateTime;
        @SerializedName("labour_payment_update_time")
        @Expose
        private String labourPaymentUpdateTime;
        @SerializedName("payment_received_update_time")
        @Expose
        private String paymentReceivedUpdateTime;
        @SerializedName("kanta_parchi_file")
        @Expose
        private String kantaParchiFile;
        @SerializedName("kanta_parchi_file_2")
        @Expose
        private String kantaParchiFile2;
        @SerializedName("quality_report_file")
        @Expose
        private String qualityReportFile;
        @SerializedName("second_kanta_parchi_file")
        @Expose
        private String secondKantaParchiFile;
        @SerializedName("second_kanta_parchi_file_2")
        @Expose
        private String secondKantaParchiFile2;
        @SerializedName("second_quality_report_file")
        @Expose
        private String secondQualityReportFile;
        @SerializedName("release_order_file")
        @Expose
        private String releaseOrderFile;
        @SerializedName("delivery_order_file")
        @Expose
        private String deliveryOrderFile;
        @SerializedName("gate_pass_file")
        @Expose
        private String gatePassFile;
        @SerializedName("e_mandi_file")
        @Expose
        private String eMandiFile;
        @SerializedName("quality_claim_file")
        @Expose
        private String qualityClaimFile;
        @SerializedName("ivr_tagging_file")
        @Expose
        private String ivrTaggingFile;
        @SerializedName("cctv_file")
        @Expose
        private String cctvFile;
        @SerializedName("cctv_file_2")
        @Expose
        private String cctvFile2;
        @SerializedName("commodity_withdrawal_file")
        @Expose
        private String commodityWithdrawalFile;
        @SerializedName("accounts_file")
        @Expose
        private String accountsFile;
        @SerializedName("truck_payment_file")
        @Expose
        private String truckPaymentFile;
        @SerializedName("labour_payment_file")
        @Expose
        private String labourPaymentFile;
        @SerializedName("payment_received_file")
        @Expose
        private String paymentReceivedFile;
        @SerializedName("lead_gen_fname")
        @Expose
        private String leadGenFname;
        @SerializedName("lead_gen_lname")
        @Expose
        private String leadGenLname;
        @SerializedName("lead_conv_fname")
        @Expose
        private String leadConvFname;
        @SerializedName("lead_conv_lname")
        @Expose
        private String leadConvLname;
        @SerializedName("cate_name")
        @Expose
        private String cateName;
        @SerializedName("commodity_type")
        @Expose
        private String commodityType;
        @SerializedName("terminal_name")
        @Expose
        private String terminalName;
        @SerializedName("warehouse_code")
        @Expose
        private String warehouseCode;

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

        public String getQualityReportCaseId() {
            return qualityReportCaseId;
        }

        public void setQualityReportCaseId(String qualityReportCaseId) {
            this.qualityReportCaseId = qualityReportCaseId;
        }

        public String getPricingCaseId() {
            return pricingCaseId;
        }

        public void setPricingCaseId(String pricingCaseId) {
            this.pricingCaseId = pricingCaseId;
        }

        public String getReleaseOrderCaseId() {
            return releaseOrderCaseId;
        }

        public void setReleaseOrderCaseId(String releaseOrderCaseId) {
            this.releaseOrderCaseId = releaseOrderCaseId;
        }

        public String getDeliveryOrderCaseId() {
            return deliveryOrderCaseId;
        }

        public void setDeliveryOrderCaseId(String deliveryOrderCaseId) {
            this.deliveryOrderCaseId = deliveryOrderCaseId;
        }

        public String getTruckBookCaseId() {
            return truckBookCaseId;
        }

        public void setTruckBookCaseId(String truckBookCaseId) {
            this.truckBookCaseId = truckBookCaseId;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getLabourBookCaseId() {
            return labourBookCaseId;
        }

        public void setLabourBookCaseId(String labourBookCaseId) {
            this.labourBookCaseId = labourBookCaseId;
        }

        public String getKantaParchiCaseId() {
            return kantaParchiCaseId;
        }

        public void setKantaParchiCaseId(String kantaParchiCaseId) {
            this.kantaParchiCaseId = kantaParchiCaseId;
        }

        public String getSecondQualityReportCaseId() {
            return secondQualityReportCaseId;
        }

        public void setSecondQualityReportCaseId(String secondQualityReportCaseId) {
            this.secondQualityReportCaseId = secondQualityReportCaseId;
        }

        public String getSecondKantaParchiCaseId() {
            return secondKantaParchiCaseId;
        }

        public void setSecondKantaParchiCaseId(String secondKantaParchiCaseId) {
            this.secondKantaParchiCaseId = secondKantaParchiCaseId;
        }

        public String getGatePassCaseId() {
            return gatePassCaseId;
        }

        public void setGatePassCaseId(String gatePassCaseId) {
            this.gatePassCaseId = gatePassCaseId;
        }

        public String getEMandiCaseId() {
            return eMandiCaseId;
        }

        public void setEMandiCaseId(String eMandiCaseId) {
            this.eMandiCaseId = eMandiCaseId;
        }

        public String getCctvCaseId() {
            return cctvCaseId;
        }

        public void setCctvCaseId(String cctvCaseId) {
            this.cctvCaseId = cctvCaseId;
        }

        public String getCommodityWithdrawalCaseId() {
            return commodityWithdrawalCaseId;
        }

        public void setCommodityWithdrawalCaseId(String commodityWithdrawalCaseId) {
            this.commodityWithdrawalCaseId = commodityWithdrawalCaseId;
        }

        public String getAccountsCaseId() {
            return accountsCaseId;
        }

        public void setAccountsCaseId(String accountsCaseId) {
            this.accountsCaseId = accountsCaseId;
        }

        public String getColdwinCaseId() {
            return coldwinCaseId;
        }

        public void setColdwinCaseId(String coldwinCaseId) {
            this.coldwinCaseId = coldwinCaseId;
        }

        public String getCaseInventoryCaseId() {
            return caseInventoryCaseId;
        }

        public void setCaseInventoryCaseId(String caseInventoryCaseId) {
            this.caseInventoryCaseId = caseInventoryCaseId;
        }

        public String getShippingStartCaseId() {
            return shippingStartCaseId;
        }

        public void setShippingStartCaseId(String shippingStartCaseId) {
            this.shippingStartCaseId = shippingStartCaseId;
        }

        public String getIvrTaggingCaseId() {
            return ivrTaggingCaseId;
        }

        public void setIvrTaggingCaseId(String ivrTaggingCaseId) {
            this.ivrTaggingCaseId = ivrTaggingCaseId;
        }

        public String getShippingEndCaseId() {
            return shippingEndCaseId;
        }

        public void setShippingEndCaseId(String shippingEndCaseId) {
            this.shippingEndCaseId = shippingEndCaseId;
        }

        public String getQualityClaimCaseId() {
            return qualityClaimCaseId;
        }

        public void setQualityClaimCaseId(String qualityClaimCaseId) {
            this.qualityClaimCaseId = qualityClaimCaseId;
        }

        public String getTruckPaymentCaseId() {
            return truckPaymentCaseId;
        }

        public void setTruckPaymentCaseId(String truckPaymentCaseId) {
            this.truckPaymentCaseId = truckPaymentCaseId;
        }

        public String getLabourPaymentCaseId() {
            return labourPaymentCaseId;
        }

        public void setLabourPaymentCaseId(String labourPaymentCaseId) {
            this.labourPaymentCaseId = labourPaymentCaseId;
        }

        public String getPaymentReceivedCaseId() {
            return paymentReceivedCaseId;
        }

        public void setPaymentReceivedCaseId(String paymentReceivedCaseId) {
            this.paymentReceivedCaseId = paymentReceivedCaseId;
        }

        public String getQualityReportUpdateTime() {
            return qualityReportUpdateTime;
        }

        public void setQualityReportUpdateTime(String qualityReportUpdateTime) {
            this.qualityReportUpdateTime = qualityReportUpdateTime;
        }

        public String getPricingUpdateTime() {
            return pricingUpdateTime;
        }

        public void setPricingUpdateTime(String pricingUpdateTime) {
            this.pricingUpdateTime = pricingUpdateTime;
        }

        public String getReleaseOrderUpdateTime() {
            return releaseOrderUpdateTime;
        }

        public void setReleaseOrderUpdateTime(String releaseOrderUpdateTime) {
            this.releaseOrderUpdateTime = releaseOrderUpdateTime;
        }

        public String getDeliveryOrderUpdateTime() {
            return deliveryOrderUpdateTime;
        }

        public void setDeliveryOrderUpdateTime(String deliveryOrderUpdateTime) {
            this.deliveryOrderUpdateTime = deliveryOrderUpdateTime;
        }

        public String getTruckBookUpdateTime() {
            return truckBookUpdateTime;
        }

        public void setTruckBookUpdateTime(String truckBookUpdateTime) {
            this.truckBookUpdateTime = truckBookUpdateTime;
        }

        public String getLabourBookUpdateTime() {
            return labourBookUpdateTime;
        }

        public void setLabourBookUpdateTime(String labourBookUpdateTime) {
            this.labourBookUpdateTime = labourBookUpdateTime;
        }

        public String getKantaParchiUpdateTime() {
            return kantaParchiUpdateTime;
        }

        public void setKantaParchiUpdateTime(String kantaParchiUpdateTime) {
            this.kantaParchiUpdateTime = kantaParchiUpdateTime;
        }

        public String getSecondQualityReportUpdateTime() {
            return secondQualityReportUpdateTime;
        }

        public void setSecondQualityReportUpdateTime(String secondQualityReportUpdateTime) {
            this.secondQualityReportUpdateTime = secondQualityReportUpdateTime;
        }

        public String getSecondKantaParchiUpdateTime() {
            return secondKantaParchiUpdateTime;
        }

        public void setSecondKantaParchiUpdateTime(String secondKantaParchiUpdateTime) {
            this.secondKantaParchiUpdateTime = secondKantaParchiUpdateTime;
        }

        public String getGatePassUpdateTime() {
            return gatePassUpdateTime;
        }

        public void setGatePassUpdateTime(String gatePassUpdateTime) {
            this.gatePassUpdateTime = gatePassUpdateTime;
        }

        public String getEMandiUpdateTime() {
            return eMandiUpdateTime;
        }

        public void setEMandiUpdateTime(String eMandiUpdateTime) {
            this.eMandiUpdateTime = eMandiUpdateTime;
        }

        public String getCctvUpdateTime() {
            return cctvUpdateTime;
        }

        public void setCctvUpdateTime(String cctvUpdateTime) {
            this.cctvUpdateTime = cctvUpdateTime;
        }

        public String getCommodityWithdrawalUpdateTime() {
            return commodityWithdrawalUpdateTime;
        }

        public void setCommodityWithdrawalUpdateTime(String commodityWithdrawalUpdateTime) {
            this.commodityWithdrawalUpdateTime = commodityWithdrawalUpdateTime;
        }

        public String getCaseInventoryUpdateTime() {
            return caseInventoryUpdateTime;
        }

        public void setCaseInventoryUpdateTime(String caseInventoryUpdateTime) {
            this.caseInventoryUpdateTime = caseInventoryUpdateTime;
        }

        public String getAccountsUpdateTime() {
            return accountsUpdateTime;
        }

        public void setAccountsUpdateTime(String accountsUpdateTime) {
            this.accountsUpdateTime = accountsUpdateTime;
        }

        public String getColdwinUpdateTime() {
            return coldwinUpdateTime;
        }

        public void setColdwinUpdateTime(String coldwinUpdateTime) {
            this.coldwinUpdateTime = coldwinUpdateTime;
        }

        public String getIvrTaggingUpdateTime() {
            return ivrTaggingUpdateTime;
        }

        public void setIvrTaggingUpdateTime(String ivrTaggingUpdateTime) {
            this.ivrTaggingUpdateTime = ivrTaggingUpdateTime;
        }

        public String getShippingStartUpdateTime() {
            return shippingStartUpdateTime;
        }

        public void setShippingStartUpdateTime(String shippingStartUpdateTime) {
            this.shippingStartUpdateTime = shippingStartUpdateTime;
        }

        public String getShippingEndUpdateTime() {
            return shippingEndUpdateTime;
        }

        public void setShippingEndUpdateTime(String shippingEndUpdateTime) {
            this.shippingEndUpdateTime = shippingEndUpdateTime;
        }

        public String getQualityClaimUpdateTime() {
            return qualityClaimUpdateTime;
        }

        public void setQualityClaimUpdateTime(String qualityClaimUpdateTime) {
            this.qualityClaimUpdateTime = qualityClaimUpdateTime;
        }

        public String getTruckPaymentUpdateTime() {
            return truckPaymentUpdateTime;
        }

        public void setTruckPaymentUpdateTime(String truckPaymentUpdateTime) {
            this.truckPaymentUpdateTime = truckPaymentUpdateTime;
        }

        public String getLabourPaymentUpdateTime() {
            return labourPaymentUpdateTime;
        }

        public void setLabourPaymentUpdateTime(String labourPaymentUpdateTime) {
            this.labourPaymentUpdateTime = labourPaymentUpdateTime;
        }

        public String getPaymentReceivedUpdateTime() {
            return paymentReceivedUpdateTime;
        }

        public void setPaymentReceivedUpdateTime(String paymentReceivedUpdateTime) {
            this.paymentReceivedUpdateTime = paymentReceivedUpdateTime;
        }

        public String getKantaParchiFile() {
            return kantaParchiFile;
        }

        public void setKantaParchiFile(String kantaParchiFile) {
            this.kantaParchiFile = kantaParchiFile;
        }

        public String getKantaParchiFile2() {
            return kantaParchiFile2;
        }

        public void setKantaParchiFile2(String kantaParchiFile2) {
            this.kantaParchiFile2 = kantaParchiFile2;
        }

        public String getQualityReportFile() {
            return qualityReportFile;
        }

        public void setQualityReportFile(String qualityReportFile) {
            this.qualityReportFile = qualityReportFile;
        }

        public String getSecondKantaParchiFile() {
            return secondKantaParchiFile;
        }

        public void setSecondKantaParchiFile(String secondKantaParchiFile) {
            this.secondKantaParchiFile = secondKantaParchiFile;
        }

        public String getSecondKantaParchiFile2() {
            return secondKantaParchiFile2;
        }

        public void setSecondKantaParchiFile2(String secondKantaParchiFile2) {
            this.secondKantaParchiFile2 = secondKantaParchiFile2;
        }

        public String getSecondQualityReportFile() {
            return secondQualityReportFile;
        }

        public void setSecondQualityReportFile(String secondQualityReportFile) {
            this.secondQualityReportFile = secondQualityReportFile;
        }

        public String getReleaseOrderFile() {
            return releaseOrderFile;
        }

        public void setReleaseOrderFile(String releaseOrderFile) {
            this.releaseOrderFile = releaseOrderFile;
        }

        public String getDeliveryOrderFile() {
            return deliveryOrderFile;
        }

        public void setDeliveryOrderFile(String deliveryOrderFile) {
            this.deliveryOrderFile = deliveryOrderFile;
        }

        public String getGatePassFile() {
            return gatePassFile;
        }

        public void setGatePassFile(String gatePassFile) {
            this.gatePassFile = gatePassFile;
        }

        public String getEMandiFile() {
            return eMandiFile;
        }

        public void setEMandiFile(String eMandiFile) {
            this.eMandiFile = eMandiFile;
        }

        public String getQualityClaimFile() {
            return qualityClaimFile;
        }

        public void setQualityClaimFile(String qualityClaimFile) {
            this.qualityClaimFile = qualityClaimFile;
        }

        public String getIvrTaggingFile() {
            return ivrTaggingFile;
        }

        public void setIvrTaggingFile(String ivrTaggingFile) {
            this.ivrTaggingFile = ivrTaggingFile;
        }

        public String getCctvFile() {
            return cctvFile;
        }

        public void setCctvFile(String cctvFile) {
            this.cctvFile = cctvFile;
        }

        public String getCctvFile2() {
            return cctvFile2;
        }

        public void setCctvFile2(String cctvFile2) {
            this.cctvFile2 = cctvFile2;
        }

        public String getCommodityWithdrawalFile() {
            return commodityWithdrawalFile;
        }

        public void setCommodityWithdrawalFile(String commodityWithdrawalFile) {
            this.commodityWithdrawalFile = commodityWithdrawalFile;
        }

        public String getAccountsFile() {
            return accountsFile;
        }

        public void setAccountsFile(String accountsFile) {
            this.accountsFile = accountsFile;
        }

        public String getTruckPaymentFile() {
            return truckPaymentFile;
        }

        public void setTruckPaymentFile(String truckPaymentFile) {
            this.truckPaymentFile = truckPaymentFile;
        }

        public String getLabourPaymentFile() {
            return labourPaymentFile;
        }

        public void setLabourPaymentFile(String labourPaymentFile) {
            this.labourPaymentFile = labourPaymentFile;
        }

        public String getPaymentReceivedFile() {
            return paymentReceivedFile;
        }

        public void setPaymentReceivedFile(String paymentReceivedFile) {
            this.paymentReceivedFile = paymentReceivedFile;
        }

        public String getLeadGenFname() {
            return leadGenFname;
        }

        public void setLeadGenFname(String leadGenFname) {
            this.leadGenFname = leadGenFname;
        }

        public String getLeadGenLname() {
            return leadGenLname;
        }

        public void setLeadGenLname(String leadGenLname) {
            this.leadGenLname = leadGenLname;
        }

        public String getLeadConvFname() {
            return leadConvFname;
        }

        public void setLeadConvFname(String leadConvFname) {
            this.leadConvFname = leadConvFname;
        }

        public String getLeadConvLname() {
            return leadConvLname;
        }

        public void setLeadConvLname(String leadConvLname) {
            this.leadConvLname = leadConvLname;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

        public String getTerminalName() {
            return terminalName;
        }

        public void setTerminalName(String terminalName) {
            this.terminalName = terminalName;
        }

        public String getWarehouseCode() {
            return warehouseCode;
        }

        public void setWarehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
        }

    }
}
