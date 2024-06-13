package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkSpeedViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    var networkSpeedMonitor = MutableLiveData<Int?>()
    fun getNetworkSpeed(context: Context) = viewModelScope.launch {
        networkSpeed(context).collect {
            it?.let {
                networkSpeedMonitor.value = it
            }
        }
    }

    suspend fun networkSpeed(context: Context): Flow<Int?> {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return flow {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nc = cm.getNetworkCapabilities(cm.activeNetwork)
                val downSpeed = (nc?.linkDownstreamBandwidthKbps)?.div(10000)
                emit(downSpeed)
            } else emit(0)
        }.flowOn(Dispatchers.IO)
    }

}