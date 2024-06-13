package com.apnagodam.staff.Network.Request

data class AddNeighbourRequest(
    private var terminal_id: String,
    private var notes: String?=null,
    private var neighbour_array: ArrayList<NeighbourDetails>
) {
    data class NeighbourDetails(private var name: String, private var phone: String)
}
