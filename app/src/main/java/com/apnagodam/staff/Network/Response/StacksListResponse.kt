package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StacksListResponse {
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

        @SerializedName("stack_number")
        @Expose
        var stackNumber: String? = null

        @SerializedName("stack_type")
        @Expose
        var stackType: String? = null

        @SerializedName("commodity_id")
        @Expose
        var commodityId: Int? = null

        @SerializedName("commodity_name")
        @Expose
        var commodityName: String? = null

    }
}
