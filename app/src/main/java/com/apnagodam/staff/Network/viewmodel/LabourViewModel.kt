package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData
import com.apnagodam.staff.Network.Response.LabourContractorNameResponse
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.LabourRepo
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllLabourBookListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabourViewModel @Inject constructor(val labourRepo: LabourRepo, application: Application) :
    AndroidViewModel(application) {

    var labourResponse = MutableLiveData<NetworkResult<AllLabourBookListResponse>>()
    var labourDetailsUploadResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    var labourContractorNameResponse =
        MutableLiveData<NetworkResult<LabourContractorNameResponse>>()

    fun getLabourList(limit: String, page: String, inOut: String, search: String) =
        viewModelScope.launch {
            labourRepo.getLabourList(limit, page, inOut, search).collect() {
                if (it.data!!.status != "1") {
                    SharedPreferencesRepository.logout()
                    val intent = Intent(getApplication(), LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ContextCompat.startActivity(getApplication(), intent, null)
                }
                labourResponse.value = it
            }
        }

    fun uploadLabourDetails(uploadLabourDetailsPostData: UploadLabourDetailsPostData) =
        viewModelScope.launch {
            labourRepo.uploadLabourDetails(uploadLabourDetailsPostData).collect() {
                labourDetailsUploadResponse.value = it
            }
        }

    fun getLabourContractorName(warehouseId: String,commodityId:String) = viewModelScope.launch {
        labourRepo.getLabourContractorName(warehouseId,commodityId).collect {
            labourContractorNameResponse.value = it
        }
    }

}