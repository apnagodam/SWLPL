package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.CaseIdRepo
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.module.TerminalListPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class CaseIdViewModel @Inject constructor(private val repository: CaseIdRepo,
                                          application: Application) : AndroidViewModel(application) {
    private val _response: MutableLiveData<NetworkResult<AllCaseIDResponse>> = MutableLiveData()
    private val terminalResponse: MutableLiveData<NetworkResult<TerminalListPojo>> = MutableLiveData()

    val response: LiveData<NetworkResult<AllCaseIDResponse>> = _response
    fun getCaseId(str: String?, i: Int, str2: String?, str3: String?)=viewModelScope.launch {
        repository.getCaseId(str,i,str2,str3).collect { values ->
            _response.value = values
        }
    }
    fun getTerminalList()=viewModelScope.launch {
        repository.getTerminalList().collect(){
            terminalResponse.value = it;
        }

    }
}