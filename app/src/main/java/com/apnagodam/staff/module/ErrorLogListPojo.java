package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorLogListPojo extends BaseResponse {
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
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("log")
        @Expose
        private String log;
        @SerializedName("error")
        @Expose
        private String error;
        @SerializedName("correction")
        @Expose
        private String correction;
        @SerializedName("future_correction")
        @Expose
        private String futureCorrection;
        @SerializedName("final_conclusion")
        @Expose
        private String finalConclusion;
        @SerializedName("error_img_1")
        @Expose
        private String errorImg1;
        @SerializedName("error_img_2")
        @Expose
        private String errorImg2;
        @SerializedName("approve_by")
        @Expose
        private String approveBy;
        @SerializedName("verify_by")
        @Expose
        private String verifyBy;
        @SerializedName("verify")
        @Expose
        private String verify;

        public String getIt_action() {
            return it_action;
        }

        public void setIt_action(String it_action) {
            this.it_action = it_action;
        }

        @SerializedName("it_action")
        @Expose
        private String it_action;
        @SerializedName("status")
        @Expose
        private String status;
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
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("empID")
        @Expose
        private String empID;

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

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getCorrection() {
            return correction;
        }

        public void setCorrection(String correction) {
            this.correction = correction;
        }

        public String getFutureCorrection() {
            return futureCorrection;
        }

        public void setFutureCorrection(String futureCorrection) {
            this.futureCorrection = futureCorrection;
        }

        public String getFinalConclusion() {
            return finalConclusion;
        }

        public void setFinalConclusion(String finalConclusion) {
            this.finalConclusion = finalConclusion;
        }

        public String getErrorImg1() {
            return errorImg1;
        }

        public void setErrorImg1(String errorImg1) {
            this.errorImg1 = errorImg1;
        }

        public String getErrorImg2() {
            return errorImg2;
        }

        public void setErrorImg2(String errorImg2) {
            this.errorImg2 = errorImg2;
        }

        public String getApproveBy() {
            return approveBy;
        }

        public void setApproveBy(String approveBy) {
            this.approveBy = approveBy;
        }

        public String getVerifyBy() {
            return verifyBy;
        }

        public void setVerifyBy(String verifyBy) {
            this.verifyBy = verifyBy;
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

        public String getEmpID() {
            return empID;
        }

        public void setEmpID(String empID) {
            this.empID = empID;
        }

    }
}
