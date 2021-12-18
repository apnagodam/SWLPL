package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.LoginResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApproveGatepassPojo extends LoginResponse {
    @SerializedName("data")
    @Expose
    private Data data;

    public DhangDetails getDhangDetails() {
        return dhangDetails;
    }

    public void setDhangDetails(DhangDetails dhangDetails) {
        this.dhangDetails = dhangDetails;
    }

    @SerializedName("dhang_details")
    @Expose
    private DhangDetails dhangDetails;
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
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("gate_pass_no")
        @Expose
        private String gatePassNo;

        public String getDhram_kanta_name() {
            return dhram_kanta_name;
        }

        public void setDhram_kanta_name(String dhram_kanta_name) {
            this.dhram_kanta_name = dhram_kanta_name;
        }

        @SerializedName("dhram_kanta_name")
        @Expose
        private String dhram_kanta_name;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("average_weight")
        @Expose
        private String averageWeight;
        @SerializedName("bags")
        @Expose
        private Integer bags;
        @SerializedName("displedge_bags")
        @Expose
        private Integer displedgeBags;
        @SerializedName("stack_no")
        @Expose
        private String stackNo;
        @SerializedName("lot_no")
        @Expose
        private String lotNo;
        @SerializedName("kanta_parchi_no")
        @Expose
        private Integer kantaParchiNo;
        @SerializedName("dharam_kanta")
        @Expose
        private Integer dharamKanta;
        @SerializedName("transporter_name")
        @Expose
        private String transporterName;
        @SerializedName("transporter_phone_no")
        @Expose
        private String transporterPhoneNo;
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
        @SerializedName("driver_phone")
        @Expose
        private String driverPhone;
        @SerializedName("file")
        @Expose
        private String file;
        @SerializedName("update_weight")
        @Expose
        private String updateWeight;
        @SerializedName("gate_pass_dhang_details")
        @Expose
        private String gatePassDhangDetails;
        @SerializedName("supervisor_id")
        @Expose
        private Integer supervisorId;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getGatePassNo() {
            return gatePassNo;
        }

        public void setGatePassNo(String gatePassNo) {
            this.gatePassNo = gatePassNo;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getAverageWeight() {
            return averageWeight;
        }

        public void setAverageWeight(String averageWeight) {
            this.averageWeight = averageWeight;
        }

        public Integer getBags() {
            return bags;
        }

        public void setBags(Integer bags) {
            this.bags = bags;
        }

        public Integer getDispledgeBags() {
            return displedgeBags;
        }

        public void setDispledgeBags(Integer displedgeBags) {
            this.displedgeBags = displedgeBags;
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

        public Integer getKantaParchiNo() {
            return kantaParchiNo;
        }

        public void setKantaParchiNo(Integer kantaParchiNo) {
            this.kantaParchiNo = kantaParchiNo;
        }

        public Integer getDharamKanta() {
            return dharamKanta;
        }

        public void setDharamKanta(Integer dharamKanta) {
            this.dharamKanta = dharamKanta;
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

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getUpdateWeight() {
            return updateWeight;
        }

        public void setUpdateWeight(String updateWeight) {
            this.updateWeight = updateWeight;
        }

        public String getGatePassDhangDetails() {
            return gatePassDhangDetails;
        }

        public void setGatePassDhangDetails(String gatePassDhangDetails) {
            this.gatePassDhangDetails = gatePassDhangDetails;
        }

        public Integer getSupervisorId() {
            return supervisorId;
        }

        public void setSupervisorId(Integer supervisorId) {
            this.supervisorId = supervisorId;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
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

    }
    public class DhangDetails {

        @SerializedName("dhang_1")
        @Expose
        private String dhang1;
        @SerializedName("dhang_2")
        @Expose
        private String dhang2;
        @SerializedName("dhang_3")
        @Expose
        private String dhang3;
        @SerializedName("dhang_4")
        @Expose
        private String dhang4;
        @SerializedName("dhang_5")
        @Expose
        private String dhang5;
        @SerializedName("dhang_6")
        @Expose
        private String dhang6;
        @SerializedName("dhang_7")
        @Expose
        private String dhang7;
        @SerializedName("dhang_8")
        @Expose
        private String dhang8;
        @SerializedName("dhang_9")
        @Expose
        private String dhang9;
        @SerializedName("dhang_10")
        @Expose
        private String dhang10;
        @SerializedName("dhang_11")
        @Expose
        private String dhang11;
        @SerializedName("dhang_12")
        @Expose
        private String dhang12;
        @SerializedName("dhang_13")
        @Expose
        private String dhang13;
        @SerializedName("dhang_14")
        @Expose
        private String dhang14;
        @SerializedName("dhang_15")
        @Expose
        private String dhang15;

        public String getDhang1() {
            return dhang1;
        }

        public void setDhang1(String dhang1) {
            this.dhang1 = dhang1;
        }

        public String getDhang2() {
            return dhang2;
        }

        public void setDhang2(String dhang2) {
            this.dhang2 = dhang2;
        }

        public String getDhang3() {
            return dhang3;
        }

        public void setDhang3(String dhang3) {
            this.dhang3 = dhang3;
        }

        public String getDhang4() {
            return dhang4;
        }

        public void setDhang4(String dhang4) {
            this.dhang4 = dhang4;
        }

        public String getDhang5() {
            return dhang5;
        }

        public void setDhang5(String dhang5) {
            this.dhang5 = dhang5;
        }

        public String getDhang6() {
            return dhang6;
        }

        public void setDhang6(String dhang6) {
            this.dhang6 = dhang6;
        }

        public String getDhang7() {
            return dhang7;
        }

        public void setDhang7(String dhang7) {
            this.dhang7 = dhang7;
        }

        public String getDhang8() {
            return dhang8;
        }

        public void setDhang8(String dhang8) {
            this.dhang8 = dhang8;
        }

        public String getDhang9() {
            return dhang9;
        }

        public void setDhang9(String dhang9) {
            this.dhang9 = dhang9;
        }

        public String getDhang10() {
            return dhang10;
        }

        public void setDhang10(String dhang10) {
            this.dhang10 = dhang10;
        }

        public String getDhang11() {
            return dhang11;
        }

        public void setDhang11(String dhang11) {
            this.dhang11 = dhang11;
        }

        public String getDhang12() {
            return dhang12;
        }

        public void setDhang12(String dhang12) {
            this.dhang12 = dhang12;
        }

        public String getDhang13() {
            return dhang13;
        }

        public void setDhang13(String dhang13) {
            this.dhang13 = dhang13;
        }

        public String getDhang14() {
            return dhang14;
        }

        public void setDhang14(String dhang14) {
            this.dhang14 = dhang14;
        }

        public String getDhang15() {
            return dhang15;
        }

        public void setDhang15(String dhang15) {
            this.dhang15 = dhang15;
        }

    }
}
