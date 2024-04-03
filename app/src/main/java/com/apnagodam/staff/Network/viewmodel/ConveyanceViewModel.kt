package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.net.Network
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateConveyancePostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.ConveyanceRepo
import com.apnagodam.staff.module.AllConvancyList
import com.apnagodam.staff.module.AllLevelEmpListPojo
import com.apnagodam.staff.module.AllVendorConvancyList
import com.apnagodam.staff.module.VendorExpensionApprovedListPojo
import com.apnagodam.staff.module.VendorNamePojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConveyanceViewModel @Inject constructor(
    val conveyanceRepo: ConveyanceRepo,
    application: Application
) : AndroidViewModel(application) {
    val conveyanceResponse = MutableLiveData<NetworkResult<AllConvancyList>>()
    val conveyanceListResponse = MutableLiveData<NetworkResult<AllLevelEmpListPojo>>()

    val conveyanceUploadRespomse = MutableLiveData<NetworkResult<LoginResponse>>()

    val vendorUserListResponse = MutableLiveData<NetworkResult<VendorNamePojo>>()
    val vendorConveyanceListResponse = MutableLiveData<NetworkResult<AllVendorConvancyList>>()
    var  expensionApprovedList= MutableLiveData<NetworkResult<VendorExpensionApprovedListPojo>>()
    fun getConveyanceList(limit: String, page: Int, search: String) = viewModelScope.launch {
        conveyanceRepo.getConvancyList(limit, page, search).collect {
            conveyanceResponse.value = it
        }
    }

    fun getlevelwiselist() = viewModelScope.launch {
        conveyanceRepo.getlevelwiselist().collect {
            conveyanceListResponse.value = it
        }
    }

    fun getVendorUserList() = viewModelScope.launch {
        conveyanceRepo.getVendorUserList().collect {
            vendorUserListResponse.value = it
        }
    }

     fun getVendorConvancyList(
        limit: String,
        page: Int,
        search: String
    )=viewModelScope.launch {
        conveyanceRepo.getVendorConvancyList(limit, page, search).collect{
            vendorConveyanceListResponse.value = it
        }
     }

    fun uploadConveyanceVoucher(createConveyancePostData: CreateConveyancePostData) = viewModelScope.launch {
        conveyanceRepo.createConveyance(createConveyancePostData).collect{
            conveyanceUploadRespomse.value = it
        }
    }
    fun expensionApprovedList(expId:String,chargeId:String)=viewModelScope.launch {
        conveyanceRepo.expensionApprovedList(expId,chargeId).collect{
            expensionApprovedList.value = it
        }
    }
}