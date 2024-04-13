package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.repository.SpotDealRepo
import com.apnagodam.staff.module.SpotSellDealTrackPojo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotDealViewModel @Inject constructor(
    val sportDealRepo: SpotDealRepo,
    application: Application
) : AndroidViewModel(application) {
    val spotDealSellsResponse = MutableLiveData<NetworkResult<SpotSellDealTrackPojo>>()
    fun getSpotDeals(limit: String, page: String, search: String) = viewModelScope.launch {
        sportDealRepo.getSpotSellDeal(limit, page, search).collect{
            spotDealSellsResponse.value = it
        }
    }
}