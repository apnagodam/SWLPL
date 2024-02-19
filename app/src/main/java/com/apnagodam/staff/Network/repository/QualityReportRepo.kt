package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.QualityParamsResponse
import com.apnagodam.staff.module.FirstQuilityReportListResponse
import com.apnagodam.staff.module.SecoundQuilityReportListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class QualityReportRepo @Inject constructor(val apiService: ApiService) : BaseApiResponse() {

    suspend fun getQualityReportList(
        limit: String,
        page: String,
        inOut: String,
        search: String
    ): Flow<NetworkResult<FirstQuilityReportListResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getf_qualityReportsList(
                    limit,
                    page,
                    inOut,
                    search
                )
            })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun uploadFirstQualityReport(uploadFirstQualityPostData: UploadFirstQualityPostData): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.uploadFirstQualityReports(
                    uploadFirstQualityPostData
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCommodityParams(case_id: String): Flow<NetworkResult<QualityParamsResponse>> {
        return flow { emit(safeApiCall { apiService.getCommodityParams(case_id) }) }.flowOn(Dispatchers.IO)
    }

    suspend fun getSQualityReportList(
        limit: String,
        page: String,
        inOut: String,
        search: String
    ): Flow<NetworkResult<SecoundQuilityReportListResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getS_qualityReportsList(
                    limit,
                    page,
                    inOut,
                    search
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadSecondQualityReport(uploadSecoundQualityPostData: UploadSecoundQualityPostData): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.uploadSecoundQualityReports(
                    uploadSecoundQualityPostData
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}