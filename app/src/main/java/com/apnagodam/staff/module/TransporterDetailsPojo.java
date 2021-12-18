package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransporterDetailsPojo extends BaseResponse {
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
        private String id;
        @SerializedName("transporter_unique_id")
        @Expose
        private String transporterUniqueId;
        @SerializedName("transporter_name")
        @Expose
        private String transporterName;
        @SerializedName("transporter_firm_name")
        @Expose
        private String transporterFirmName;
        @SerializedName("transporter_age")
        @Expose
        private String transporterAge;
        @SerializedName("transporter_email")
        @Expose
        private String transporterEmail;
        @SerializedName("transporter_phone_no")
        @Expose
        private String transporterPhoneNo;
        @SerializedName("transporter_location")
        @Expose
        private String transporterLocation;
        @SerializedName("transporter_bank_name")
        @Expose
        private String transporterBankName;
        @SerializedName("transporter_account_number")
        @Expose
        private String transporterAccountNumber;
        @SerializedName("transporter_ifsc_code")
        @Expose
        private String transporterIfscCode;
        @SerializedName("transporter_rate")
        @Expose
        private String transporterRate;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("transporter_pancard_number")
        @Expose
        private String transporterPancardNumber;
        @SerializedName("transporter_aadhar_number")
        @Expose
        private String transporterAadharNumber;
        @SerializedName("transporter_gst_number")
        @Expose
        private String transporterGstNumber;
        @SerializedName("transporter_profile_image")
        @Expose
        private String transporterProfileImage;
        @SerializedName("transporter_pancard_image")
        @Expose
        private String transporterPancardImage;
        @SerializedName("transporter_aadhar_image")
        @Expose
        private String transporterAadharImage;
        @SerializedName("transporter_gst_image")
        @Expose
        private String transporterGstImage;
        @SerializedName("bank_passbook_image")
        @Expose
        private String bankPassbookImage;
        @SerializedName("verify_status")
        @Expose
        private String verifyStatus;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTransporterUniqueId() {
            return transporterUniqueId;
        }

        public void setTransporterUniqueId(String transporterUniqueId) {
            this.transporterUniqueId = transporterUniqueId;
        }

        public String getTransporterName() {
            return transporterName;
        }

        public void setTransporterName(String transporterName) {
            this.transporterName = transporterName;
        }

        public String getTransporterFirmName() {
            return transporterFirmName;
        }

        public void setTransporterFirmName(String transporterFirmName) {
            this.transporterFirmName = transporterFirmName;
        }

        public String getTransporterAge() {
            return transporterAge;
        }

        public void setTransporterAge(String transporterAge) {
            this.transporterAge = transporterAge;
        }

        public String getTransporterEmail() {
            return transporterEmail;
        }

        public void setTransporterEmail(String transporterEmail) {
            this.transporterEmail = transporterEmail;
        }

        public String getTransporterPhoneNo() {
            return transporterPhoneNo;
        }

        public void setTransporterPhoneNo(String transporterPhoneNo) {
            this.transporterPhoneNo = transporterPhoneNo;
        }

        public String getTransporterLocation() {
            return transporterLocation;
        }

        public void setTransporterLocation(String transporterLocation) {
            this.transporterLocation = transporterLocation;
        }

        public String getTransporterBankName() {
            return transporterBankName;
        }

        public void setTransporterBankName(String transporterBankName) {
            this.transporterBankName = transporterBankName;
        }

        public String getTransporterAccountNumber() {
            return transporterAccountNumber;
        }

        public void setTransporterAccountNumber(String transporterAccountNumber) {
            this.transporterAccountNumber = transporterAccountNumber;
        }

        public String getTransporterIfscCode() {
            return transporterIfscCode;
        }

        public void setTransporterIfscCode(String transporterIfscCode) {
            this.transporterIfscCode = transporterIfscCode;
        }

        public String getTransporterRate() {
            return transporterRate;
        }

        public void setTransporterRate(String transporterRate) {
            this.transporterRate = transporterRate;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getTransporterPancardNumber() {
            return transporterPancardNumber;
        }

        public void setTransporterPancardNumber(String transporterPancardNumber) {
            this.transporterPancardNumber = transporterPancardNumber;
        }

        public String getTransporterAadharNumber() {
            return transporterAadharNumber;
        }

        public void setTransporterAadharNumber(String transporterAadharNumber) {
            this.transporterAadharNumber = transporterAadharNumber;
        }

        public String getTransporterGstNumber() {
            return transporterGstNumber;
        }

        public void setTransporterGstNumber(String transporterGstNumber) {
            this.transporterGstNumber = transporterGstNumber;
        }

        public String getTransporterProfileImage() {
            return transporterProfileImage;
        }

        public void setTransporterProfileImage(String transporterProfileImage) {
            this.transporterProfileImage = transporterProfileImage;
        }

        public String getTransporterPancardImage() {
            return transporterPancardImage;
        }

        public void setTransporterPancardImage(String transporterPancardImage) {
            this.transporterPancardImage = transporterPancardImage;
        }

        public String getTransporterAadharImage() {
            return transporterAadharImage;
        }

        public void setTransporterAadharImage(String transporterAadharImage) {
            this.transporterAadharImage = transporterAadharImage;
        }

        public String getTransporterGstImage() {
            return transporterGstImage;
        }

        public void setTransporterGstImage(String transporterGstImage) {
            this.transporterGstImage = transporterGstImage;
        }

        public String getBankPassbookImage() {
            return bankPassbookImage;
        }

        public void setBankPassbookImage(String bankPassbookImage) {
            this.bankPassbookImage = bankPassbookImage;
        }

        public String getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(String verifyStatus) {
            this.verifyStatus = verifyStatus;
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
    }
}
