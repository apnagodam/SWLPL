package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PleasedApproverResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("employees")
    @Expose
    var employees: List<Employee>? = null

    inner class Employee {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("employee")
        @Expose
        var employee: String? = null
    }
}
