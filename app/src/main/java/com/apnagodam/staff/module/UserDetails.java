package com.apnagodam.staff.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails {
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getPersonal_phone() {
        return personal_phone;
    }

    public void setPersonal_phone(String personal_phone) {
        this.personal_phone = personal_phone;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    @SerializedName("designation")
    @Expose
    private String designation;

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getTwo_wheeler_rate() {
        return two_wheeler_rate;
    }

    public void setTwo_wheeler_rate(String two_wheeler_rate) {
        this.two_wheeler_rate = two_wheeler_rate;
    }

    @SerializedName("two_wheeler_rate")
    @Expose
    private String two_wheeler_rate;

    public String getFour_wheeler_rate() {
        return four_wheeler_rate;
    }

    public void setFour_wheeler_rate(String four_wheeler_rate) {
        this.four_wheeler_rate = four_wheeler_rate;
    }

    @SerializedName("four_wheeler_rate")
    @Expose
    private String four_wheeler_rate;







    @SerializedName("level_id")
    @Expose
    private String level_id;
    @SerializedName("designation_id")
    @Expose
    private String role_id;
    @SerializedName("personal_phone")
    @Expose
    private String personal_phone;
    @SerializedName("emp_id")
    @Expose
    private String emp_id;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("first_name")
    @Expose
    private String fname;
    @SerializedName("last_name")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("referral_by")
    @Expose
    private String referralBy;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("gst_number")
    @Expose
    private String gstNumber;
    @SerializedName("khasra_no")
    @Expose
    private String khasraNo;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("tehsil")
    @Expose
    private String tehsil;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("power")
    @Expose
    private String power;
    @SerializedName("aadhar_card")
    @Expose
    private String aadharNo;
    @SerializedName("pan_card")
    @Expose
    private String pancardNo;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_branch")
    @Expose
    private String bankBranch;
    @SerializedName("account_no")
    @Expose
    private String bankAccNo;
    @SerializedName("bank_ifsc_code")
    @Expose
    private String bankIfscCode;
    @SerializedName("passport_image")
    @Expose
    private String profileImage;
    @SerializedName("aadhar_image")
    @Expose
    private String aadharImage;
    @SerializedName("cheque_image")
    @Expose
    private String chequeImage;
    @SerializedName("firm_name")
    @Expose
    private String firmName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("area_vilage")
    @Expose
    private String areaVilage;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("mandi_license")
    @Expose
    private String mandiLicense;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("transfer_amount")
    @Expose
    private String transferAmount;
    @SerializedName("verified_account")
    @Expose
    private String verifiedAccount;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferralBy() {
        return referralBy;
    }

    public void setReferralBy(String referralBy) {
        this.referralBy = referralBy;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPancardNo() {
        return pancardNo;
    }

    public void setPancardNo(String pancardNo) {
        this.pancardNo = pancardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAadharImage() {
        return aadharImage;
    }

    public void setAadharImage(String aadharImage) {
        this.aadharImage = aadharImage;
    }

    public String getChequeImage() {
        return chequeImage;
    }

    public void setChequeImage(String chequeImage) {
        this.chequeImage = chequeImage;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaVilage() {
        return areaVilage;
    }

    public void setAreaVilage(String areaVilage) {
        this.areaVilage = areaVilage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMandiLicense() {
        return mandiLicense;
    }

    public void setMandiLicense(String mandiLicense) {
        this.mandiLicense = mandiLicense;
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

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getVerifiedAccount() {
        return verifiedAccount;
    }

    public void setVerifiedAccount(String verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
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
