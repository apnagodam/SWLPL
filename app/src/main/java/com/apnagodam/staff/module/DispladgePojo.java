package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DispladgePojo extends BaseResponse {
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getRequest_count() {
        return Request_count;
    }

    public void setRequest_count(Integer request_count) {
        Request_count = request_count;
    }

    @SerializedName("req_count")
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
        @SerializedName("emp_user_id")
        @Expose
        private Integer empUserId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("terminal_id")
        @Expose
        private Integer terminalId;
        @SerializedName("commodity_id")
        @Expose
        private Integer commodityId;
        @SerializedName("stack_number")
        @Expose
        private String stackNumber;
        @SerializedName("net_weight")
        @Expose
        private String netWeight;

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("bags")
        @Expose
        private String bags;
        @SerializedName("approve_by")
        @Expose
        private Integer approveBy;
        @SerializedName("verify_by")
        @Expose
        private String verifyBy;
        @SerializedName("verfiy_status")
        @Expose
        private Integer verfiyStatus;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("terminal_name")
        @Expose
        private String terminalName;
        @SerializedName("warehouse_code")
        @Expose
        private String warehouseCode;
        @SerializedName("user_name")
        @Expose
        private String userName;

        public String getEmp_displege_notes() {
            return emp_displege_notes;
        }

        public void setEmp_displege_notes(String emp_displege_notes) {
            this.emp_displege_notes = emp_displege_notes;
        }

        public String getDispledge_image() {
            return displedge_image;
        }

        public void setDispledge_image(String displedge_image) {
            this.displedge_image = displedge_image;
        }

        @SerializedName("emp_displege_notes")
        @Expose
        private String emp_displege_notes;
        @SerializedName("displedge_image")
        @Expose
        private String displedge_image;


        @SerializedName("user_phone_no")
        @Expose
        private String userPhoneNo;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("emp_id")
        @Expose
        private String empId;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("commodity_type")
        @Expose
        private String commodityType;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getEmpUserId() {
            return empUserId;
        }

        public void setEmpUserId(Integer empUserId) {
            this.empUserId = empUserId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(Integer terminalId) {
            this.terminalId = terminalId;
        }

        public Integer getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(Integer commodityId) {
            this.commodityId = commodityId;
        }

        public String getStackNumber() {
            return stackNumber;
        }

        public void setStackNumber(String stackNumber) {
            this.stackNumber = stackNumber;
        }

        public String getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(String netWeight) {
            this.netWeight = netWeight;
        }

        public String getBags() {
            return bags;
        }

        public void setBags(String bags) {
            this.bags = bags;
        }

        public Integer getApproveBy() {
            return approveBy;
        }

        public void setApproveBy(Integer approveBy) {
            this.approveBy = approveBy;
        }

        public String getVerifyBy() {
            return verifyBy;
        }

        public void setVerifyBy(String verifyBy) {
            this.verifyBy = verifyBy;
        }

        public Integer getVerfiyStatus() {
            return verfiyStatus;
        }

        public void setVerfiyStatus(Integer verfiyStatus) {
            this.verfiyStatus = verfiyStatus;
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

        public String getTerminalName() {
            return terminalName;
        }

        public void setTerminalName(String terminalName) {
            this.terminalName = terminalName;
        }

        public String getWarehouseCode() {
            return warehouseCode;
        }

        public void setWarehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhoneNo() {
            return userPhoneNo;
        }

        public void setUserPhoneNo(String userPhoneNo) {
            this.userPhoneNo = userPhoneNo;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

    }
}
