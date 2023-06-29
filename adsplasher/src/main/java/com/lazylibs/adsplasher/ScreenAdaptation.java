package com.lazylibs.adsplasher;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class ScreenAdaptation {

    /**
     * 設置應用全屏
     *
     * @return void
     * @author lanhm
     * @date 2014年11月18日 下午5:48:30
     * @MethodName: setFullScreen
     */
    public static void setFullScreen(@NonNull Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     *
     * @return void
     * @author lanhm
     * @date 2014年11月18日 下午5:48:52
     * @MethodName: quitFullScreen
     */
    public static void quitFullScreen(@NonNull Window window) {
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(attrs);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
//    public static void unFullScreen(@NonNull Window window) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(window。getcon.getColor(R.color.colorPrimary));
//            window
//                    .getDecorView()
//                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Utils.quitFullScreen(window);
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private static final String TAG = "ScreenAdaptation";

    /**
     * adapt fullScreen mode
     *
     * @param mActivity a
     */
    public static void openFullScreenModel(Activity mActivity) {
        try {
            if (needAdaptNotch(mActivity)) {
                mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                if (Build.VERSION.SDK_INT >= 28) {
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                }
                mActivity.getWindow().setAttributes(lp);
                View decorView = mActivity.getWindow().getDecorView();
                int systemUiVisibility = decorView.getSystemUiVisibility();
                int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                systemUiVisibility |= flags;
                mActivity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * need to adapt Notch screen
     *
     * @return true otherwise false
     */
    private static boolean needAdaptNotch(Context c) {
        return Build.VERSION.SDK_INT >= 28 || isHuaweiNotch(c) || isOppoNotch(c) || isVivoNotch(c);
    }

    /**
     * huawei
     *
     * @param context c
     * @return hasNotch
     */
    private static boolean isHuaweiNotch(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }

    /**
     * OPPO
     *
     * @param context Context
     * @return hasNotch
     */
    private static boolean isOppoNotch(Context context) {
        try {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * VIVO
     * param:
     * 0x00000020表示是否有凹槽;
     * 0x00000008表示是否有圆角。
     *
     * @param context Context
     * @return hasNotch
     */
    private static boolean isVivoNotch(Context context) {
        boolean hasNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("android.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport");
            hasNotch = (boolean) get.invoke(FtFeature, new Object[]{0x00000020});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNotch;
    }

}
