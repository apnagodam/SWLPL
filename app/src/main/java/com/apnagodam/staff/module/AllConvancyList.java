package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllConvancyList extends BaseResponse {
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
        @SerializedName("current_page")
        @Expose
        private Integer currentPage;

        public List<Datum> getDataa() {
            return dataa;
        }

        public void setDataa(List<Datum> dataa) {
            this.dataa = dataa;
        }

        @SerializedName("data")
        @Expose
        private List<Datum> dataa = null;
        @SerializedName("first_page_url")
        @Expose
        private String firstPageUrl;
        @SerializedName("from")
        @Expose
        private Integer from;
        @SerializedName("last_page")
        @Expose
        private Integer lastPage;
        @SerializedName("last_page_url")
        @Expose
        private String lastPageUrl;
        @SerializedName("next_page_url")
        @Expose
        private String nextPageUrl;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("per_page")
        @Expose
        private String perPage;
        @SerializedName("prev_page_url")
        @Expose
        private String prevPageUrl;
        @SerializedName("to")
        @Expose
        private Integer to;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }


        public String getFirstPageUrl() {
            return firstPageUrl;
        }

        public void setFirstPageUrl(String firstPageUrl) {
            this.firstPageUrl = firstPageUrl;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getLastPage() {
            return lastPage;
        }

        public void setLastPage(Integer lastPage) {
            this.lastPage = lastPage;
        }

        public String getLastPageUrl() {
            return lastPageUrl;
        }

        public void setLastPageUrl(String lastPageUrl) {
            this.lastPageUrl = lastPageUrl;
        }

        public String getNextPageUrl() {
            return nextPageUrl;
        }

        public void setNextPageUrl(String nextPageUrl) {
            this.nextPageUrl = nextPageUrl;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPerPage() {
            return perPage;
        }

        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        public String getPrevPageUrl() {
            return prevPageUrl;
        }

        public void setPrevPageUrl(String prevPageUrl) {
            this.prevPageUrl = prevPageUrl;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }
    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("from_place")
        @Expose
        private String fromPlace;
        @SerializedName("to_place")
        @Expose
        private String toPlace;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("start_reading")
        @Expose
        private String startReading;

        public String getConveyance_type() {
            return conveyance_type;
        }

        public void setConveyance_type(String conveyance_type) {
            this.conveyance_type = conveyance_type;
        }

        @SerializedName("conveyance_type")
        @Expose
        private String conveyance_type;
        @SerializedName("end_reading")
        @Expose
        private String endReading;
        @SerializedName("kms")
        @Expose
        private String kms;
        @SerializedName("charges")
        @Expose
        private String charges;
        @SerializedName("other_expense")
        @Expose
        private String otherExpense;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("final_prize")
        @Expose
        private String finalPrize;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("purpose")
        @Expose
        private String purpose;
        @SerializedName("approved_by")
        @Expose
        private String approvedBy;
        @SerializedName("image_2")
        @Expose
        private String image2;

        public String getOther_charge_img() {
            return other_charge_img;
        }

        public void setOther_charge_img(String other_charge_img) {
            this.other_charge_img = other_charge_img;
        }

        @SerializedName("other_charge_img")
        @Expose
        private String other_charge_img;
        @SerializedName("verify")
        @Expose
        private String verify;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("payment")
        @Expose
        private String payment;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("emp_id")
        @Expose
        private String empId;
        @SerializedName("designation_id")
        @Expose
        private String designationId;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;

        public String getUSerempId() {
            return USerempId;
        }

        public void setUSerempId(String USerempId) {
            this.USerempId = USerempId;
        }

        @SerializedName("empId")
        @Expose
        private String USerempId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getFromPlace() {
            return fromPlace;
        }

        public void setFromPlace(String fromPlace) {
            this.fromPlace = fromPlace;
        }

        public String getToPlace() {
            return toPlace;
        }

        public void setToPlace(String toPlace) {
            this.toPlace = toPlace;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getStartReading() {
            return startReading;
        }

        public void setStartReading(String startReading) {
            this.startReading = startReading;
        }

        public String getEndReading() {
            return endReading;
        }

        public void setEndReading(String endReading) {
            this.endReading = endReading;
        }

        public String getKms() {
            return kms;
        }

        public void setKms(String kms) {
            this.kms = kms;
        }

        public String getCharges() {
            return charges;
        }

        public void setCharges(String charges) {
            this.charges = charges;
        }

        public String getOtherExpense() {
            return otherExpense;
        }

        public void setOtherExpense(String otherExpense) {
            this.otherExpense = otherExpense;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFinalPrize() {
            return finalPrize;
        }

        public void setFinalPrize(String finalPrize) {
            this.finalPrize = finalPrize;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
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

        public String getEmpId() {
            return empId;
        }

        public void setEmpId(String empId) {
            this.empId = empId;
        }

        public String getDesignationId() {
            return designationId;
        }

        public void setDesignationId(String designationId) {
            this.designationId = designationId;
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

    }
}
