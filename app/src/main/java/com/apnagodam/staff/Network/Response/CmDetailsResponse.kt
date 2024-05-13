package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CmDetailsResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    private var data: ArrayList<Datum>? = null
    fun getData(): ArrayList<Datum>? {
        return data
    }

    fun setData(data: ArrayList<Datum>?) {
        this.data = data
    }

    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("unique_id")
        @Expose
        var uniqueId: String? = null

        @SerializedName("vendor_first_name")
        @Expose
        var vendorFirstName: String? = null

        @SerializedName("vendor_last_name")
        @Expose
        var vendorLastName: Any? = null
    }
}
