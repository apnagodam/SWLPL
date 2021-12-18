package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TerminalResponse extends BaseResponse {
    public List<Terminals> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Terminals> terminals) {
        this.terminals = terminals;
    }

    @SerializedName("terminals")
    @Expose
    private List<Terminals> terminals = null;

    public class Terminals {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("mandi_samiti_id")
        @Expose
        private String mandiSamitiId;
        @SerializedName("warehouse_code")
        @Expose
        private String warehouseCode;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("name_hi")
        @Expose
        private String nameHi;
        @SerializedName("facility_ids")
        @Expose
        private String facilityIds;
        @SerializedName("bank_ids")
        @Expose
        private String bankIds;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("gatepass_start")
        @Expose
        private String gatepassStart;
        @SerializedName("gatepass_end")
        @Expose
        private String gatepassEnd;
        @SerializedName("no_of_stacks")
        @Expose
        private String noOfStacks;
        @SerializedName("dharam_kanta")
        @Expose
        private String dharamKanta;
        @SerializedName("labour_contractor")
        @Expose
        private String labourContractor;
        @SerializedName("contractor_phone")
        @Expose
        private String contractorPhone;
        @SerializedName("labour_rate")
        @Expose
        private String labourRate;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("area")
        @Expose
        private String area;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("area_sqr_ft")
        @Expose
        private String areaSqrFt;
        @SerializedName("rent_per_month")
        @Expose
        private String rentPerMonth;
        @SerializedName("capacity_in_mt")
        @Expose
        private String capacityInMt;
        @SerializedName("mandi_samiti_name")
        @Expose
        private String mandiSamitiName;
        @SerializedName("dharam_kanta_name")
        @Expose
        private String dharamKantaName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMandiSamitiId() {
            return mandiSamitiId;
        }

        public void setMandiSamitiId(String mandiSamitiId) {
            this.mandiSamitiId = mandiSamitiId;
        }

        public String getWarehouseCode() {
            return warehouseCode;
        }

        public void setWarehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameHi() {
            return nameHi;
        }

        public void setNameHi(String nameHi) {
            this.nameHi = nameHi;
        }

        public String getFacilityIds() {
            return facilityIds;
        }

        public void setFacilityIds(String facilityIds) {
            this.facilityIds = facilityIds;
        }

        public String getBankIds() {
            return bankIds;
        }

        public void setBankIds(String bankIds) {
            this.bankIds = bankIds;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getGatepassStart() {
            return gatepassStart;
        }

        public void setGatepassStart(String gatepassStart) {
            this.gatepassStart = gatepassStart;
        }

        public String getGatepassEnd() {
            return gatepassEnd;
        }

        public void setGatepassEnd(String gatepassEnd) {
            this.gatepassEnd = gatepassEnd;
        }

        public String getNoOfStacks() {
            return noOfStacks;
        }

        public void setNoOfStacks(String noOfStacks) {
            this.noOfStacks = noOfStacks;
        }

        public String getDharamKanta() {
            return dharamKanta;
        }

        public void setDharamKanta(String dharamKanta) {
            this.dharamKanta = dharamKanta;
        }

        public String getLabourContractor() {
            return labourContractor;
        }

        public void setLabourContractor(String labourContractor) {
            this.labourContractor = labourContractor;
        }

        public String getContractorPhone() {
            return contractorPhone;
        }

        public void setContractorPhone(String contractorPhone) {
            this.contractorPhone = contractorPhone;
        }

        public String getLabourRate() {
            return labourRate;
        }

        public void setLabourRate(String labourRate) {
            this.labourRate = labourRate;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAreaSqrFt() {
            return areaSqrFt;
        }

        public void setAreaSqrFt(String areaSqrFt) {
            this.areaSqrFt = areaSqrFt;
        }

        public String getRentPerMonth() {
            return rentPerMonth;
        }

        public void setRentPerMonth(String rentPerMonth) {
            this.rentPerMonth = rentPerMonth;
        }

        public String getCapacityInMt() {
            return capacityInMt;
        }

        public void setCapacityInMt(String capacityInMt) {
            this.capacityInMt = capacityInMt;
        }

        public String getMandiSamitiName() {
            return mandiSamitiName;
        }

        public void setMandiSamitiName(String mandiSamitiName) {
            this.mandiSamitiName = mandiSamitiName;
        }

        public String getDharamKantaName() {
            return dharamKantaName;
        }

        public void setDharamKantaName(String dharamKantaName) {
            this.dharamKantaName = dharamKantaName;
        }

    }
}
