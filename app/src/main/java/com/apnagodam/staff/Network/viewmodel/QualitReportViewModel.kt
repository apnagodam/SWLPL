package com.apnagodam.staff.Network.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.Network.Response.QualityParamsResponse
import com.apnagodam.staff.Network.repository.QualityReportRepo
import com.apnagodam.staff.module.FirstQuilityReportListResponse
import com.apnagodam.staff.module.SecoundQuilityReportListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QualitReportViewModel @Inject constructor(
    val qualityReportRepo: QualityReportRepo,
    application: Application
) : AndroidViewModel(application) {


    var fQualityResponse = MutableLiveData<NetworkResult<FirstQuilityReportListResponse>>()
    var sQualityResponse = MutableLiveData<NetworkResult<SecoundQuilityReportListResponse>>()
    var fQualityUploadResponse = MutableLiveData<NetworkResult<LoginResponse>>()

    var sQualityUploadResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    var commodityResponse = MutableLiveData<NetworkResult<QualityParamsResponse>>()

    fun getFirstQualityListing(limit: String, page: String, inOut: String, search: String) =
        viewModelScope.launch {
            qualityReportRepo.getQualityReportList(limit, page, inOut, search).collect() {
                fQualityResponse.value = it
            }
        }

    fun getSecondQualityListing(limit: String, page: String, inOut: String, search: String) =
        viewModelScope.launch {
            qualityReportRepo.getSQualityReportList(limit, page, inOut, search).collect() {
                sQualityResponse.value = it
            }
        }


    fun uploadFirstQualityReport(uploadFirstQualityPostData: UploadFirstQualityPostData) =
        viewModelScope.launch {
            qualityReportRepo.uploadFirstQualityReport(uploadFirstQualityPostData)
                .collect() {
                    fQualityUploadResponse.value = it
                }
        }

    fun uploadSecondQualityReport(uploadSecoundQualityPostData: UploadSecoundQualityPostData) =
        viewModelScope.launch {
            qualityReportRepo.uploadSecondQualityReport(uploadSecoundQualityPostData)
                .collect() {
                    sQualityUploadResponse.value = it
                }
        }

    fun getCommodityParams(case_id: String) = viewModelScope.launch {
        qualityReportRepo.getCommodityParams(case_id).collect() {
            commodityResponse.value = it
        }
    }
}