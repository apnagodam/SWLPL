package com.apnagodam.staff.Network.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PleasedCommodityResponse {
    @SerializedName("data")
    @Expose
    var data: ArrayList<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("case_id")
        @Expose
        var caseId: Any? = null

        @SerializedName("warehouse_id")
        @Expose
        var warehouseId: Int? = null

        @SerializedName("commodity")
        @Expose
        var commodity: Int? = null

        @SerializedName("weight_bridge_no")
        @Expose
        var weightBridgeNo: Any? = null

        @SerializedName("truck_no")
        @Expose
        var truckNo: String? = null

        @SerializedName("stack_no")
        @Expose
        var stackNo: String? = null

        @SerializedName("lot_no")
        @Expose
        var lotNo: Any? = null

        @SerializedName("net_weight")
        @Expose
        var netWeight: Any? = null

        @SerializedName("type")
        @Expose
        var type: Any? = null

        @SerializedName("quantity")
        @Expose
        var quantity: String? = null

        @SerializedName("bags")
        @Expose
        var bags: Int? = null

        @SerializedName("sell_quantity")
        @Expose
        var sellQuantity: Any? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("gate_pass_wr")
        @Expose
        var gatePassWr: String? = null

        @SerializedName("quality_category")
        @Expose
        var qualityCategory: Any? = null

        @SerializedName("origin")
        @Expose
        var origin: Any? = null

        @SerializedName("image")
        @Expose
        var image: Any? = null

        @SerializedName("file")
        @Expose
        var file: Any? = null

        @SerializedName("rent_row_id")
        @Expose
        var rentRowId: Any? = null

        @SerializedName("interest_row_id")
        @Expose
        var interestRowId: Any? = null

        @SerializedName("mandi_amount")
        @Expose
        var mandiAmount: Any? = null

        @SerializedName("settlment_amount")
        @Expose
        var settlmentAmount: Any? = null

        @SerializedName("rem_amount")
        @Expose
        var remAmount: Any? = null

        @SerializedName("less_old_neg_wallet_sattlement")
        @Expose
        var lessOldNegWalletSattlement: Int? = null

        @SerializedName("walletSatlementAmount")
        @Expose
        var walletSatlementAmount: Any? = null

        @SerializedName("is_liquidation")
        @Expose
        var isLiquidation: Int? = null

        @SerializedName("rent_rate")
        @Expose
        var rentRate: Int? = null

        @SerializedName("sales_status")
        @Expose
        var salesStatus: Int? = null

        @SerializedName("lien_beg")
        @Expose
        var lienBeg: Int? = null

        @SerializedName("unlien_beg")
        @Expose
        var unlienBeg: Int? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("category")
        @Expose
        var category: String? = null

        @SerializedName("category_hi")
        @Expose
        var categoryHi: String? = null
    }
}
