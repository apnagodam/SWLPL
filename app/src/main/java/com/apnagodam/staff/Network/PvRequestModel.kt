package com.apnagodam.staff.Network

data class PvRequestModel(
    var terminal_id: String,
    var stack_no: Float,
    var commodity_id: Int?=null,
    var block_no: List<BlockNo>
) {


    data class BlockNo(
        var block_no: String,
        var danda: String,
        var dhang: String,
        var height: String,
        var plusMinus: String,
        var total: String,
        var no_of_blocks: String? = null
    ) {
    }
}