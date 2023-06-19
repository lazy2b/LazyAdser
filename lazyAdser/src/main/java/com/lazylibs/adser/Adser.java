package com.lazylibs.adser;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public enum Adser {
    CORE;

    public LiveData<AdjustAttribution> get() {
        return _liveData;
    }

    public boolean isAdser() {
        return isAdser(get().getValue());
    }

    public void update(@Nullable AdjustAttribution attribution) {
        _liveData.postValue(attribution);
    }

    public static void onCreate(Application app, String adsToken) {
        assert app != null;
        boolean debug = (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        AdjustConfig config = new AdjustConfig(app, adsToken, debug ? AdjustConfig.ENVIRONMENT_SANDBOX : AdjustConfig.ENVIRONMENT_PRODUCTION);
        config.setOnEventTrackingSucceededListener(adjustEventSuccess -> {
            Log.d("Lazy.Adser", "EventTrackingSucceeded:" + adjustEventSuccess.toString());
        });
        config.setOnEventTrackingFailedListener(adjustEventFailure -> {
            Log.d("Lazy.Adser", "EventTrackingFailed:" + adjustEventFailure.toString());
        });
        config.setOnSessionTrackingSucceededListener(adjustSessionSuccess -> {
            Log.d("Lazy.Adser", "SessionTrackingSucceeded:" + adjustSessionSuccess.toString());
        });
        config.setOnSessionTrackingFailedListener(adjustSessionFailure -> {
            Log.d("Lazy.Adser", "SessionTrackingFailed:" + adjustSessionFailure.toString());
        });
        config.setOnDeeplinkResponseListener(uri -> true);
        config.setOnAttributionChangedListener(Adser.CORE::update);
        config.setLogLevel(debug ? LogLevel.VERBOSE : LogLevel.ERROR);
        Adjust.onCreate(config);
        CORE.register(app);
    }

    public static void onTerminate(Application app) {
        CORE.unregister(app);
    }

    private Application.ActivityLifecycleCallbacks lifecycleCallbacks;

    private void register(Application app) {
        app.registerActivityLifecycleCallbacks(lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Adjust.onResume();
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Adjust.onPause();
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void unregister(Application app) {
        if (lifecycleCallbacks != null) {
            app.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
            lifecycleCallbacks = null;
        }
    }

    private final MutableLiveData<AdjustAttribution> _liveData = new LazyLiveData<>(this::fetchData);

    private LiveData<AdjustAttribution> fetchData() {
        return new MutableLiveData<>(Adjust.getAttribution());
    }

    private boolean isFbInstaller(AdjustAttribution attribution) {
        String fbInstallReferrer = attribution.fbInstallReferrer;
        if (fbInstallReferrer != null && (fbInstallReferrer.trim().startsWith("{") || fbInstallReferrer.trim().startsWith("["))) {
            try {
                JSONObject fbObj = new JSONObject(attribution.fbInstallReferrer);
                Log.d("Lazy.Adser", fbObj.toString());
                return true;
            } catch (JSONException e) {
                Log.d("Lazy.Adser", "not fb : " + e.getMessage());
            }
        }
        return false;
    }

    public boolean isAdser(@Nullable AdjustAttribution attribution) {
        if (attribution != null) {
            String networkInfo = attribution.network;
            return isFbInstaller(attribution) || (!(Objects.equals(networkInfo, "Organic") || Objects.equals(networkInfo, "Google%20Organic%20Search") || Objects.equals(networkInfo, "Google+Organic+Search")));
        }
        return false;
    }
}
