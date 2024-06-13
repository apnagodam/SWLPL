package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.Response.AttendanceResponse
import com.apnagodam.staff.Network.repository.AttendanceRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendenceViewModel @Inject constructor(
    application: Application,
    private val attendanceRepo: AttendanceRepo
) : AndroidViewModel(application) {
     val attendenceRespons = MutableLiveData<NetworkResult<AttendanceResponse>>();
     val setAttandanceResponse = MutableLiveData<NetworkResult<AttendanceResponse>>()

    fun checkClockStatus() = viewModelScope.launch {
        attendanceRepo.checkClockStatus().collect {
            attendenceRespons.value = it;
        }
    }

    fun setAttandance(attendancePostData: AttendancePostData) = viewModelScope.launch {
        attendanceRepo.setAttandance(attendancePostData).collect {
            setAttandanceResponse.value = it
        }
    }
}