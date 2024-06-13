package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.Request.AddNeighbourRequest
import com.apnagodam.staff.Network.Request.AuditQVRequest
import com.apnagodam.staff.Network.Response.AuditQVResponse
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.CmDetailsResponse
import com.apnagodam.staff.Network.Response.StacksListResponse
import com.apnagodam.staff.Network.repository.AuditRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AuditViewModel @Inject constructor(
    private val auditRepo: AuditRepo, application: Application
) : AndroidViewModel(application) {
    val getStackListResponse = MutableLiveData<NetworkResult<StacksListResponse>>()
    val postPvAuditResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    val postVideoResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    val auditQVDataResponse = MutableLiveData<NetworkResult<AuditQVResponse>>()
    val postAuditQvResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    val postAuditCMResponse = MutableLiveData<NetworkResult<BaseResponse>>()

    val postAuditNeighborResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    val getCmDataResponse = MutableLiveData<NetworkResult<CmDetailsResponse>>()

    val postAuditInOutResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    fun getStackList(terminal_id: String) = viewModelScope.launch {
        auditRepo.getStackList(terminal_id).collect {
            getStackListResponse.value = it
        }
    }

    fun postPvData(pvRequestModel: PvRequestModel) = viewModelScope.launch {
        auditRepo.postAuditPv(pvRequestModel).collect {
            postPvAuditResponse.value = it
        }

    }

    fun postVideoResponse(file: MultipartBody) = viewModelScope.launch {
        auditRepo.postAuditVideo(file).collect {
            postVideoResponse.value = it
        }
    }


    fun getAuditQvData(commodityId: String) = viewModelScope.launch {
        auditRepo.getAuditQvData(commodityId).collect {
            auditQVDataResponse.value = it
        }
    }

    fun postAuditQV(
        commodityList: AuditQVRequest
    ) = viewModelScope.launch {
        auditRepo.postAuditQv(commodityList).collect {
            postAuditQvResponse.value = it
        }
    }

    fun postAuditCM(
        terminalId: String,
        agencyId: String,
        guardName: String,
        guardPhone: String,
        notes: String,
        cmName: String,
        cmPhone: String,
    ) = viewModelScope.launch {
        auditRepo.postAuditCM(
            terminalId,
            agencyId,
            guardName,
            guardPhone,
            notes,
            cmName,
            cmPhone,
        ).collect {
            postAuditCMResponse.value = it
        }
    }

    fun postAuditNeighbour(addNeighbourRequest: AddNeighbourRequest) = viewModelScope.launch {
        auditRepo.postNeighbourRequest(addNeighbourRequest).collect {
            postAuditNeighborResponse.value = it
        }
    }

    fun getCmData() = viewModelScope.launch {
        auditRepo.getCmData().collect {
            getCmDataResponse.value = it
        }
    }

    fun postAuditInOut(
        lat: String,
        long: String,
        terminalId: String,
        inOutType: String,
        notes: String
    ) = viewModelScope.launch {
        auditRepo.postAuditInOut(lat, long, terminalId, inOutType, notes).collect {
            postAuditInOutResponse.value = it
        }
    }
}