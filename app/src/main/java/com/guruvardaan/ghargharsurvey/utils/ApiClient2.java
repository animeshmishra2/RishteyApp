package com.guruvardaan.ghargharsurvey.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient2 {
    public static final String BASE_URL = "http://test.rudrakshainfra.com/api/changeProfilePicAgent/K9154289-68a1-80c7-e009-2asdccf7b0d/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
           /* OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectTimeout(120, TimeUnit.SECONDS);
            client.readTimeout(120, TimeUnit.SECONDS);
            client.writeTimeout(120, TimeUnit.SECONDS);*/
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())/*.client(client.build())*/
                    .build();
        }
        return retrofit;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean outcome = false;
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo tempNetworkInfo : networkInfos) {
                if (tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }

        return outcome;

        //return  isOnline();
    }

}