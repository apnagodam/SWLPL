package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData
import com.apnagodam.staff.Network.Request.StackPostData
import com.apnagodam.staff.Network.Response.DriverOtpResponse
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.StackRequestResponse
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.module.AllUserListPojo
import com.apnagodam.staff.module.CommodityResponseData
import com.apnagodam.staff.module.StackListPojo
import com.apnagodam.staff.module.TerminalListPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class CaseIdRepo @Inject constructor(private val apiService: ApiService): BaseApiResponse()  {

    suspend fun getCaseId(
        str: String?,
        i: Int,
        str2: String?,
        str3: String?
    ): Flow<NetworkResult<AllCaseIDResponse>> {
        return flow {
            emit(safeApiCall { apiService.getAllCase(str, i, str2, str3) })

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTerminalList(): Flow<NetworkResult<TerminalListPojo>> {
        return flow {
            emit(safeApiCall { apiService.terminalListLevel() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun driverOtp(
        phone: String,
        stackId: String,
        inOut: String,
        otp: String = ""
    ): Flow<NetworkResult<DriverOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.driverOtp(phone, stackId, inOut, otp) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getStackList(stackPostData: StackPostData): Flow<NetworkResult<StackListPojo>> {
        return flow {
            emit(safeApiCall { apiService.getStackList(stackPostData) })
        }
    }

    suspend fun getCommodities(
        terminalId: String,
        inOut: String,
        userPhone: String
    ): Flow<NetworkResult<CommodityResponseData>> {
        return flow {
            emit(safeApiCall { apiService.getCommodityList(terminalId, inOut, userPhone) })
        }
    }

    suspend fun doCreateCaseId(
      createCaseIDPostData: CreateCaseIDPostData
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.doCreateCaseID(
                 createCaseIDPostData
                )
            })
        }
    }

    suspend fun getUserList(
        terminalId: String,
        inOut: String
    ): Flow<NetworkResult<AllUserListPojo>> {
        return flow {
            emit(safeApiCall { apiService.getUserList(terminalId, inOut) })
        }
    }

    suspend fun getStackRequestList():Flow<NetworkResult<StackRequestResponse>>{
        return flow { emit(safeApiCall { apiService.getStackRequest() }) }
    }
}