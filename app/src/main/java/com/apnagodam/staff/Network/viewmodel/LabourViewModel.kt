package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.LabourRepo
import com.apnagodam.staff.module.AllLabourBookListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabourViewModel @Inject constructor(val labourRepo: LabourRepo,application: Application):AndroidViewModel(application){

    var labourResponse = MutableLiveData<NetworkResult<AllLabourBookListResponse>>()

    fun getLabourList(limit:String,page:String,inOut:String,search:String) = viewModelScope.launch {
        labourRepo.getLabourList(limit,page,inOut,search).collect(){
            labourResponse.value = it
        }
    }

}