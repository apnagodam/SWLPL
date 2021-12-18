package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PVGetListPojo extends BaseResponse {
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
        @SerializedName("terminal_id")
        @Expose
        private Integer terminalId;
        @SerializedName("emp_id")
        @Expose
        private Integer empId;
        @SerializedName("commodity_id")
        @Expose
        private Integer commodityId;
        @SerializedName("stack_no")
        @Expose
        private Integer stackNo;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("warehouse_code")
        @Expose
        private String warehouseCode;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("emp_fname")
        @Expose
        private String empFname;
        @SerializedName("emp_lname")
        @Expose
        private String empLname;
        @SerializedName("emp_phone")
        @Expose
        private String empPhone;

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

        public Integer getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(Integer terminalId) {
            this.terminalId = terminalId;
        }

        public Integer getEmpId() {
            return empId;
        }

        public void setEmpId(Integer empId) {
            this.empId = empId;
        }

        public Integer getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(Integer commodityId) {
            this.commodityId = commodityId;
        }

        public Integer getStackNo() {
            return stackNo;
        }

        public void setStackNo(Integer stackNo) {
            this.stackNo = stackNo;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWarehouseCode() {
            return warehouseCode;
        }

        public void setWarehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
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

        public String getEmpFname() {
            return empFname;
        }

        public void setEmpFname(String empFname) {
            this.empFname = empFname;
        }

        public String getEmpLname() {
            return empLname;
        }

        public void setEmpLname(String empLname) {
            this.empLname = empLname;
        }

        public String getEmpPhone() {
            return empPhone;
        }

        public void setEmpPhone(String empPhone) {
            this.empPhone = empPhone;
        }

    }
}
