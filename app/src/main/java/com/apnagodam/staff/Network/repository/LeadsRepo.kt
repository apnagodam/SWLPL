package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.TerminalListPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LeadsRepo @Inject constructor(private val apiService: ApiService):BaseApiResponse() {


    fun getTerminalList(): Flow<NetworkResult<TerminalListPojo>> {
        return flow{
            emit(safeApiCall { apiService.terminalListLevel() })
        }.flowOn(Dispatchers.IO)
    }
}