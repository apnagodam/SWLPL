package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LabourContractorNameResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("contractor_name")
        @Expose
        var contractorName: String? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("contractor_phone")
        @Expose
        var contractorPhone: String? = null

        @SerializedName("labour_rate")
        @Expose
        var labourRate: String? = null
    }
}
