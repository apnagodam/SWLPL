package com.apnagodam.staff

import com.apnagodam.staff.Network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

import android.util.Log
import com.apnagodam.staff.db.SharedPreferencesRepository
import com.apnagodam.staff.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intuit.sdp.BuildConfig
import java.io.IOException
import java.util.Collections
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
                .Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()
    }
    @Provides
    fun okHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var originalRequest: Request = chain.request()
                // set OAuth token
                val newRequest = originalRequest.newBuilder()
                val accessToken = SharedPreferencesRepository.getSessionToken()
                newRequest.header("language", SharedPreferencesRepository.getDataManagerInstance().selectedLanguage)
                newRequest.header("lat", SharedPreferencesRepository.getDataManagerInstance().getlat())
                newRequest.header("long", SharedPreferencesRepository.getDataManagerInstance().getlong())
                newRequest.header("Authorization", accessToken).method(originalRequest.method, originalRequest.body)
                // newRequest.header("Authorization", Credentials.basic("power", "$power2019$")).build();
                originalRequest = newRequest.build()
                val response: Response = chain.proceed(originalRequest)
                val responseCode = response.code
                return response
            }
        })

        // Log the request only in debug mode.
        /*   if (BuildConfig.DEBUG)*/
        //     httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // set the connection time to 1 minutes
        httpClient.protocols(listOf(Protocol.HTTP_1_1))
        httpClient.connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }
    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory =
            GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
            gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(okHttpClient())
                .addConverterFactory(gsonConverterFactory)
                .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)
}