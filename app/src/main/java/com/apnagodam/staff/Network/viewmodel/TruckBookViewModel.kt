package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.net.Network
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadTruckDetailsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.CaseIdRepo
import com.apnagodam.staff.Network.repository.TuckBookRepo
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.module.AllTruckBookListResponse
import com.apnagodam.staff.module.TransporterDetailsPojo
import com.apnagodam.staff.module.TransporterListPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TruckBookViewModel @Inject constructor(private val repository: TuckBookRepo,
                                             application: Application) :AndroidViewModel(application){
    private val _response: MutableLiveData<NetworkResult<AllTruckBookListResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<AllTruckBookListResponse>> get() = _response
    var transporterResponse = MutableLiveData<NetworkResult<TransporterListPojo>>()
    var transporterDetailsResponse = MutableLiveData<NetworkResult<TransporterDetailsPojo>>()
    val uploadTruckResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    fun getTruckBookList(str: String, i: Int, str2: String, str3: String)=viewModelScope.launch {
        repository.getTruckList(str,i,str2,str3).collect { values ->
            _response.value = values
        }
    }
    fun transporterList()= viewModelScope.launch {
        repository.transporterList().collect(){
            transporterResponse.value = it
        }
    }

    fun getTransporterDetails(transporterId :String)= viewModelScope.launch {
        repository.getTransporterDetails(transporterId).collect(){
            transporterDetailsResponse.value = it
        }
    }
    fun uploadTruckDetails(uploadTruckDetailsPostData: UploadTruckDetailsPostData) = viewModelScope.launch {
        repository.uploadTruckDetails(uploadTruckDetailsPostData).collect(){
            uploadTruckResponse.value = it
        }
    }
}