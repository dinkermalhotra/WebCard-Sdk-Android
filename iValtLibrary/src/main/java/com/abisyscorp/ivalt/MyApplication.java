package com.abisyscorp.ivalt;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mInstance = this;
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
