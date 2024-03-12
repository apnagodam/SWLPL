package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.AttendancePostData
import com.apnagodam.staff.Network.Response.AttendanceResponse
import com.apnagodam.staff.Network.Response.VersionCodeResponse
import com.apnagodam.staff.Network.repository.HomeRepo
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse
import com.apnagodam.staff.module.CommudityResponse
import com.apnagodam.staff.module.DashBoardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepo: HomeRepo, application: Application) :
    AndroidViewModel(application) {
    var response = MutableLiveData<NetworkResult<AllUserPermissionsResultListResponse>>();
    var homeDataResponse = MutableLiveData<NetworkResult<DashBoardData>>()
    var commoditiesReponse = MutableLiveData<NetworkResult<CommudityResponse>>()
    var appVersionResponse = MutableLiveData<NetworkResult<VersionCodeResponse>>()
    var attendenceResponse = MutableLiveData<NetworkResult<AttendanceResponse>>()
    fun getDashboardData() = viewModelScope.launch {
        homeRepo.getDashboardData().collect() {
//            if(it.data!!.status!="1"){
//                SharedPreferencesRepository.logout()
//                val intent = Intent(getApplication(), LoginActivity::class.java)
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(getApplication(),intent,null)
//            }
            homeDataResponse.value = it
        }
    }

    fun getPermissions(str: String, str2: String) = viewModelScope.launch {
        homeRepo.getPermission(str, str2).collect() {
            response.value = it
        }
    }

    fun getCommodities(appType: String) = viewModelScope.launch {
        homeRepo.getCommodities(appType).collect()
        {
            commoditiesReponse.value = it
        }
    }

    fun getAppVersion(appType: String) = viewModelScope.launch {
        homeRepo.getVersion(appType).collect(){
            appVersionResponse.value = it
        }
    }
    fun attendence(attendancePostData: AttendancePostData) = viewModelScope.launch {
        homeRepo.attendence(attendancePostData).collect(){
            attendenceResponse.value = it
        }
    }
}