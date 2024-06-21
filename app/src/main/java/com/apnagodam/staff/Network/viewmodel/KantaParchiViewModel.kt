package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstkantaParchiPostData
import com.apnagodam.staff.Network.Request.UploadSecoundkantaParchiPostData
import com.apnagodam.staff.Network.Response.DharamKanta
import com.apnagodam.staff.Network.Response.DharmaKantaNameResponse
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.repository.KantaParchiRepo
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.FirstkanthaParchiListResponse
import com.apnagodam.staff.module.SecoundkanthaParchiListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KantaParchiViewModel @Inject constructor(
    val kantaParchiRepo: KantaParchiRepo,
    application: Application
) : AndroidViewModel(application) {

    var kantaParchiResponse = MutableLiveData<NetworkResult<FirstkanthaParchiListResponse>>();
    var sKantaParchiResponse = MutableLiveData<NetworkResult<SecoundkanthaParchiListResponse>>();

    var uploadFirstKantaParchiResponse = MutableLiveData<NetworkResult<LoginResponse>>()

    var uploadSecondKantaParchiResponse = MutableLiveData<NetworkResult<LoginResponse>>()

    var dharmaKantaNameResponse = MutableLiveData<NetworkResult<DharmaKantaNameResponse>>();

    var dharamKantaResponse = MutableLiveData<NetworkResult<DharamKanta>>()
    fun getKantaParchiListing(limit: String, page: String, inOut: String, search: String) =
        viewModelScope.launch {
            kantaParchiRepo.getKantaParchiListing(limit, page, inOut, search).collect() {
                if (it.data!!.status != "1") {
                    SharedPreferencesRepository.logout()
                    val intent = Intent(getApplication(), LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ContextCompat.startActivity(getApplication(), intent, null)
                }
                kantaParchiResponse.value = it
            }
        }

    fun getDharmaKantasName(warehouseId: String) = viewModelScope.launch {
        kantaParchiRepo.getDharmaKantasName(warehouseId).collect() {

            dharmaKantaNameResponse.value = it
        }
    }

    fun uploadFirstKantaParchi(
        uploadFirstkantaParchiPostData: UploadFirstkantaParchiPostData,
        inOut: String
    ) = viewModelScope.launch {
        kantaParchiRepo.uploadFirstKantaParchi(uploadFirstkantaParchiPostData, inOut).collect()
        {
            uploadFirstKantaParchiResponse.value = it
        }
    }

    fun uploadSecondKantaParchi(
        uploadFirstkantaParchiPostData: UploadSecoundkantaParchiPostData,
        inOut: String
    ) = viewModelScope.launch {
        kantaParchiRepo.uploadSecondKantaParchi(uploadFirstkantaParchiPostData, inOut).collect()
        {
            uploadSecondKantaParchiResponse.value = it
        }
    }

    fun getSKantaParchiListing(limit: String, page: String, inOut: String, search: String) =
        viewModelScope.launch {
            kantaParchiRepo.getSecondKantaParchiList(limit, page, inOut, search).collect() {
                sKantaParchiResponse.value = it
            }
        }

    fun getKantaDetails(caseId: String) = viewModelScope.launch {
        kantaParchiRepo.getDharamKantaDetails(caseId).collect() {
            dharamKantaResponse.value = it
        }
    }
}