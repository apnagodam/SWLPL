package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.ReleaseOrderPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class OrdersRepo @Inject constructor(val apiService: ApiService) : BaseApiResponse() {

    suspend fun getDeliverOrders(limit: String, page: Int, inOut: String, search: String): Flow<NetworkResult<ReleaseOrderPojo>> {
        return flow {
            emit(safeApiCall { apiService.getDeliveredOrderList(limit, page, inOut, search) })
        }.flowOn(Dispatchers.IO)
    }
}