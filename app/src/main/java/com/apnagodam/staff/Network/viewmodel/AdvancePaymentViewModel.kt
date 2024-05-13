package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.AdvancePaymentListModel
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.repository.AdvancePaymentRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvancePaymentViewModel @Inject constructor(
    private val repo: AdvancePaymentRepo,
    application: Application
) : AndroidViewModel(application) {
    val postAdvancesResponse = MutableLiveData<NetworkResult<BaseResponse>>()
    val advanceslistResponse = MutableLiveData<NetworkResult<AdvancePaymentListModel>>()


    fun postAdvanceRequest(requestedAmount: String, notes: String) = viewModelScope.launch {
        repo.postAdvanceRequest(requestedAmount, notes).collect {
            postAdvancesResponse.value = it
        }
    }

    fun getAdvancesList() = viewModelScope.launch {
        repo
            .getAdvancesList().collect {
                advanceslistResponse.value = it
            }
    }
}