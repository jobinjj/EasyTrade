package com.keralagamestudio.easytrade.utils;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    public Context getContext() {
        return this.getApplicationContext();
    }
}
