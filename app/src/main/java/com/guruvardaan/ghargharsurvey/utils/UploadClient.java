package com.guruvardaan.ghargharsurvey.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadClient {
    public static final String BASE_URL = "http://test.rudrakshainfra.com/api/addVisitCustomersImages/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectTimeout(120, TimeUnit.SECONDS);
            client.readTimeout(120, TimeUnit.SECONDS);
            client.writeTimeout(120, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client.build())
                    .build();
        }
        return retrofit;
    }
}