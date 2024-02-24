package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadReleaseOrderlsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.OrdersRepo
import com.apnagodam.staff.module.ReleaseOrderPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(val ordersRepo: OrdersRepo,application: Application):AndroidViewModel(application) {

    val ordersResponse = MutableLiveData<NetworkResult<ReleaseOrderPojo>>()
    val uploadReleaseOrdersResponse =MutableLiveData<NetworkResult<LoginResponse>>()
    fun getOrdersList(limit: String, page: Int, inOut: String, search: String)=viewModelScope.launch {
        ordersRepo.getDeliverOrders(limit, page, inOut, search).collect(){
            ordersResponse.value = it
        }
    }
    fun uploadReleaseOrders(uploadReleaseOrderlsPostData: UploadReleaseOrderlsPostData)= viewModelScope.launch {
        ordersRepo.uploadReleaseOrder(uploadReleaseOrderlsPostData).collect(){
            uploadReleaseOrdersResponse.value = it
        }
    }
}