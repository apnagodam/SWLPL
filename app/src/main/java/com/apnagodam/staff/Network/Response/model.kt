package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PvResponseModel {
    @SerializedName("terminal_data")
    @Expose
    var terminalData: List<TerminalDatum>? = null

    @SerializedName("stack_data")
    @Expose
    var stackData: List<StackDatum>? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    inner class StackDatum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("terminal_id")
        @Expose
        var terminalId: Int? = null

        @SerializedName("stack_no")
        @Expose
        var stackNo: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("warehouse_code")
        @Expose
        var warehouseCode: String? = null

        @SerializedName("warehouse_id")
        @Expose
        var warehouseId: Int? = null
    }

    inner class TerminalDatum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("terminal_id")
        @Expose
        var terminalId: Int? = null

        @SerializedName("stack_no")
        @Expose
        var stackNo: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("warehouse_code")
        @Expose
        var warehouseCode: String? = null

        @SerializedName("warehouse_id")
        @Expose
        var warehouseId: Int? = null
    }
}
