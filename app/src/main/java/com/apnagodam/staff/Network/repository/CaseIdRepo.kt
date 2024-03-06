package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.CreateCaseIDPostData
import com.apnagodam.staff.Network.Request.RequestOfflineCaseData
import com.apnagodam.staff.Network.Request.StackPostData
import com.apnagodam.staff.Network.Response.AttendanceResponse
import com.apnagodam.staff.Network.Response.BaseResponse
import com.apnagodam.staff.Network.Response.DriverOtpResponse
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.ResponseFastcaseList
import com.apnagodam.staff.Network.Response.ResponseStackData
import com.apnagodam.staff.Network.Response.ResponseUserData
import com.apnagodam.staff.Network.Response.ResponseWarehouse
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
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCommodities(
        terminalId: String,
        inOut: String,
        userPhone: String
    ): Flow<NetworkResult<CommodityResponseData>> {
        return flow {
            emit(safeApiCall { apiService.getCommodityList(terminalId, inOut, userPhone) })
        }.flowOn(Dispatchers.IO)
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
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserList(
        terminalId: String,
        inOut: String
    ): Flow<NetworkResult<AllUserListPojo>> {
        return flow {
            emit(safeApiCall { apiService.getUserList(terminalId, inOut) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStackRequestList():Flow<NetworkResult<StackRequestResponse>>{
        return flow { emit(safeApiCall { apiService.getStackRequest() }) }.flowOn(Dispatchers.IO)
    }


    suspend fun getFastCaseWarehouse():Flow<NetworkResult<ResponseWarehouse>>{
        return flow {
            emit(safeApiCall {
                apiService.getWareHouseData()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStack(terminalId:String,commodityId:String):Flow<NetworkResult<ResponseStackData>>{
        return flow {
            emit(safeApiCall {
                apiService.getStack(terminalId,commodityId)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun  getUser(phone:String):Flow<NetworkResult<ResponseUserData>>{
        return  flow {
            emit(safeApiCall { apiService.getUserName(phone) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun offlineFastCase(requestOfflineCaseData: RequestOfflineCaseData):Flow<NetworkResult<BaseResponse>>{
        return  flow {
            emit(safeApiCall {
                apiService.offlineFastCase(requestOfflineCaseData)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun  getFastCaseList():Flow<NetworkResult<ResponseFastcaseList>>{
        return  flow {
            emit(safeApiCall {
                apiService.fastCaseList()
            })
        }.flowOn(Dispatchers.IO)
    }
}