package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.Request.DispleasedRequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DispleasedRepo @Inject constructor(private val apiService: ApiService) : BaseApiResponse() {

    suspend fun getPleasedUsers(terminalId: String) =
        flow {
            emit(safeApiCall { apiService.getPleasedUsers(terminalId) })

        }.flowOn(Dispatchers.IO)

    suspend fun getPleasedCommodity(terminalId: String, userId: String) = flow {
        emit(safeApiCall { apiService.getPleasedCommodity(terminalId, userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun getPleasedStacks(terminalId: String, userId: String, commodityId: String) = flow {
        emit(safeApiCall { apiService.getPleasedStacks(terminalId, userId, commodityId) })
    }.flowOn(Dispatchers.IO)


    suspend fun getPleasedApprovar() = flow {
        emit(safeApiCall { apiService.getPleasedApprovar() })
    }.flowOn(Dispatchers.IO)


    suspend fun postDispleasedRequest(
        displeasedRequestModel: DispleasedRequestModel
    ) = flow {
        emit(safeApiCall {
            apiService.postDisplegedRequest(
              displeasedRequestModel
            )
        })
    }.flowOn(Dispatchers.IO)
}