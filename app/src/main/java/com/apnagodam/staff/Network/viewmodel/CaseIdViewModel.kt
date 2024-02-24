package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData
import com.apnagodam.staff.Network.Request.StackPostData
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData
import com.apnagodam.staff.Network.Response.DriverOtpResponse
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.CaseIdRepo
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.module.AllUserListPojo
import com.apnagodam.staff.module.CommodityResponseData
import com.apnagodam.staff.module.StackListPojo
import com.apnagodam.staff.module.TerminalListPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class CaseIdViewModel @Inject constructor(
    private val repository: CaseIdRepo,
    application: Application
) : AndroidViewModel(application) {
    private val _response: MutableLiveData<NetworkResult<AllCaseIDResponse>> = MutableLiveData()
    private val terminalResponse: MutableLiveData<NetworkResult<TerminalListPojo>> =
        MutableLiveData()

     val driverOtpResponse = MutableLiveData<NetworkResult<DriverOtpResponse>>()
    val response: LiveData<NetworkResult<AllCaseIDResponse>> = _response

    val stackReponse = MutableLiveData<NetworkResult<StackListPojo>>()

    val commoditiesResponse= MutableLiveData<NetworkResult<CommodityResponseData>>()

    val createCaseIdResponse =MutableLiveData<NetworkResult<LoginResponse>>()
    val usersListResponse = MutableLiveData<NetworkResult<AllUserListPojo>>()
    fun getCaseId(str: String?, i: Int, str2: String?, str3: String?) = viewModelScope.launch {
        repository.getCaseId(str, i, str2, str3).collect { values ->

            _response.value = values
        }
    }

    fun getTerminalList() = viewModelScope.launch {
        repository.getTerminalList().collect() {
            terminalResponse.value = it;
        }

    }

    fun driverOtp(phone: String, stackId: String, inOut: String, otp: String = "") =
        viewModelScope.launch {
            repository.driverOtp(phone, stackId, inOut, otp).collect() {
                driverOtpResponse.value = it
            }
        }

    fun getStackList(stackPostData: StackPostData) = viewModelScope.launch {
        repository.getStackList(stackPostData).collect() {
            stackReponse.value = it
        }
    }

    fun getCommodities(terminalId:String,inOut:String,userPhone:String) = viewModelScope.launch {
        repository.getCommodities(terminalId, inOut, userPhone).collect(){
            commoditiesResponse.value = it
        }
    }
     fun doCreateCaseId( createCaseIDPostData: CreateCaseIDPostData)= viewModelScope.launch {
         repository.doCreateCaseId(createCaseIDPostData).collect(){
             createCaseIdResponse.value = it
         }
     }

    fun getUsersList(terminalId: String,inOut: String) = viewModelScope.launch {
        repository.getUserList(terminalId, inOut).collect(){
            usersListResponse.value = it
        }
    }

}