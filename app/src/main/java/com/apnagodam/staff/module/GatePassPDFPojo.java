package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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
    public class Data {

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

        public String getDisplacedBags() {
            return displacedBags;
        }

        public void setDisplacedBags(String displacedBags) {
            this.displacedBags = displacedBags;
        }

        @SerializedName("displedge_bags")
        @Expose
        private String displacedBags;



        public String getgPCaseId() {
            return gPCaseId;
        }

        public void setgPCaseId(String gPCaseId) {
            this.gPCaseId = gPCaseId;
        }

        public String getsQCaseId() {
            return sQCaseId;
        }

        public void setsQCaseId(String sQCaseId) {
            this.sQCaseId = sQCaseId;
        }

        public String getsKPCaseId() {
            return sKPCaseId;
        }

        public void setsKPCaseId(String sKPCaseId) {
            this.sKPCaseId = sKPCaseId;
        }

        public String getSupervisorEmpId() {
            return supervisorEmpId;
        }

        public void setSupervisorEmpId(String supervisorEmpId) {
            this.supervisorEmpId = supervisorEmpId;
        }

        @SerializedName("supervisor_emp_id")
        @Expose
        private String supervisorEmpId;

        public String getEmployeeEmpId() {
            return employeeEmpId;
        }

        public void setEmployeeEmpId(String employeeEmpId) {
            this.employeeEmpId = employeeEmpId;
        }

        @SerializedName("employee_emp_id")
        @Expose
        private String employeeEmpId;

        @SerializedName("emp_first_name")
        @Expose
        private String empFirstName;
        @SerializedName("emp_last_name")
        @Expose
        private String empLastName;

        @SerializedName("emp_phone")
        @Expose
        private String empPhone;

        @SerializedName("contractor_id")
        @Expose
        private String contractorId;

        @SerializedName("transporter_id")
        @Expose
        private String transporterId;

        @SerializedName("qv_data")
        @Expose
        private ArrayList<QvData> qvList;


        public ArrayList<QvData> getQvList() {
            return qvList;
        }

        public void setQvList(ArrayList<QvData> qvList) {
            this.qvList = qvList;
        }

        public String getContractorId() {
            return contractorId;
        }

        public void setContractorId(String contractorId) {
            this.contractorId = contractorId;
        }

        public String getTransporterId() {
            return transporterId;
        }

        public void setTransporterId(String transporterId) {
            this.transporterId = transporterId;
        }

        public String getEmpFirstName() {
            return empFirstName;
        }

        public void setEmpFirstName(String empFirstName) {
            this.empFirstName = empFirstName;
        }

        public String getEmpLastName() {
            return empLastName;
        }

        public void setEmpLastName(String empLastName) {
            this.empLastName = empLastName;
        }

        public String getEmpPhone() {
            return empPhone;
        }

        public void setEmpPhone(String empPhone) {
            this.empPhone = empPhone;
        }

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


      public  class QvData implements Serializable {


            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("value")
            @Expose
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
