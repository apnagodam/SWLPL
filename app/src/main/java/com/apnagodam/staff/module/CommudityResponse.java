package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommudityResponse extends BaseResponse {
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @SerializedName("commodites")
    @Expose
    private List<Category> categories = null;
    public List<Terminals> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Terminals> terminals) {
        this.terminals = terminals;
    }

    @SerializedName("terminals")
    @Expose
    private List<Terminals> terminals = null;


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }

    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("employee")
    @Expose
    private List<Employee> employee = null;

    public class Category {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("category_hi")
        @Expose
        private String categoryHi;
        @SerializedName("commodity_type")
        @Expose
        private String commodityType;
        @SerializedName("gst")
        @Expose
        private String gst;
        @SerializedName("gst_on_rent")
        @Expose
        private String gstOnRent;
        @SerializedName("commossion")
        @Expose
        private String commossion;
        @SerializedName("mandi_fees")
        @Expose
        private String mandiFees;
        @SerializedName("loading")
        @Expose
        private String loading;
        @SerializedName("bardana")
        @Expose
        private String bardana;
        @SerializedName("freight")
        @Expose
        private String freight;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("image_path")
        @Expose
        private String imagePath;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategoryHi() {
            return categoryHi;
        }

        public void setCategoryHi(String categoryHi) {
            this.categoryHi = categoryHi;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }

        public String getGstOnRent() {
            return gstOnRent;
        }

        public void setGstOnRent(String gstOnRent) {
            this.gstOnRent = gstOnRent;
        }

        public String getCommossion() {
            return commossion;
        }

        public void setCommossion(String commossion) {
            this.commossion = commossion;
        }

        public String getMandiFees() {
            return mandiFees;
        }

        public void setMandiFees(String mandiFees) {
            this.mandiFees = mandiFees;
        }

        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
        }

        public String getBardana() {
            return bardana;
        }

        public void setBardana(String bardana) {
            this.bardana = bardana;
        }

        public String getFreight() {
            return freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
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
    public class Employee {

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
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("level")
        @Expose
        private String level;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("district_name")
        @Expose
        private String districtName;
        @SerializedName("location_address")
        @Expose
        private String locationAddress;
        @SerializedName("location_area")
        @Expose
        private String locationArea;
        @SerializedName("level_id")
        @Expose
        private String levelId;

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

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getLocationAddress() {
            return locationAddress;
        }

        public void setLocationAddress(String locationAddress) {
            this.locationAddress = locationAddress;
        }

        public String getLocationArea() {
            return locationArea;
        }

        public void setLocationArea(String locationArea) {
            this.locationArea = locationArea;
        }

        public String getLevelId() {
            return levelId;
        }

        public void setLevelId(String levelId) {
            this.levelId = levelId;
        }

    }
    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
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
        @SerializedName("aadhar_no")
        @Expose
        private String aadharNo;
        @SerializedName("pancard_no")
        @Expose
        private String pancardNo;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("bank_branch")
        @Expose
        private String bankBranch;
        @SerializedName("bank_acc_no")
        @Expose
        private String bankAccNo;
        @SerializedName("bank_ifsc_code")
        @Expose
        private String bankIfscCode;
        @SerializedName("profile_image")
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
        @SerializedName("live_latitude")
        @Expose
        private String liveLatitude;
        @SerializedName("live_longitude")
        @Expose
        private String liveLongitude;
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
        @SerializedName("role_id")
        @Expose
        private String roleId;

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

        public String getLiveLatitude() {
            return liveLatitude;
        }

        public void setLiveLatitude(String liveLatitude) {
            this.liveLatitude = liveLatitude;
        }

        public String getLiveLongitude() {
            return liveLongitude;
        }

        public void setLiveLongitude(String liveLongitude) {
            this.liveLongitude = liveLongitude;
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

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

    }
}
