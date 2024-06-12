package com.apnagodam.staff.Network.Request

data class DispleasedRequestModel(
    var user: String,
    var terminal_id: String,
    var commodity_id: String,
    var stack_id: String,
    var quantity: String,
    var Bags: String,
    var displedge_image: String,
    var approved_by: String,
    var emp_displege_notes: String,
)
