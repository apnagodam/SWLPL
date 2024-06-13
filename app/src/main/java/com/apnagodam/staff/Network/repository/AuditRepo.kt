package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.PvRequestModel
import com.apnagodam.staff.Network.Request.AddNeighbourRequest
import com.apnagodam.staff.Network.Request.AuditQVRequest
import com.apnagodam.staff.Network.Response.AuditQVResponse
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.CmDetailsResponse
import com.apnagodam.staff.Network.Response.StacksListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.http.Query
import javax.inject.Inject


class AuditRepo @Inject constructor(private val apiService: ApiService) : BaseApiResponse() {
    suspend fun getStackList(terminal_id: String): Flow<NetworkResult<StacksListResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getListOfStacks(terminal_id)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postAuditPv(pvRequestModel: PvRequestModel): Flow<NetworkResult<BaseResponse>> {

        return flow<NetworkResult<BaseResponse>> {
            emit(safeApiCall {
                apiService.postAuditPv(pvRequestModel)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postAuditVideo(
        file: MultipartBody
    ): Flow<NetworkResult<BaseResponse>> {
        return flow<NetworkResult<BaseResponse>> {
            emit(safeApiCall {
                apiService.postAuditVideo(file)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAuditQvData(commodityId: String): Flow<NetworkResult<AuditQVResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getAuditQvParameters(commodityId)
            })
        }.flowOn(Dispatchers.IO)

    }

    suspend fun postAuditQv(

        commodityList: AuditQVRequest
    ): Flow<NetworkResult<BaseResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.postAuditQv(commodityList)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postAuditCM(
        terminalId: String,
        agencyId: String,
        guardName: String,
        guardPhone: String,
        notes: String,
        cmName: String,
        cmPhone: String,
    ): Flow<NetworkResult<BaseResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.postAuditCM(
                    terminalId,
                    agencyId,
                    guardName,
                    guardPhone,
                    notes,
                    cmName,
                    cmPhone,
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postNeighbourRequest(neighbourRequest: AddNeighbourRequest): Flow<NetworkResult<BaseResponse>> {

        return flow {
            emit(safeApiCall { apiService.postAuditNeighbour(neighbourRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCmData(): Flow<NetworkResult<CmDetailsResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getCmData()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postAuditInOut(
        lat: String,
        long: String,
        terminalId: String,
        inOutType: String,
        notes: String
    ) =
        flow {
            emit(safeApiCall {
                apiService.postAuditInOut(lat, long, terminalId, inOutType, notes)
            })
        }.flowOn(Dispatchers.IO)
}