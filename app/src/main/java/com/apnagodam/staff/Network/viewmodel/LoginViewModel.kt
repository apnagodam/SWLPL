package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.LoginPostData
import com.apnagodam.staff.Network.Request.OTPData
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.OTPvarifedResponse
import com.apnagodam.staff.Network.repository.LoginRepo
import com.apnagodam.staff.module.AllCaseIDResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepo,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<LoginResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<LoginResponse>> = _response


    private val _Otpresponse: MutableLiveData<NetworkResult<OTPvarifedResponse>> = MutableLiveData()
    val Otpresponse: LiveData<NetworkResult<OTPvarifedResponse>> = _Otpresponse


    val uploadDeliveryResponse = MutableLiveData<NetworkResult<LoginResponse>>()

    val logoutResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    fun doLogin(loginPostData: LoginPostData) = viewModelScope.launch {
        repository.doLogin(loginPostData).collect { values ->
            _response.value = values
        }
    }

    fun dpVerifyOtp(loginPostData: OTPData) = viewModelScope.launch {
        repository.verifyOtp(loginPostData).collect { values ->
            _Otpresponse.value = values
        }
    }

    fun uploadDeliveredOrder(uploadReleaseOrderlsPostData: UploadReleaseOrderlsPostData) =
        viewModelScope.launch {
            repository.uploadDeliveredOrder(uploadReleaseOrderlsPostData).collect() {
                uploadDeliveryResponse.value = it
            }
        }

    fun doLogout() = viewModelScope.launch {
        repository.doLogout().collect() {
            logoutResponse.value = it
        }
    }
}