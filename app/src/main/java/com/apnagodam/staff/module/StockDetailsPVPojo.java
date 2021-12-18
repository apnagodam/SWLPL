package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockDetailsPVPojo extends BaseResponse {
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
        @SerializedName("pv_id")
        @Expose
        private Integer pvId;
        @SerializedName("stack_no")
        @Expose
        private Integer stackNo;
        @SerializedName("block_no")
        @Expose
        private Integer blockNo;
        @SerializedName("dhang")
        @Expose
        private Integer dhang;
        @SerializedName("danda")
        @Expose
        private Integer danda;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("plus_minus")
        @Expose
        private Integer plusMinus;
        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("remark")
        @Expose
        private String remark;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPvId() {
            return pvId;
        }

        public void setPvId(Integer pvId) {
            this.pvId = pvId;
        }

        public Integer getStackNo() {
            return stackNo;
        }

        public void setStackNo(Integer stackNo) {
            this.stackNo = stackNo;
        }

        public Integer getBlockNo() {
            return blockNo;
        }

        public void setBlockNo(Integer blockNo) {
            this.blockNo = blockNo;
        }

        public Integer getDhang() {
            return dhang;
        }

        public void setDhang(Integer dhang) {
            this.dhang = dhang;
        }

        public Integer getDanda() {
            return danda;
        }

        public void setDanda(Integer danda) {
            this.danda = danda;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getPlusMinus() {
            return plusMinus;
        }

        public void setPlusMinus(Integer plusMinus) {
            this.plusMinus = plusMinus;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

    }
}
