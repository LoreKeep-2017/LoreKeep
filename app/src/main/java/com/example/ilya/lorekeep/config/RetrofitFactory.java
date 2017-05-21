package com.example.ilya.lorekeep.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private final static Retrofit INSTANCE = new Retrofit.Builder()
            .baseUrl("https://whispering-castle-86895.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit retrofitLore(){
        return INSTANCE;
    }
}
