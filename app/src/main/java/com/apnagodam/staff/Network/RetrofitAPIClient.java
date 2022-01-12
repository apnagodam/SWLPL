package com.apnagodam.staff.Network;

import android.util.Log;

import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intuit.sdp.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClient {
    public static ApiService getRetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient())
                .build()
                .create(ApiService.class);
    }

    public static OkHttpClient okHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("response", message);
            }
        });

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();
                // set OAuth token
                Request.Builder newRequest = originalRequest.newBuilder();
                String accessToken = SharedPreferencesRepository.getDataManagerInstance().getSessionToken();

                newRequest.header("Content-Type", "application/json");
                newRequest.header("language", SharedPreferencesRepository.getDataManagerInstance().getSelectedLanguage());
                newRequest.header("lat", SharedPreferencesRepository.getDataManagerInstance().getlat());
                newRequest.header("long", SharedPreferencesRepository.getDataManagerInstance().getlong());
                newRequest.header("Authorization", accessToken).method(originalRequest.method(), originalRequest.body());
               // newRequest.header("Authorization", Credentials.basic("power", "$power2019$")).build();

                originalRequest = newRequest.build();

                Response response = chain.proceed(originalRequest);
                int responseCode = response.code();

                return response;
            }
        });

        // Log the request only in debug mode.
     /*   if (BuildConfig.DEBUG)*/
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // set the connection time to 1 minutes
        httpClient.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        httpClient.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                //.sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager())
                .addInterceptor(httpLoggingInterceptor);
        return httpClient.build();
    }
}