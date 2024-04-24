package com.apnagodam.staff.Network

data class PvRequestModel(
    var terminal_id: String,
    var stack_no: String,
    var block_no: ArrayList<BlockNo>
) {


    data class BlockNo(
        var block_no: String,
        var danda: String,
        var dhang: String,
        var height: String,
        var plusMinus: String
    ) {
    }
}