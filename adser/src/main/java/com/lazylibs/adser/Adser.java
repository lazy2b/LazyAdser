package com.lazylibs.adser;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public static void logD(String msg) {
        Log.d("Lazy.Adser", msg);
    }

    public static void onCreate(Application app, String adsToken) {
        assert app != null;
        boolean debug = (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        AdjustConfig config = new AdjustConfig(app, adsToken, debug ? AdjustConfig.ENVIRONMENT_SANDBOX : AdjustConfig.ENVIRONMENT_PRODUCTION);
        config.setOnEventTrackingSucceededListener(adjustEventSuccess -> {
            logD("EventTrackingSucceeded:" + adjustEventSuccess.toString());
        });
        config.setOnEventTrackingFailedListener(adjustEventFailure -> {
            logD("EventTrackingFailed:" + adjustEventFailure.toString());
        });
        config.setOnSessionTrackingSucceededListener(adjustSessionSuccess -> {
            logD("SessionTrackingSucceeded:" + adjustSessionSuccess.toString());
        });
        config.setOnSessionTrackingFailedListener(adjustSessionFailure -> {
            logD("SessionTrackingFailed:" + adjustSessionFailure.toString());
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

    private static String CURRENCY = "";
    private static Map<String, String> EVENTS = new HashMap<>();

    public static void setup(String currency) {
        CURRENCY = currency;
    }

    public static void setup(Map<String, String> events) {
        EVENTS = events;
    }

    public static void trackEvent(String key, String value) {
        if (TextUtils.isEmpty(key)) return;
        logD(String.format("Adset.trackEvent('%s','%s');", key, value));
        if (EVENTS.containsKey(key)) {
            String eToken = EVENTS.get(key);
            if (!TextUtils.isEmpty(eToken)) {
                AdjustEvent adjustEvent = new AdjustEvent(eToken);
                if (!TextUtils.isEmpty(eToken)) {
                    adjustEvent.setCallbackId(value);
                }
                Adjust.trackEvent(adjustEvent);
            }
        }
    }

    public static void trackRevenue(String key, String value, String... args) {
        if (TextUtils.isEmpty(key)) return;
        logD(String.format("Adset.trackEvent('%s','%s');", key, value));
        if (EVENTS.containsKey(key)) {
            String eToken = EVENTS.get(key);
            if (!TextUtils.isEmpty(eToken)) {
                AdjustEvent adjustEvent = new AdjustEvent(eToken);
                String _currency = args.length == 0 ? CURRENCY : args[0];
                try {
                    adjustEvent.setRevenue(Double.parseDouble(value), _currency);
                } catch (NumberFormatException ignored) {
                }
                Adjust.trackEvent(adjustEvent);
            }
        }
    }

}
