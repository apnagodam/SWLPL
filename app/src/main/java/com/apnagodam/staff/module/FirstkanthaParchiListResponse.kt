package com.apnagodam.staff.module

import com.apnagodam.staff.Network.Response.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FirstkanthaParchiListResponse : BaseResponse(), Serializable {

    @SerializedName("data")
    @Expose
    var firstKataParchiData: FirstKataParchiDatum? = null

    @SerializedName("dharem_kanta")
    @Expose
    var dharemKantas: List<DharemKanta>? = null
        private set

    inner class FirstKataParchiDatum {
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

        @SerializedName("next_page_url")
        @Expose
        var nextPageUrl: String? = null

        @SerializedName("path")
        @Expose
        var path: String? = null

        @SerializedName("per_page")
        @Expose
        var perPage: String? = null

        @SerializedName("prev_page_url")
        @Expose
        var prevPageUrl: String? = null

        @SerializedName("to")
        @Expose
        var to: Int? = null

        @SerializedName("total")
        @Expose
        var total: Int? = null
    }

    inner class DharemKanta {
        @SerializedName("id")
        var id = 0

        @SerializedName("name")
        var name: String? = null

        @SerializedName("operator_name")
        var operatorName: String? = null

        @SerializedName("phone")
        var phone: String? = null

        @SerializedName("location")
        var location: String? = null

        @SerializedName("length")
        var length: String? = null

        @SerializedName("capicity")
        var capicity: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("status")
        var status = 0
    }

    inner class Datum : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @JvmField
        @SerializedName("case_id")
        @Expose
        var caseId: String? = null

        @SerializedName("gate_pass")
        @Expose
        var gatePass: String? = null

        @SerializedName("in_out")
        @Expose
        var inOut: String? = null

        @SerializedName("customer_uid")
        @Expose
        var customerUid: Int? = null

        @SerializedName("location")
        @Expose
        var location: String? = null

        @SerializedName("commodity_id")
        @Expose
        var commodityId: Int? = null

        @SerializedName("terminal_id")
        @Expose
        var terminalId: Int? = null

        @SerializedName("total_weight")
        @Expose
        var totalWeight: String? = null

        @SerializedName("vehicle_no")
        @Expose
        var vehicleNo: String? = null

        @SerializedName("lead_gen_uid")
        @Expose
        var leadGenUid: Int? = null

        @SerializedName("lead_conv_uid")
        @Expose
        var leadConvUid: Int? = null

        @SerializedName("purpose")
        @Expose
        var purpose: String? = null

        @SerializedName("fpo_users")
        @Expose
        var fpoUsers: String? = null

        @SerializedName("fpo_user_id")
        @Expose
        var fpoUserId: String? = null

        @SerializedName("gate_pass_cdf_user_name")
        @Expose
        var gatePassCdfUserName: String? = null

        @SerializedName("coldwin_name")
        @Expose
        var coldwinName: String? = null

        @SerializedName("purchase_name")
        @Expose
        var purchaseName: String? = null

        @SerializedName("loan_name")
        @Expose
        var loanName: String? = null

        @SerializedName("sale_name")
        @Expose
        var saleName: String? = null

        @SerializedName("no_of_bags")
        @Expose
        var noOfBags: String? = null

        @SerializedName("cancel_notes")
        @Expose
        var cancelNotes: String? = null

        @SerializedName("approved_remark")
        @Expose
        var approvedRemark: String? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @JvmField
        @SerializedName("cust_fname")
        @Expose
        var custFname: String? = null

        @SerializedName("cust_lname")
        @Expose
        var custLname: String? = null

        @SerializedName("k_p_case_id")
        @Expose
        var kPCaseId: String? = null

        @SerializedName("rst_no")
        @Expose
        var rstNo: String? = null

        @SerializedName("bags")
        @Expose
        var bags: String? = null

        @SerializedName("gross_weight")
        @Expose
        var grossWeight: String? = null

        @SerializedName("tare_weight")
        @Expose
        var tareWeight: String? = null

        @SerializedName("net_weight")
        @Expose
        var netWeight: String? = null

        @SerializedName("gross_date_time")
        @Expose
        var grossDateTime: String? = null

        @SerializedName("tare_date_time")
        @Expose
        var tareDateTime: String? = null

        @SerializedName("charges")
        @Expose
        var charges: String? = null

        @SerializedName("kanta_name")
        @Expose
        var kantaName: String? = null

        @SerializedName("kanta_place")
        @Expose
        var kantaPlace: String? = null

        @SerializedName("file")
        @Expose
        var file: String? = null

        @SerializedName("file_2")
        @Expose
        var file2: String? = null

        @JvmField
        @SerializedName("file_3")
        @Expose
        var file3: String? = null

        @SerializedName("notes")
        @Expose
        var notes: String? = null

        @SerializedName("user_price_fname")
        @Expose
        var userPriceFname: String? = null

        @SerializedName("user_price_lname")
        @Expose
        var userPriceLname: String? = null

        @JvmField
        @SerializedName("l_b_case_id")
        @Expose
        var lBCaseId: String? = null

        @JvmField
        @SerializedName("dharam_kanta")
        @Expose
        var dharamKanta: String? = null

        @SerializedName("dharam_kanta_id")
        @Expose
        var dharamKantaId: String? = null

        @SerializedName("dharam_kanta_name")
        @Expose
        var dharamKantaName: String? = null

        @JvmField
        @SerializedName("f_q_case_id")
        @Expose
        var f_q_case_id: String? = null
    }
}
