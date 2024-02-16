package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VendorNamePojo extends BaseResponse {
    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
        @SerializedName("vendor_first_name")
        @Expose
        private String vendorFirstName;
        @SerializedName("vendor_last_name")
        @Expose
        private String vendorLastName;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("vendor_address")
        @Expose
        private String vendorAddress;
        @SerializedName("vendor_bank_name")
        @Expose
        private String vendorBankName;
        @SerializedName("vendor_branch_name")
        @Expose
        private String vendorBranchName;
        @SerializedName("vendor_acc_no")
        @Expose
        private String vendorAccNo;
        @SerializedName("vendor_ifsc_code")
        @Expose
        private String vendorIfscCode;
        @SerializedName("vendor_aadhar_no")
        @Expose
        private String vendorAadharNo;
        @SerializedName("vendor_pancard_no")
        @Expose
        private String vendorPancardNo;
        @SerializedName("vendor_cap_price")
        @Expose
        private String vendorCapPrice;
        @SerializedName("approvedBy_id_1")
        @Expose
        private String approvedById1;
        @SerializedName("approvedBy_id_2")
        @Expose
        private String approvedById2;
        @SerializedName("vendor_expenses_id")
        @Expose
        private String vendorExpensesId;
        @SerializedName("vendor_terminal_id")
        @Expose
        private String vendorTerminalId;
        @SerializedName("vendor_passbook_image")
        @Expose
        private String vendorPassbookImage;
        @SerializedName("vendor_profile_img")
        @Expose
        private String vendorProfileImg;
        @SerializedName("vendor_aadhar_img")
        @Expose
        private String vendorAadharImg;
        @SerializedName("vendor_pancard_img")
        @Expose
        private String vendorPancardImg;
        @SerializedName("approved_by")
        @Expose
        private String approvedBy;
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

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getVendorFirstName() {
            return vendorFirstName;
        }

        public void setVendorFirstName(String vendorFirstName) {
            this.vendorFirstName = vendorFirstName;
        }

        public String getVendorLastName() {
            return vendorLastName;
        }

        public void setVendorLastName(String vendorLastName) {
            this.vendorLastName = vendorLastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getVendorAddress() {
            return vendorAddress;
        }

        public void setVendorAddress(String vendorAddress) {
            this.vendorAddress = vendorAddress;
        }

        public String getVendorBankName() {
            return vendorBankName;
        }

        public void setVendorBankName(String vendorBankName) {
            this.vendorBankName = vendorBankName;
        }

        public String getVendorBranchName() {
            return vendorBranchName;
        }

        public void setVendorBranchName(String vendorBranchName) {
            this.vendorBranchName = vendorBranchName;
        }

        public String getVendorAccNo() {
            return vendorAccNo;
        }

        public void setVendorAccNo(String vendorAccNo) {
            this.vendorAccNo = vendorAccNo;
        }

        public String getVendorIfscCode() {
            return vendorIfscCode;
        }

        public void setVendorIfscCode(String vendorIfscCode) {
            this.vendorIfscCode = vendorIfscCode;
        }

        public String getVendorAadharNo() {
            return vendorAadharNo;
        }

        public void setVendorAadharNo(String vendorAadharNo) {
            this.vendorAadharNo = vendorAadharNo;
        }

        public String getVendorPancardNo() {
            return vendorPancardNo;
        }

        public void setVendorPancardNo(String vendorPancardNo) {
            this.vendorPancardNo = vendorPancardNo;
        }

        public String getVendorCapPrice() {
            return vendorCapPrice;
        }

        public void setVendorCapPrice(String vendorCapPrice) {
            this.vendorCapPrice = vendorCapPrice;
        }

        public String getApprovedById1() {
            return approvedById1;
        }

        public void setApprovedById1(String approvedById1) {
            this.approvedById1 = approvedById1;
        }

        public String getApprovedById2() {
            return approvedById2;
        }

        public void setApprovedById2(String approvedById2) {
            this.approvedById2 = approvedById2;
        }

        public String getVendorExpensesId() {
            return vendorExpensesId;
        }

        public void setVendorExpensesId(String vendorExpensesId) {
            this.vendorExpensesId = vendorExpensesId;
        }

        public String getVendorTerminalId() {
            return vendorTerminalId;
        }

        public void setVendorTerminalId(String vendorTerminalId) {
            this.vendorTerminalId = vendorTerminalId;
        }

        public String getVendorPassbookImage() {
            return vendorPassbookImage;
        }

        public void setVendorPassbookImage(String vendorPassbookImage) {
            this.vendorPassbookImage = vendorPassbookImage;
        }

        public String getVendorProfileImg() {
            return vendorProfileImg;
        }

        public void setVendorProfileImg(String vendorProfileImg) {
            this.vendorProfileImg = vendorProfileImg;
        }

        public String getVendorAadharImg() {
            return vendorAadharImg;
        }

        public void setVendorAadharImg(String vendorAadharImg) {
            this.vendorAadharImg = vendorAadharImg;
        }

        public String getVendorPancardImg() {
            return vendorPancardImg;
        }

        public void setVendorPancardImg(String vendorPancardImg) {
            this.vendorPancardImg = vendorPancardImg;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
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
