package com.apnagodam.staff.Network

data class PvUpdateModel(
    var block_no: String,
    var danda: String,
    var dhang: String,
    var height: String,
    var plusMinus: String,
) {
    var getBlockNo: String
        get() = block_no
        set(value) {
            block_no = value;
        }
    var getDanda: String
        get() = danda
        set(value) {
            danda = value
        }
    var getDhang: String
        get() = dhang
        set(value) {
            dhang = value
        }

    var getHeight: String
        get() = height
        set(value) {
            height = value
        }
    var getPlusMinus: String
        get() = plusMinus
        set(value) {
            plusMinus = value
        }


}