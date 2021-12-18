package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DispladgeCommodityPojo extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("case_id")
        @Expose
        private String caseId;
        @SerializedName("warehouse_id")
        @Expose
        private Integer warehouseId;
        @SerializedName("commodity")
        @Expose
        private Integer commodity;
        @SerializedName("weight_bridge_no")
        @Expose
        private String weightBridgeNo;
        @SerializedName("truck_no")
        @Expose
        private String truckNo;
        @SerializedName("stack_no")
        @Expose
        private String stackNo;
        @SerializedName("lot_no")
        @Expose
        private String lotNo;
        @SerializedName("net_weight")
        @Expose
        private String netWeight;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("bags")
        @Expose
        private Integer bags;
        @SerializedName("sell_quantity")
        @Expose
        private String sellQuantity;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("gate_pass_wr")
        @Expose
        private String gatePassWr;
        @SerializedName("quality_category")
        @Expose
        private String qualityCategory;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("file")
        @Expose
        private String file;
        @SerializedName("rent_row_id")
        @Expose
        private String rentRowId;
        @SerializedName("interest_row_id")
        @Expose
        private String interestRowId;
        @SerializedName("mandi_amount")
        @Expose
        private String mandiAmount;
        @SerializedName("settlment_amount")
        @Expose
        private String settlmentAmount;
        @SerializedName("rem_amount")
        @Expose
        private String remAmount;
        @SerializedName("sales_status")
        @Expose
        private Integer salesStatus;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("category_hi")
        @Expose
        private String categoryHi;
        @SerializedName("commodity_type")
        @Expose
        private String commodityType;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public Integer getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(Integer warehouseId) {
            this.warehouseId = warehouseId;
        }

        public Integer getCommodity() {
            return commodity;
        }

        public void setCommodity(Integer commodity) {
            this.commodity = commodity;
        }

        public String getWeightBridgeNo() {
            return weightBridgeNo;
        }

        public void setWeightBridgeNo(String weightBridgeNo) {
            this.weightBridgeNo = weightBridgeNo;
        }

        public String getTruckNo() {
            return truckNo;
        }

        public void setTruckNo(String truckNo) {
            this.truckNo = truckNo;
        }

        public String getStackNo() {
            return stackNo;
        }

        public void setStackNo(String stackNo) {
            this.stackNo = stackNo;
        }

        public String getLotNo() {
            return lotNo;
        }

        public void setLotNo(String lotNo) {
            this.lotNo = lotNo;
        }

        public String getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(String netWeight) {
            this.netWeight = netWeight;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public Integer getBags() {
            return bags;
        }

        public void setBags(Integer bags) {
            this.bags = bags;
        }

        public String getSellQuantity() {
            return sellQuantity;
        }

        public void setSellQuantity(String sellQuantity) {
            this.sellQuantity = sellQuantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGatePassWr() {
            return gatePassWr;
        }

        public void setGatePassWr(String gatePassWr) {
            this.gatePassWr = gatePassWr;
        }

        public String getQualityCategory() {
            return qualityCategory;
        }

        public void setQualityCategory(String qualityCategory) {
            this.qualityCategory = qualityCategory;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getRentRowId() {
            return rentRowId;
        }

        public void setRentRowId(String rentRowId) {
            this.rentRowId = rentRowId;
        }

        public String getInterestRowId() {
            return interestRowId;
        }

        public void setInterestRowId(String interestRowId) {
            this.interestRowId = interestRowId;
        }

        public String getMandiAmount() {
            return mandiAmount;
        }

        public void setMandiAmount(String mandiAmount) {
            this.mandiAmount = mandiAmount;
        }

        public String getSettlmentAmount() {
            return settlmentAmount;
        }

        public void setSettlmentAmount(String settlmentAmount) {
            this.settlmentAmount = settlmentAmount;
        }

        public String getRemAmount() {
            return remAmount;
        }

        public void setRemAmount(String remAmount) {
            this.remAmount = remAmount;
        }

        public Integer getSalesStatus() {
            return salesStatus;
        }

        public void setSalesStatus(Integer salesStatus) {
            this.salesStatus = salesStatus;
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

    }
}
