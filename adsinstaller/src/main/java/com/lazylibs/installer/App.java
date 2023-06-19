package com.lazylibs.installer;

import android.app.Application;

import com.lazylibs.adser.Adser;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Adser.onCreate(this, "bn2v0eblh3wg");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Adser.onTerminate(this);
    }
}
