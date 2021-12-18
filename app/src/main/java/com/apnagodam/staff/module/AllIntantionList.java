package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllIntantionList extends BaseResponse {
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
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("terminal_id")
        @Expose
        private String terminalId;
        @SerializedName("commudity_id")
        @Expose
        private String commudityId;
        @SerializedName("gst_no")
        @Expose
        private String gstNo;
        @SerializedName("quality_grade")
        @Expose
        private String qualityGrade;
        @SerializedName("packing")
        @Expose
        private String packing;
        @SerializedName("total_bags")
        @Expose
        private String totalBags;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("gst_type")
        @Expose
        private String gstType;
        @SerializedName("sell_price")
        @Expose
        private String sellPrice;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("appoveby_user_id")
        @Expose
        private String appovebyUserId;
        @SerializedName("live_latitude")
        @Expose
        private String liveLatitude;
        @SerializedName("live_longitude")
        @Expose
        private String liveLongitude;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("warehouse_name")
        @Expose
        private String warehouseName;
        @SerializedName("warehouse_code")
        @Expose
        private String warehouseCode;


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

        public String getArea_vilage() {
            return area_vilage;
        }

        public void setArea_vilage(String area_vilage) {
            this.area_vilage = area_vilage;
        }

        @SerializedName("area_vilage")
        @Expose
        private String area_vilage;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;

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

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getCommudityId() {
            return commudityId;
        }

        public void setCommudityId(String commudityId) {
            this.commudityId = commudityId;
        }

        public String getGstNo() {
            return gstNo;
        }

        public void setGstNo(String gstNo) {
            this.gstNo = gstNo;
        }

        public String getQualityGrade() {
            return qualityGrade;
        }

        public void setQualityGrade(String qualityGrade) {
            this.qualityGrade = qualityGrade;
        }

        public String getPacking() {
            return packing;
        }

        public void setPacking(String packing) {
            this.packing = packing;
        }

        public String getTotalBags() {
            return totalBags;
        }

        public void setTotalBags(String totalBags) {
            this.totalBags = totalBags;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getGstType() {
            return gstType;
        }

        public void setGstType(String gstType) {
            this.gstType = gstType;
        }

        public String getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getAppovebyUserId() {
            return appovebyUserId;
        }

        public void setAppovebyUserId(String appovebyUserId) {
            this.appovebyUserId = appovebyUserId;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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
    }
}
