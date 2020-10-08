package com.apnagodam.staff.module;

import com.apnagodam.staff.Network.Response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllUserPermissionsResultListResponse extends BaseResponse {

    @SerializedName("user_permissions_result")
    @Expose
    private List<UserPermissionsResult> userPermissionsResult = null;
    public List<UserPermissionsResult> getUserPermissionsResult() {
        return userPermissionsResult;
    }

    public void setUserPermissionsResult(List<UserPermissionsResult> userPermissionsResult) {
        this.userPermissionsResult = userPermissionsResult;
    }
    public class UserPermissionsResult {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("permission_id")
        @Expose
        private String permissionId;
        @SerializedName("designation_id")
        @Expose
        private Integer designationId;
        @SerializedName("emp_level_id")
        @Expose
        private Integer empLevelId;
        @SerializedName("edit")
        @Expose
        private Integer edit;
        @SerializedName("view")
        @Expose
        private Integer view;
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

        public String getPermissionId() {
            return permissionId;
        }

        public void setPermissionId(String permissionId) {
            this.permissionId = permissionId;
        }

        public Integer getDesignationId() {
            return designationId;
        }

        public void setDesignationId(Integer designationId) {
            this.designationId = designationId;
        }

        public Integer getEmpLevelId() {
            return empLevelId;
        }

        public void setEmpLevelId(Integer empLevelId) {
            this.empLevelId = empLevelId;
        }

        public Integer getEdit() {
            return edit;
        }

        public void setEdit(Integer edit) {
            this.edit = edit;
        }

        public Integer getView() {
            return view;
        }

        public void setView(Integer view) {
            this.view = view;
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
