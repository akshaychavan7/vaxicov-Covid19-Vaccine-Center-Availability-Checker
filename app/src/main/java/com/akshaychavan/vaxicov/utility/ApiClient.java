package com.akshaychavan.vaxicov.utility;

/**
 * Created by Akshay Chavan on 06,May,2021
 * akshaychavan.kkwedu@gmail.com.com
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {


    private static final String BASE_URL = "https://cdn-api.co-vin.in/api/v2/";
    private static final String BASE_URL2 = "https://api.countapi.xyz/hit/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;

    // for BASE_URL
    public static Retrofit getClient() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                  //  .client(getUnsafeOkHttpClient().build())                // to handle security certificates issue
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientCounter() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();

        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .client(okHttpClient)
                    //  .client(getUnsafeOkHttpClient().build())                // to handle security certificates issue
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .build();
        }
        return retrofit2;
    }

}
