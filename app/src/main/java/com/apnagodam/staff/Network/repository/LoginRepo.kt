package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.LoginPostData
import com.apnagodam.staff.Network.Request.OTPData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.OTPvarifedResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepo @Inject constructor(val apiService: ApiService):BaseApiResponse() {

     suspend fun doLogin(loginPostData: LoginPostData):Flow<NetworkResult<LoginResponse>>{
        return  flow {
            emit(safeApiCall { apiService.doLogin(loginPostData) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun verifyOtp(loginPostData: OTPData):Flow<NetworkResult<OTPvarifedResponse>>{
        return  flow {
            emit(safeApiCall { apiService.doOTPVerify(loginPostData) })
        }.flowOn(Dispatchers.IO)
    }

}