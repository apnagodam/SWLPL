
package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StackListPojo extends BaseResponse {
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
        private String id;
        @SerializedName("stack_id")
        @Expose
        private String stackId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("stack_number")
        @Expose
        private String stackNumber;
        @SerializedName("request_weight")
        @Expose
        private String requestWeight;

        public String getVehicle_no() {
            return vehicle_no;
        }

        public void setVehicle_no(String vehicle_no) {
            this.vehicle_no = vehicle_no;
        }

        @SerializedName("vehicle_no")
        @Expose
        private String vehicle_no;
        @SerializedName("in_out_status")
        @Expose
        private String inOutStatus;
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

        public String getStackId() {
            return stackId;
        }

        public void setStackId(String stackId) {
            this.stackId = stackId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStackNumber() {
            return stackNumber;
        }

        public void setStackNumber(String stackNumber) {
            this.stackNumber = stackNumber;
        }

        public String getRequestWeight() {
            return requestWeight;
        }

        public void setRequestWeight(String requestWeight) {
            this.requestWeight = requestWeight;
        }

        public String getInOutStatus() {
            return inOutStatus;
        }

        public void setInOutStatus(String inOutStatus) {
            this.inOutStatus = inOutStatus;
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
}
