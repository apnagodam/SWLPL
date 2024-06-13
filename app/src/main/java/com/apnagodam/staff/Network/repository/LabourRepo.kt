package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadLabourDetailsPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.module.AllLabourBookListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class LabourRepo @Inject constructor(val apiService: ApiService) : BaseApiResponse() {

    suspend fun getLabourList(
        limit: String,
        page: String,
        inOut: String,
        search: String
    ): Flow<NetworkResult<AllLabourBookListResponse>> {
        return flow {
            emit(safeApiCall { apiService.getLabourBookList(limit, page, inOut, search) })
        }
            .flowOn(Dispatchers.IO)
    }

    suspend fun uploadLabourDetails(uploadLabourDetailsPostData: UploadLabourDetailsPostData): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { apiService.uploadLabourDetails(uploadLabourDetailsPostData) })
        }.flowOn(Dispatchers.IO)

    }
}