package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.HomeRepo
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse
import com.apnagodam.staff.module.DashBoardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepo: HomeRepo,application: Application):AndroidViewModel(application) {
    var response = MutableLiveData<NetworkResult<AllUserPermissionsResultListResponse>>();
    var homeDataResponse = MutableLiveData<NetworkResult<DashBoardData>>()

    fun getDashboardData()=viewModelScope.launch {
        homeRepo.getDashboardData().collect(){
            homeDataResponse.value = it
        }
    }
    fun getPermissions(str:String,str2: String) = viewModelScope.launch {
         homeRepo.getPermission(str,str2).collect(){
             response.value =it
         }
    }
}