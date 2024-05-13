package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AdvancePaymentListModel {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("current_page")
        @Expose
        var currentPage: Int? = null

        @SerializedName("data")
        @Expose
        var data: List<Datum>? = null

        @SerializedName("first_page_url")
        @Expose
        var firstPageUrl: String? = null

        @SerializedName("from")
        @Expose
        var from: Int? = null

        @SerializedName("last_page")
        @Expose
        var lastPage: Int? = null

        @SerializedName("last_page_url")
        @Expose
        var lastPageUrl: String? = null

        @SerializedName("links")
        @Expose
        var links: List<Link>? = null

        @SerializedName("next_page_url")
        @Expose
        var nextPageUrl: Any? = null

        @SerializedName("path")
        @Expose
        var path: String? = null

        @SerializedName("per_page")
        @Expose
        var perPage: String? = null

        @SerializedName("prev_page_url")
        @Expose
        var prevPageUrl: Any? = null

        @SerializedName("to")
        @Expose
        var to: Int? = null

        @SerializedName("total")
        @Expose
        var total: Int? = null
    }

    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("unique_id")
        @Expose
        var uniqueId: Any? = null

        @SerializedName("requested_amount")
        @Expose
        var requestedAmount: String? = null

        @SerializedName("approved_amount")
        @Expose
        var approvedAmount: Any? = null

        @SerializedName("approved_by")
        @Expose
        var approvedBy: Any? = null

        @SerializedName("verified_by")
        @Expose
        var verifiedBy: Any? = null

        @SerializedName("pout_id")
        @Expose
        var poutId: String? = null

        @SerializedName("fund_account_id")
        @Expose
        var fundAccountId: String? = null

        @SerializedName("contact_id")
        @Expose
        var contactId: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("payment_date")
        @Expose
        var paymentDate: Any? = null

        @SerializedName("notes")
        @Expose
        var notes: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }

    inner class Link {
        @SerializedName("url")
        @Expose
        var url: Any? = null

        @SerializedName("label")
        @Expose
        var label: String? = null

        @SerializedName("active")
        @Expose
        var active: Boolean? = null
    }
}
