package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.AllCaseIDResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CaseIdRepo @Inject constructor(private val apiService: ApiService) : BaseApiResponse() {

    suspend fun getCaseId(
            str: String?,
            i: Int,
            str2: String?,
            str3: String?
    ):Flow<NetworkResult<AllCaseIDResponse>>{
        return flow {
            emit(safeApiCall{apiService.getAllCase(str,i,str2,str3)})

        }.flowOn(Dispatchers.IO)
    }


}