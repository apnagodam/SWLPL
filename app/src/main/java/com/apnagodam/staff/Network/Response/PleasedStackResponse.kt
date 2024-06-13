package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PleasedStackResponse {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    inner class Datum {
        @SerializedName("stack_id")
        @Expose
        var stackId: Int? = null

        @SerializedName("stack_no")
        @Expose
        var stackNo: String? = null
    }
}
