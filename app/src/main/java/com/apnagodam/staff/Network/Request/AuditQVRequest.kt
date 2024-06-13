package com.apnagodam.staff.Network.Request

data class AuditQVRequest(private var terminal_id: String, private var commodity_id: String,private var stack_no:String,private var commodityList:ArrayList<QVMOdel>) {

    data class QVMOdel(var id:String,var value:String)
}
