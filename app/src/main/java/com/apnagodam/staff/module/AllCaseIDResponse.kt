package com.apnagodam.staff.module

import com.apnagodam.staff.Network.Response.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AllCaseIDResponse : BaseResponse() {
    fun getaCase(): Case? {
        return aCase
    }

    fun setaCase(aCase: Case?) {
        this.aCase = aCase
    }

    @SerializedName("data")
    @Expose
    private var aCase: Case? = null

    inner class Case {
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

    inner class Datum : Serializable {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @JvmField
        @SerializedName("case_id")
        @Expose
        var caseId: String? = null

        @SerializedName("gate_pass")
        @Expose
        var gatePass: String? = null

        @JvmField
        @SerializedName("driver_phone")
        @Expose
        var drivePhone:String?=null

        @JvmField
        @SerializedName("in_out")
        @Expose
        var inOut: String? = null

        @SerializedName("customer_uid")
        @Expose
        var customerUid: String? = null

        @SerializedName("location")
        @Expose
        var location: String? = null

        @SerializedName("commodity_id")
        @Expose
        var commodityId: String? = null

        @SerializedName("terminal_id")
        @Expose
        var terminalId: String? = null

        @SerializedName("total_weight")
        @Expose
        var totalWeight: String? = null

        @JvmField
        @SerializedName("vehicle_no")
        @Expose
        var vehicleNo: String? = null

        @SerializedName("lead_gen_uid")
        @Expose
        var leadGenUid: String? = null

        @SerializedName("lead_conv_uid")
        @Expose
        var leadConvUid: String? = null

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

        @JvmField
        @SerializedName("send_to_lab")
        @Expose
        var sendToLab: String? = null

        @JvmField
        @SerializedName("stack_number")
        @Expose
        var stack_number: String? = null

        @JvmField
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
        var status: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @JvmField
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

        @SerializedName("lead_gen_fname")
        @Expose
        var leadGenFname: String? = null

        @SerializedName("lead_gen_lname")
        @Expose
        var leadGenLname: String? = null

        @SerializedName("lead_conv_fname")
        @Expose
        var leadConvFname: String? = null

        @SerializedName("lead_conv_lname")
        @Expose
        var leadConvLname: String? = null

        @SerializedName("cate_name")
        @Expose
        var cateName: String? = null

        @SerializedName("commodity_type")
        @Expose
        var commodityType: String? = null

        @SerializedName("warehouse_code")
        @Expose
        var warehouseCode: String? = null

        @SerializedName("terminal_name")
        @Expose
        var terminalName: String? = null

        @JvmField
        @SerializedName("truckbook")
        @Expose
        var truckbook: String? = null

        @JvmField
        @SerializedName("truckbook_date")
        @Expose
        var truckbookDate: String? = null


        @JvmField
        @SerializedName("labourbook")
        @Expose
        var labourBook: String? = null

        @JvmField
        @SerializedName("labourbook_date")
        @Expose
        var labourBookDate: String? = null

        @JvmField
        @SerializedName("first_kanta_parchi")
        @Expose
        var firstKantaParchi: String? = null

        @JvmField
        @SerializedName("first_kanta_parchi_date")
        @Expose
        var firstKantaParchiDate: String? = null

        @JvmField
        @SerializedName("first_quality")
        @Expose
        var firstQuality: String? = null

        @JvmField
        @SerializedName("first_quality_date")
        @Expose
        var firstQualityDate: String? = null

        @JvmField
        @SerializedName("f_q_tagging")
        @Expose
        var firstQualityTagging: String? = null

        @JvmField
        @SerializedName("f_q_tagging_date")
        @Expose
        var firstQualityTaggingDate: String? = null

        @JvmField
        @SerializedName("s_k_parchi")
        @Expose
        var secondKantaParchi: String? = null

        @JvmField
        @SerializedName("s_k_parchi_date")
        @Expose
        var secondKantaParchiDate: String? = null

        @JvmField
        @SerializedName("s_quality_report")
        @Expose
        var secondQualityReport: String? = null

        @JvmField
        @SerializedName("s_quality_date")
        @Expose
        var secondQualityReportDate: String? = null

        @JvmField
        @SerializedName("cctv_report")
        @Expose
        var cctvReport: String? = null
        @JvmField
        @SerializedName("cctv_date")
        @Expose
        var cctvReportDate: String? = null

        @JvmField
        @SerializedName("ivr_report")
        @Expose
        var ivrReport: String? = null
        @JvmField
        @SerializedName("ivr_date")
        @Expose
        var ivrReportDate: String? = null
        @JvmField
        @SerializedName("gatepass_report")
        @Expose
        var gatepassReport: String? = null

        @JvmField
        @SerializedName("first_kanta_file3")
        @Expose
        var firstKantaFile3: String? = null

        @JvmField
        @SerializedName("s_k_file3")
        @Expose
        var secondKantaFile3: String? = null

        @JvmField
        @SerializedName("first_kanta_dhramkanta")
        @Expose
        var firstKantaDharamkanta: String? = null

        @JvmField
        @SerializedName("s_k_p_avg_weight")
        @Expose
        var avgWeight: String? = null

        @JvmField
        @SerializedName("s_k_dhramkanta")
        @Expose
        var secondKantaDharamkanta: String? = null
    }
}
