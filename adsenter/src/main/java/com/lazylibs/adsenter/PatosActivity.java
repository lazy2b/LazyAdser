package com.lazylibs.adsenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.lazylibs.adser.Adser;
import com.lazylibs.utils.FragmentUtils;
import com.lazylibs.webviewer.IWebHandler;
import com.lazylibs.webviewer.LazyWebFragment;

import org.w3c.dom.Text;

public class PatosActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflipper);
        preferences = getPreferences(Context.MODE_PRIVATE);
        viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_left_in));
        Animation animOut = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_left_out);
        viewFlipper.setOutAnimation(animOut);
        vLoading = LayoutInflater.from(this).inflate(R.layout.tpl_loading, viewFlipper, false);
        if (preferences.getBoolean("isAgreePatos", false)) {
            v2Loading();
        } else {
            viewFlipper.addView(vLoading);
            viewFlipper.showNext();
            v2Patos();
        }
    }

    private View vPatos, vLoading;

    private void v2Patos() {
        vPatos = LayoutInflater.from(this).inflate(R.layout.tpl_patos, viewFlipper, false);
        TextView tvAgree = vPatos.findViewById(R.id.tv_agree);
        TextView tvPatos = vPatos.findViewById(R.id.tv_patos);
        View wbPatos = vPatos.findViewById(R.id.wb_patos);
        tvAgree.setOnClickListener(v -> {
            preferences.edit().putBoolean("isAgreePatos", true).apply();
            v2Loading();
        });
        String patos = Enter.Skipper.Impl().getPatos();
        if(patos.startsWith("http")){
            wbPatos.setVisibility(View.VISIBLE);
            FragmentUtils.replaceFragmentToActivity(R.id.wb_patos, getSupportFragmentManager(),  LazyWebFragment.newInstance(Enter.Skipper.Impl().getPatos(), null).setExtraWebHandler(new IWebHandler() {
                @Override
                public void onRealPageFinished(String url, boolean isReceivedError) {
                    tvAgree.setVisibility(View.VISIBLE);
                }
            }));
        } else {
            tvPatos.setText(patos);
            tvPatos.setVisibility(View.VISIBLE);
            tvAgree.setVisibility(View.VISIBLE);
        }
        viewFlipper.addView(vPatos);
        viewFlipper.showNext();
        viewFlipper.removeView(vLoading);
    }

    private void v2Loading() {
        viewFlipper.addView(vLoading);
        viewFlipper.showNext();
        if (vPatos != null) viewFlipper.removeView(vPatos);
        skipNext();
    }

    private void skipNext() {
        Adser.CORE.get().observe(this, attribution -> {
            if (attribution != null) {
                quitFullScreen(PatosActivity.this.getWindow());
                Enter.Skipper.Impl().afterPatosAgree(PatosActivity.this, Adser.CORE.isAdser());
            }
        });
    }
    public static void quitFullScreen(Window window) {
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(attrs);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}