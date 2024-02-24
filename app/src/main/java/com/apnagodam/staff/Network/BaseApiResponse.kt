package com.apnagodam.staff.Network

import android.content.Intent
import com.apnagodam.staff.activity.LoginActivity
import com.apnagodam.staff.db.SharedPreferencesRepository
import retrofit2.Response


abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }

            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
    private fun <T> error(errorMessage: String): NetworkResult<T> =
            NetworkResult.Error("Api call failed $errorMessage")


    private fun <T> loading():NetworkResult<T> = NetworkResult.Loading()
}