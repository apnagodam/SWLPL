package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.PvUpdateModel
import com.apnagodam.staff.Network.PvUploadRequestModel
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.PvResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PVRepo @Inject constructor(private val apiService: ApiService) : BaseApiResponse() {

    suspend fun getPvTerminal(
        type: String? = "Terminal",
        terminalId: Int? = null
    ): Flow<NetworkResult<PvResponseModel>> {

        return flow {
            emit(safeApiCall {
                apiService.getPv(type, terminalId)
            })
        }.flowOn(Dispatchers.IO)

    }

    suspend fun postPv(pvUpdateMode: PvRequestModel): Flow<NetworkResult<BaseResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.postPV(pvUpdateMode)
            })
        }.flowOn(Dispatchers.IO)
    }

}