package com.lazylibs.installer;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.lazylibs.adsenter.Enter;
import com.lazylibs.adsenter.Patos;
import com.lazylibs.adser.Adser;
import com.lazylibs.webviewer.LazyWebActivity;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Adser.onCreate(this, "bn2v0eblh3wg");
        Enter.onCreate(new Enter.ISkipper() {
            @Override
            public void adsed(Activity activity, boolean isAdser) {
                if (isAdser) {
                    LazyWebActivity.start(activity.getApplicationContext(), "https://bing.com");
                } else {
                    activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                }
                activity.finish();
            }

            @Override
            public void afterEnter(Activity activity, boolean agreePatos) {
                startActivity(new Intent(activity, Patos.class));
                activity.finish();
            }

            @Override
            public String getPatos() {
                return "https://bing.com";//"必须同意，不同意不给玩哦";//"https://bing.com";
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Adser.onTerminate(this);
        Enter.onTerminate();
    }
}
