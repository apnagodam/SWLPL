package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.KantaParchiRepo
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class KantaParchiViewModel @Inject constructor(val kantaParchiRepo: KantaParchiRepo,application: Application):AndroidViewModel(application) {

    var kantaParchiResponse = MutableLiveData<NetworkResult<FirstkanthaParchiListResponse>>();
    var sKantaParchiResponse = MutableLiveData<NetworkResult<SecoundkanthaParchiListResponse>>();

    var uploadFirstKantaParchiResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    fun getKantaParchiListing(limit:String,page:String,inOut:String,search:String)= viewModelScope.launch {
        kantaParchiRepo.getKantaParchiListing(limit,page,inOut,search).collect(){
            kantaParchiResponse.value = it
        }
    }

    fun uploadFirstKantaParchi(uploadFirstkantaParchiPostData: UploadFirstkantaParchiPostData)=viewModelScope.launch {
        kantaParchiRepo.uploadFirstKantaParchi(uploadFirstkantaParchiPostData).collect()
        {
            uploadFirstKantaParchiResponse.value = it
        }
    }
    fun getSKantaParchiListing(limit:String,page:String,inOut:String,search:String)= viewModelScope.launch {
        kantaParchiRepo.getSecondKantaParchiList(limit,page,inOut,search).collect(){
            sKantaParchiResponse.value = it
        }
    }
}