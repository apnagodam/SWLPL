package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData
import com.apnagodam.staff.Network.Response.DharamKanta
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class KantaParchiRepo @Inject constructor(val apiService: ApiService) : BaseApiResponse() {

    suspend fun getKantaParchiListing(
        limit: String,
        page: String,
        inOut: String,
        search: String
    ): Flow<NetworkResult<FirstkanthaParchiListResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getf_kanthaParchiList(limit, page, inOut, search)
            })
        }
            .flowOn(Dispatchers.IO)
    }

    suspend fun uploadFirstKantaParchi(uploadFirstQualityPostData: UploadFirstkantaParchiPostData): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.uploadFirstkantaParchi(uploadFirstQualityPostData)
            })
        }
    }

    suspend fun getSecondKantaParchiList(
        limit: String,
        page: String,
        inOut: String,
        search: String
    ): Flow<NetworkResult<SecoundkanthaParchiListResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getS_kanthaParchiList(limit, page, inOut, search)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadSecondKantaParchi(uploadFirstQualityPostData: UploadSecoundkantaParchiPostData): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.uploadSecoundkantaParchi(uploadFirstQualityPostData)
            })
        }
    }

    suspend fun getDharamKantaDetails(caseId: String): Flow<NetworkResult<DharamKanta>> {
        return flow {
            emit(safeApiCall {
                apiService.getDharamKanta(caseId)
            })
        }.flowOn(Dispatchers.IO)
    }
}