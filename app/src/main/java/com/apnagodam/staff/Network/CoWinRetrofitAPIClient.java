package com.apnagodam.staff.Network;

import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoWinRetrofitAPIClient {
    public static ApiService getRetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(Constants.API_COWIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient())
                .build()
                .create(ApiService.class);
    }

    public static OkHttpClient okHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();
                // set OAuth token
                Request.Builder newRequest = originalRequest.newBuilder();
                String accessToken = SharedPreferencesRepository.getDataManagerInstance().getSessionToken();

                newRequest.header("Content-Type", "application/json");
                newRequest.header("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7");
                newRequest.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36/8mqQaZuL-16");
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