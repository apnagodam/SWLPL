package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.LeadsRepo
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.module.AllLeadsResponse
import com.apnagodam.staff.module.TerminalListPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeadsViewModel @Inject constructor(val leadsRepo: LeadsRepo,application: Application):AndroidViewModel(application) {
    var response = MutableLiveData<NetworkResult<TerminalListPojo>>()
    var allLeadsResponse = MutableLiveData<NetworkResult<AllLeadsResponse>>()
    fun getTerminalList()=viewModelScope.launch {
        leadsRepo.getTerminalList().collect(){

            response.value = it;
        }

    }
    fun getAllLeads(limit:String,page:Int,inOut:String,search:String)=viewModelScope.launch {
        leadsRepo.getAllLeads(limit,page,inOut,search).collect(){
            if(it.data!!.status!="1"){
                SharedPreferencesRepository.logout()
                val intent = Intent(getApplication(), LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(getApplication(), intent, null)
            }
            allLeadsResponse.value=it
        }
    }
}