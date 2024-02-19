package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.AllLeadsResponse
import com.apnagodam.staff.module.TerminalListPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class LeadsRepo @Inject constructor(private val apiService: ApiService):BaseApiResponse() {


   suspend fun getTerminalList(): Flow<NetworkResult<TerminalListPojo>> {
        return flow{
            emit(safeApiCall { apiService.terminalListLevel() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllLeads(limit:String,page:Int,inOut:String,search:String):Flow<NetworkResult<AllLeadsResponse>>{
        return  flow { emit(safeApiCall { apiService.getAllLeads(
                limit,page,inOut,search
        ) }) }.flowOn(Dispatchers.IO)
    }
}