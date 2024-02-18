package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.OTPVerifyGatePassData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.module.GatePassListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query
import javax.inject.Inject

class GatePassRepo @Inject constructor(val apiService: ApiService):BaseApiResponse() {
    suspend fun  getGatePassListing(limit:String,page:String,inOut:String,search:String): Flow<NetworkResult<GatePassListResponse>> {
        return  flow {
            emit(safeApiCall { apiService.getGatePass(
                    limit,page,inOut,search
            ) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun doVerifyOtp(otpVerifyGatePassData: OTPVerifyGatePassData):Flow<NetworkResult<LoginResponse>>{
        return  flow { emit(safeApiCall { apiService.doVerifyGatePassOTP(otpVerifyGatePassData) }) }.flowOn(Dispatchers.IO)
    }
}