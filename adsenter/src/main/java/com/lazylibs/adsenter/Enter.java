package com.lazylibs.adsenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Enter extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_enter);
        findViewById(android.R.id.content).postDelayed(() -> {
            Log.e("", "2000200020002000200020002000200020002000200020002000");
            startActivity(getNext());
            finish();
        }, 500);
    }

    protected Intent getNext() {
        return new Intent(this, PatosActivity.class);
    }

    public static class Skipper {
        private static ISkipper Impl = null;

        public static ISkipper Impl() {
            if (Impl == null) {
                throw new RuntimeException("Application.onCreate() -> Enter.Skipper.onCreate()!");
            }
            return Impl;
        }

        public static void onCreate(ISkipper skipper) {
            Impl = skipper;
        }

        public static void onTerminate() {
            Impl = null;
        }

        public static boolean isEmpty(Context context) {
            return context == null || Impl == null;
        }

        public interface ISkipper {
            void afterPatosAgree(Activity activity, boolean isAdser);
            String getPatos();
        }
    }
}
