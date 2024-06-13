package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.AdvancePaymentListModel
import com.apnagodam.staff.Network.Response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AdvancePaymentRepo @Inject constructor(private val apiService: ApiService) :BaseApiResponse() {

    suspend fun postAdvanceRequest(
        requestedAmount:String,notes:String
    ) : Flow<NetworkResult<BaseResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.postAdvanceRequest(requestedAmount, notes)
            })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getAdvancesList():Flow<NetworkResult<AdvancePaymentListModel>>{
        return flow {
            emit(safeApiCall {
                apiService.getAdvancesList()
            })
        }.flowOn(Dispatchers.IO)
    }
}