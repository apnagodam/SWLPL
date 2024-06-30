package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.Response.AttendanceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AttendanceRepo @Inject constructor(private val apiService: ApiService) : BaseApiResponse() {

    suspend fun checkClockStatus() = flow{
        emit(safeApiCall {
            apiService.getattendanceStatus()
        })
    }.flowOn(Dispatchers.IO)

    suspend fun setAttandance(attendancePostData: AttendancePostData) =
        flow{
            emit(safeApiCall {
                apiService.attendance(attendancePostData)
            })
        }.flowOn(Dispatchers.IO)
}