package com.apnagodam.staff.Network.repository

import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.BaseApiResponse
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.module.SpotSellDealTrackPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SpotDealRepo @Inject constructor(val apiService: ApiService): BaseApiResponse() {

    suspend fun getSpotSellDeal(limit:String,page:String,search:String):Flow<NetworkResult<SpotSellDealTrackPojo>>{
        return  flow {
            emit(safeApiCall { apiService.getSpotSellDealTrackList(limit,page,search) })
        }.flowOn(Dispatchers.IO)
    }
}