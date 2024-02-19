package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.AllTruckBookListResponse
import com.apnagodam.staff.module.TransporterDetailsPojo
import com.apnagodam.staff.module.TransporterListPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TuckBookRepo @Inject constructor(var apiService: ApiService):BaseApiResponse() {

    suspend fun getTruckList(str:String,i:Int,str2:String,str3:String):Flow<NetworkResult<AllTruckBookListResponse>>{
        return  flow {
            emit(safeApiCall {
                apiService.getTruckBookList(str,i,str2,str3)
            })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun transporterList():Flow<NetworkResult<TransporterListPojo>>{
        return  flow {
            emit(safeApiCall {
                apiService.transporterList()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTransporterDetails(transport_id:String):Flow<NetworkResult<TransporterDetailsPojo>>{
        return flow {
            emit(safeApiCall { apiService.getTransporterDetails(transport_id) })
        }.flowOn(Dispatchers.IO)
    }
}