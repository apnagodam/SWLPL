package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DispleasedListResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<Datum>? = null

    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("emp_user_id")
        @Expose
        var empUserId: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("terminal_id")
        @Expose
        var terminalId: Int? = null

        @SerializedName("commodity_id")
        @Expose
        var commodityId: Int? = null

        @SerializedName("stack_number")
        @Expose
        var stackNumber: String? = null

        @SerializedName("net_weight")
        @Expose
        var netWeight: String? = null

        @SerializedName("bags")
        @Expose
        var bags: String? = null

        @SerializedName("emp_displege_notes")
        @Expose
        var empDisplegeNotes: String? = null

        @SerializedName("displedge_image")
        @Expose
        var displedgeImage: String? = null

        @SerializedName("approve_by")
        @Expose
        var approveBy: Int? = null

        @SerializedName("verify_by")
        @Expose
        var verifyBy: Any? = null

        @SerializedName("verfiy_status")
        @Expose
        var verfiyStatus: Int? = null

        @SerializedName("notes")
        @Expose
        var notes: Any? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("terminal_name")
        @Expose
        var terminalName: String? = null

        @SerializedName("warehouse_code")
        @Expose
        var warehouseCode: String? = null

        @SerializedName("user_name")
        @Expose
        var userName: String? = null

        @SerializedName("user_phone_no")
        @Expose
        var userPhoneNo: String? = null

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null

        @SerializedName("emp_id")
        @Expose
        var empId: String? = null

        @SerializedName("category")
        @Expose
        var category: String? = null
    }
}
