package com.example.ilya.lorekeep.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private final static Retrofit INSTANCE = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit retrofitLore(){
        return INSTANCE;
    }
}
