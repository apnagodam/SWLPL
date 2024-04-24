package com.apnagodam.staff.Network

data class PvUploadRequestModel(var listOfUpdateModel: ArrayList<PvUpdateModel>) {

    var getListOfUpdateModel: ArrayList<PvUpdateModel>
        get() = listOfUpdateModel
        set(value) {
            listOfUpdateModel = value
        }

}