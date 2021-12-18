package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.LoginResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackApprovalLisPojo extends LoginResponse {
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
        @SerializedName("emp_user_id")
        @Expose
        private Integer empUserId;
        @SerializedName("feedback_department")
        @Expose
        private Integer feedbackDepartment;
        @SerializedName("process_problem")
        @Expose
        private String processProblem;
        @SerializedName("process_solution")
        @Expose
        private String processSolution;
        @SerializedName("approve_by")
        @Expose
        private String approveBy;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("emp_id")
        @Expose
        private String empId;

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

        public Integer getFeedbackDepartment() {
            return feedbackDepartment;
        }

        public void setFeedbackDepartment(Integer feedbackDepartment) {
            this.feedbackDepartment = feedbackDepartment;
        }

        public String getProcessProblem() {
            return processProblem;
        }

        public void setProcessProblem(String processProblem) {
            this.processProblem = processProblem;
        }

        public String getProcessSolution() {
            return processSolution;
        }

        public void setProcessSolution(String processSolution) {
            this.processSolution = processSolution;
        }

        public String getApproveBy() {
            return approveBy;
        }

        public void setApproveBy(String approveBy) {
            this.approveBy = approveBy;
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

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
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

    }
}
