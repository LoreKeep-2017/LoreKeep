package com.example.ilya.lorekeep;

import android.app.Application;

import com.vk.sdk.VKSdk;


public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(app.this);
    }
}
