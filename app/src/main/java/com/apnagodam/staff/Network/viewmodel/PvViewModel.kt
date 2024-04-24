package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.PvUpdateModel
import com.apnagodam.staff.Network.PvUploadRequestModel
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.PvResponseModel
import com.apnagodam.staff.Network.repository.PVRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PvViewModel @Inject constructor(private val pvRepo: PVRepo, application: Application):AndroidViewModel(application){

    var pvTerminalResponse = MutableLiveData<NetworkResult<PvResponseModel>>()
    var postPvResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    fun getPvTerminal(type: String?="Terminal",
                      terminalId: Int?=null) = viewModelScope.launch {
        pvRepo.getPvTerminal(type, terminalId).collect(){
            pvTerminalResponse.value = it
        }
    }

    fun postPvData(pvUpdateModel: PvRequestModel, terminalId: Int, stackId:String) = viewModelScope.launch {
        pvRepo.postPv(pvUpdateModel,terminalId,stackId).collect(){
            postPvResponse.value = it
        }
    }

}