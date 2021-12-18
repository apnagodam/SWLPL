package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllVendorConvancyList extends BaseResponse {
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getRequest_count() {
        return Request_count;
    }

    public void setRequest_count(Integer request_count) {
        Request_count = request_count;
    }

    @SerializedName("Request_count")
    @Expose
    private Integer Request_count;
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
        @SerializedName("emp_user_id")
        @Expose
        private String empUserId;
        @SerializedName("terminal_id")
        @Expose
        private String terminalId;
        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("expenses_id")
        @Expose
        private String expensesId;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("final_price")
        @Expose
        private String finalPrice;
        @SerializedName("exp_image_1")
        @Expose
        private String expImage1;
        @SerializedName("exp_image_2")
        @Expose
        private String expImage2;
        @SerializedName("exp_image_3")
        @Expose
        private String expImage3;
        @SerializedName("purpose")
        @Expose
        private String purpose;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("approved_by")
        @Expose
        private String approvedBy;
        @SerializedName("verify_by")
        @Expose
        private String verifyBy;
        @SerializedName("account_maker_userid")
        @Expose
        private String accountMakerUserid;
        @SerializedName("account_checker_userid")
        @Expose
        private String accountCheckerUserid;
        @SerializedName("account_author_userid")
        @Expose
        private String accountAuthorUserid;
        @SerializedName("verfiy_status")
        @Expose
        private String verfiyStatus;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("vendor_first_name")
        @Expose
        private String vendorFirstName;
        @SerializedName("vendor_last_name")
        @Expose
        private String vendorLastName;
        @SerializedName("vendorUniqueId")
        @Expose
        private String vendorUniqueId;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("emp_id")
        @Expose
        private String empId;
        @SerializedName("approvel_fname")
        @Expose
        private String approvelFname;
        @SerializedName("approvel_lname")
        @Expose
        private String approvelLname;
        @SerializedName("approvel_empID")
        @Expose
        private String approvelEmpID;
        @SerializedName("warehouse_name")
        @Expose
        private String warehouseName;
        @SerializedName("warehouse_code")
        @Expose
        private String warehouseCode;

        @SerializedName("expenses_name")
        @Expose
        private String expensesName;
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

        public String getEmpUserId() {
            return empUserId;
        }

        public void setEmpUserId(String empUserId) {
            this.empUserId = empUserId;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getExpensesId() {
            return expensesId;
        }

        public void setExpensesId(String expensesId) {
            this.expensesId = expensesId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(String finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String getExpImage1() {
            return expImage1;
        }

        public void setExpImage1(String expImage1) {
            this.expImage1 = expImage1;
        }

        public String getExpImage2() {
            return expImage2;
        }

        public void setExpImage2(String expImage2) {
            this.expImage2 = expImage2;
        }

        public String getExpImage3() {
            return expImage3;
        }

        public void setExpImage3(String expImage3) {
            this.expImage3 = expImage3;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getVerifyBy() {
            return verifyBy;
        }

        public void setVerifyBy(String verifyBy) {
            this.verifyBy = verifyBy;
        }

        public String getAccountMakerUserid() {
            return accountMakerUserid;
        }

        public void setAccountMakerUserid(String accountMakerUserid) {
            this.accountMakerUserid = accountMakerUserid;
        }

        public String getAccountCheckerUserid() {
            return accountCheckerUserid;
        }

        public void setAccountCheckerUserid(String accountCheckerUserid) {
            this.accountCheckerUserid = accountCheckerUserid;
        }

        public String getAccountAuthorUserid() {
            return accountAuthorUserid;
        }

        public void setAccountAuthorUserid(String accountAuthorUserid) {
            this.accountAuthorUserid = accountAuthorUserid;
        }

        public String getVerfiyStatus() {
            return verfiyStatus;
        }

        public void setVerfiyStatus(String verfiyStatus) {
            this.verfiyStatus = verfiyStatus;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
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

        public String getVendorUniqueId() {
            return vendorUniqueId;
        }

        public void setVendorUniqueId(String vendorUniqueId) {
            this.vendorUniqueId = vendorUniqueId;
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

        public String getApprovelFname() {
            return approvelFname;
        }

        public void setApprovelFname(String approvelFname) {
            this.approvelFname = approvelFname;
        }

        public String getApprovelLname() {
            return approvelLname;
        }

        public void setApprovelLname(String approvelLname) {
            this.approvelLname = approvelLname;
        }

        public String getApprovelEmpID() {
            return approvelEmpID;
        }

        public void setApprovelEmpID(String approvelEmpID) {
            this.approvelEmpID = approvelEmpID;
        }

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
        }

        public String getWarehouseCode() {
            return warehouseCode;
        }

        public void setWarehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
        }

        public String getExpensesName() {
            return expensesName;
        }

        public void setExpensesName(String expensesName) {
            this.expensesName = expensesName;
        }

    }
}
