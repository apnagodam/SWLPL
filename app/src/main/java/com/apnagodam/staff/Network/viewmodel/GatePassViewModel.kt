package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.OTPVerifyGatePassData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.GatePassRepo
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.GatePassListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GatePassViewModel @Inject constructor(val gatePassRepo: GatePassRepo,application: Application):AndroidViewModel(application) {

    var gatePassList = MutableLiveData<NetworkResult<GatePassListResponse>>()
    var gatePassOtpResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    fun getGatePassList(limit:String,page:String,inOut:String,search:String)=viewModelScope.launch {
        gatePassRepo.getGatePassListing(limit, page, inOut, search).collect(){
            if(it.data!!.status!="1"){
                SharedPreferencesRepository.logout()
                val intent = Intent(getApplication(), LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(getApplication(), intent, null)
            }
            gatePassList.value = it
        }
    }

    fun verifyGatePassOtp(otpVerifyGatePassData: OTPVerifyGatePassData)=viewModelScope.launch {
        gatePassRepo.doVerifyOtp(otpVerifyGatePassData).collect(){
            gatePassOtpResponse.value = it
        }
    }
}