package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InventoryRespionse extends BaseResponse {
    public List<Inventory> getInventories() {
        return inventories;
    }
    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
    @SerializedName("inventories")
    @Expose
    private List<Inventory> inventories = null;

    public class Inventory {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("warehouse_id")
        @Expose
        private Object warehouseId;
        @SerializedName("commodity")
        @Expose
        private String commodity;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("type")
        @Expose
        private Object type;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("quality_category")
        @Expose
        private Object qualityCategory;
        @SerializedName("sell_quantity")
        @Expose
        private Object sellQuantity;
        @SerializedName("image")
        @Expose
        private Object image;
        @SerializedName("commodity_image")
        @Expose
        private String commodityImage;
        @SerializedName("commodity_image_path")
        @Expose
        private String commodityImagePath;
        @SerializedName("sales_status")
        @Expose
        private Object salesStatus;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("cat_name")
        @Expose
        private String catName;

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

        public Object getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(Object warehouseId) {
            this.warehouseId = warehouseId;
        }

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Object getQualityCategory() {
            return qualityCategory;
        }

        public void setQualityCategory(Object qualityCategory) {
            this.qualityCategory = qualityCategory;
        }
        public String getCommodityImagePath() {
            return commodityImagePath;
        }

        public void setCommodityImagePath(String commodityImagePath) {
            this.commodityImagePath = commodityImagePath;
        }
        public Object getSellQuantity() {
            return sellQuantity;
        }

        public void setSellQuantity(Object sellQuantity) {
            this.sellQuantity = sellQuantity;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public String getCommodityImage() {
            return commodityImage;
        }

        public void setCommodityImage(String commodityImage) {
            this.commodityImage = commodityImage;
        }

        public Object getSalesStatus() {
            return salesStatus;
        }

        public void setSalesStatus(Object salesStatus) {
            this.salesStatus = salesStatus;
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

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }
    }
}
