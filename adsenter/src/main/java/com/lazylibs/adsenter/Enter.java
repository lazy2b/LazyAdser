package com.lazylibs.adsenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.lifecycle.Observer;

import com.adjust.sdk.AdjustAttribution;
import com.lazylibs.adser.Adser;

public class Enter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        FrameLayout space = new FrameLayout(this);
        space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(space);
        space.postDelayed(() -> skipper().afterEnter(this, isAgreePatos(this)), 1000);
    }

    public interface ISkipper {
        default void afterEnter(Activity activity, boolean agreePatos) {
            if (agreePatos) {
                skipPatos(activity);
            } else {
                showPatos(activity);
            }
        }

        default void showPatos(Activity activity) {
        }

        default void skipPatos(Activity activity) {
            Adser.CORE.get().observeForever(new Observer<AdjustAttribution>() {
                @Override
                public void onChanged(AdjustAttribution attribution) {
                    if (attribution != null) {
//                        quitFullScreen(activity.getWindow());
                        skipper().adsed(activity, Adser.CORE.isAdser());
                        Adser.CORE.get().removeObserver(this);
                    }
                }
            });
        }

        default void adsed(Activity activity, boolean isAdser) {
        }

        default String getPatos() {
            return "";
        }
    }

    public static final String SP_KEY_IS_AGREE_PATOS = "isAgreePatos";
    private static ISkipper _skipper = null;

    public static ISkipper skipper() {
        if (_skipper == null) {
            throw new RuntimeException("Application.onCreate() -> Enter.Skipper.onCreate()!");
        }
        return _skipper;
    }

    public static void onCreate(ISkipper skipper) {
        _skipper = skipper;
    }

    public static void onTerminate() {
        _skipper = null;
    }

    public static boolean isEmpty(Context context) {
        return context == null || _skipper == null;
    }

    protected static boolean isAgreePatos(Context context) {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean(SP_KEY_IS_AGREE_PATOS, false);
    }

    protected static void setAgreePatos(Context context) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putBoolean(SP_KEY_IS_AGREE_PATOS, true).apply();
    }

    public static int dp(Context context, float dpValue) {
        // 获取设备屏幕的密度比例因子
        float scale = context.getResources().getDisplayMetrics().density;
        // 将dp值转换为像素值
        return (int) (dpValue * scale + 0.5f);
    }

    public static void quitFullScreen(Window window) {
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(attrs);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}