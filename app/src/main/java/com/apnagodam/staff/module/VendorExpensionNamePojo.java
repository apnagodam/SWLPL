package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VendorExpensionNamePojo extends BaseResponse {
    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<WherHouseName> getWarehouse_detail() {
        return warehouse_detail;
    }

    public void setWarehouse_detail(List<WherHouseName> warehouse_detail) {
        this.warehouse_detail = warehouse_detail;
    }

    @SerializedName("warehouse_detail")
    @Expose
    private List<WherHouseName> warehouse_detail = null;
    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("expenses_name")
        @Expose
        private String expensesName;
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

        public String getExpensesName() {
            return expensesName;
        }

        public void setExpensesName(String expensesName) {
            this.expensesName = expensesName;
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
    public  class WherHouseName {
        @SerializedName("id")
        @Expose
        private String id;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWarehouse_code() {
            return warehouse_code;
        }

        public void setWarehouse_code(String warehouse_code) {
            this.warehouse_code = warehouse_code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @SerializedName("warehouse_code")
        @Expose
        private String warehouse_code;
        @SerializedName("name")
        @Expose
        private String name;
    }
}
