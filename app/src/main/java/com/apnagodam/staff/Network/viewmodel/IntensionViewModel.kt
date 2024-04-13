package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.IntentionRepo
import com.apnagodam.staff.module.AllIntantionList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntensionViewModel @Inject constructor(
    val intentionRepo: IntentionRepo,
    application: Application
) : AndroidViewModel(application) {

    val intentionListResponse = MutableLiveData<NetworkResult<AllIntantionList>>()

    fun getintentionList(limit: String, page: Int, search: String) = viewModelScope.launch {
        intentionRepo.getintentionList(limit, page, search).collect {

            intentionListResponse.value = it

        }
    }

}