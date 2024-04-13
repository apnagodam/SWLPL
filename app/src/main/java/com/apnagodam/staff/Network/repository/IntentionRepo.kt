package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.AllIntantionList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntentionRepo @Inject constructor(val apiService: ApiService):BaseApiResponse() {

    suspend fun getintentionList(limit:String,page:Int,search:String):Flow<NetworkResult<AllIntantionList>>{
        return flow {
            emit(safeApiCall {
                apiService.getintentionList(limit,page,search)
            })
        }
    }
}