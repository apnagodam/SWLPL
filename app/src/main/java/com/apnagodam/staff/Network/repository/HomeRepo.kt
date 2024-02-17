package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse
import com.apnagodam.staff.module.DashBoardData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class HomeRepo @Inject constructor(val apiService: ApiService):BaseApiResponse() {

    suspend fun  getDashboardData():Flow<NetworkResult<DashBoardData>>{
        return  flow { emit(safeApiCall { apiService.dashboardData() }) }.flowOn(Dispatchers.IO)
    }
   suspend fun getPermission(str: String,
                       str2: String): Flow<NetworkResult<AllUserPermissionsResultListResponse>> {
        return  flow {
            emit(safeApiCall { apiService.getPermission(str,str2) })
        }.flowOn(Dispatchers.IO)
    }

}