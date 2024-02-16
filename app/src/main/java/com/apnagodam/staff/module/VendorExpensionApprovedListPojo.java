package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VendorExpensionApprovedListPojo extends BaseResponse {
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
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("designation_id")
        @Expose
        private String designationId;
        @SerializedName("emp_id")
        @Expose
        private String empId;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("doj")
        @Expose
        private String doj;
        @SerializedName("doe")
        @Expose
        private String doe;
        @SerializedName("personal_phone")
        @Expose
        private String personalPhone;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("branch_name")
        @Expose
        private String branchName;
        @SerializedName("account_no")
        @Expose
        private String accountNo;
        @SerializedName("ifs_code")
        @Expose
        private String ifsCode;
        @SerializedName("pan_card")
        @Expose
        private String panCard;
        @SerializedName("aadhar_card")
        @Expose
        private String aadharCard;
        @SerializedName("bank_passbook")
        @Expose
        private String bankPassbook;
        @SerializedName("passport_image")
        @Expose
        private String passportImage;
        @SerializedName("terminal")
        @Expose
        private String terminal;
        @SerializedName("send_email")
        @Expose
        private String sendEmail;
        @SerializedName("two_wheeler_rate")
        @Expose
        private String twoWheelerRate;
        @SerializedName("four_wheeler_rate")
        @Expose
        private String fourWheelerRate;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDesignationId() {
            return designationId;
        }

        public void setDesignationId(String designationId) {
            this.designationId = designationId;
        }

        public String getEmpId() {
            return empId;
        }

        public void setEmpId(String empId) {
            this.empId = empId;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getDoj() {
            return doj;
        }

        public void setDoj(String doj) {
            this.doj = doj;
        }

        public String getDoe() {
            return doe;
        }

        public void setDoe(String doe) {
            this.doe = doe;
        }

        public String getPersonalPhone() {
            return personalPhone;
        }

        public void setPersonalPhone(String personalPhone) {
            this.personalPhone = personalPhone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getIfsCode() {
            return ifsCode;
        }

        public void setIfsCode(String ifsCode) {
            this.ifsCode = ifsCode;
        }

        public String getPanCard() {
            return panCard;
        }

        public void setPanCard(String panCard) {
            this.panCard = panCard;
        }

        public String getAadharCard() {
            return aadharCard;
        }

        public void setAadharCard(String aadharCard) {
            this.aadharCard = aadharCard;
        }

        public String getBankPassbook() {
            return bankPassbook;
        }

        public void setBankPassbook(String bankPassbook) {
            this.bankPassbook = bankPassbook;
        }

        public String getPassportImage() {
            return passportImage;
        }

        public void setPassportImage(String passportImage) {
            this.passportImage = passportImage;
        }

        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public String getSendEmail() {
            return sendEmail;
        }

        public void setSendEmail(String sendEmail) {
            this.sendEmail = sendEmail;
        }

        public String getTwoWheelerRate() {
            return twoWheelerRate;
        }

        public void setTwoWheelerRate(String twoWheelerRate) {
            this.twoWheelerRate = twoWheelerRate;
        }

        public String getFourWheelerRate() {
            return fourWheelerRate;
        }

        public void setFourWheelerRate(String fourWheelerRate) {
            this.fourWheelerRate = fourWheelerRate;
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
