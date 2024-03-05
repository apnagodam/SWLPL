package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.module.AllConvancyList
import com.apnagodam.staff.module.AllLevelEmpListPojo
import com.apnagodam.staff.module.AllVendorConvancyList
import com.apnagodam.staff.module.VendorNamePojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ConveyanceRepo @Inject constructor(val apiService: ApiService) : BaseApiResponse() {

    suspend fun getConvancyList(
        limit: String,
        page: Int,
        search: String
    ): Flow<NetworkResult<AllConvancyList>> {

        return flow {
            emit(safeApiCall {
                apiService.getConvancyList(limit, page, search)
            })
        }
            .flowOn(Dispatchers.IO)
    }

    suspend fun getlevelwiselist(): Flow<NetworkResult<AllLevelEmpListPojo>> {
        return flow {
            emit(safeApiCall { apiService.getlevelwiselist() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVendorUserList(): Flow<NetworkResult<VendorNamePojo>> {
        return flow {
            emit(safeApiCall {
                apiService.getvendorUserList()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVendorConvancyList(
        limit: String,
        page: Int,
        search: String
    ): Flow<NetworkResult<AllVendorConvancyList>> {
        return flow {
            emit(safeApiCall {
                apiService.getVendorConvancyList(limit, page, search)
            })
        }.flowOn(Dispatchers.IO)
    }
}