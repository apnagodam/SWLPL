package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.LeadsRepo
import com.apnagodam.staff.module.TerminalListPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeadsViewModel @Inject constructor(val leadsRepo: LeadsRepo,application: Application):AndroidViewModel(application) {
    var response = MutableLiveData<NetworkResult<TerminalListPojo>>()
    fun getTerminalList()=viewModelScope.launch {
        leadsRepo.getTerminalList().collect(){
            response.value = it;
        }

    }
}