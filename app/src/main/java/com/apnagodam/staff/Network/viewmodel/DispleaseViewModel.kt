package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.DispleasedRequestModel
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.DispleasedListResponse
import com.apnagodam.staff.Network.Response.PleasedApproverResponse
import com.apnagodam.staff.Network.Response.PleasedCommodityResponse
import com.apnagodam.staff.Network.Response.PleasedStackResponse
import com.apnagodam.staff.Network.Response.PleasedUsersResponse
import com.apnagodam.staff.Network.repository.DispleasedRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DispleaseViewModel @Inject constructor(
    private val repo: DispleasedRepo, application: Application
) : AndroidViewModel(application) {
    val pleasedUsersResponse = MutableLiveData<NetworkResult<PleasedUsersResponse>>()
    val pleasedCommodityResponse = MutableLiveData<NetworkResult<PleasedCommodityResponse>>()
    val pleasedStackResponse = MutableLiveData<NetworkResult<PleasedStackResponse>>()
    val pleasedApproverResponse = MutableLiveData<NetworkResult<PleasedApproverResponse>>()
    val postDispleasedResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    val displeasedListingResponse = MutableLiveData<NetworkResult<DispleasedListResponse>>()
    fun getPleasedUser(terminalId: String) = viewModelScope.launch {
        repo.getPleasedUsers(terminalId).collect {
            pleasedUsersResponse.value = it
        }
    }

    fun getPleasedCommodity(terminalId: String, userId: String) = viewModelScope.launch {
        repo.getPleasedCommodity(terminalId, userId).collect {
            pleasedCommodityResponse.value = it
        }
    }

    fun getPleaseStacks(terminalId: String, userId: String, commodityId: String) =
        viewModelScope.launch {
            repo.getPleasedStacks(terminalId, userId, commodityId).collect {
                pleasedStackResponse.value = it
            }
        }


    fun getPleasedApprovar() = viewModelScope.launch {
        repo.getPleasedApprovar().collect {
            pleasedApproverResponse.value = it
        }
    }

    fun postDispleasedRequest(
        displeasedRequestModel: DispleasedRequestModel
    ) = viewModelScope.launch {
        repo.postDispleasedRequest(
            displeasedRequestModel
        ).collect {
            postDispleasedResponse.value = it
        }
    }

    fun getDispleasedList() = viewModelScope.launch {
        repo.getDispleasedList().collect {
            displeasedListingResponse.value = it
        }
    }
}