package com.lazylibs.installer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.lazylibs.adser.Adser;
import com.lazylibs.adsplasher.ScreenAdaptation;
import com.lazylibs.webviewer.LazyWebActivity;

public class PatosActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_main);
        preferences = getPreferences(Context.MODE_PRIVATE);
        viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_left_in));
        Animation animOut = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_left_out);
        viewFlipper.setOutAnimation(animOut);
        if (preferences.getBoolean("isAgreePatos", false)) {
            v2Loading();
        } else {
            v2Patos();
        }
    }

    private View vPatos;

    private void v2Patos() {
        vPatos = LayoutInflater.from(this).inflate(R.layout.tpl_patos, viewFlipper, false);
        viewFlipper.addView(vPatos);
        viewFlipper.showNext();
        vPatos.findViewById(R.id.tv_agree).setOnClickListener(v -> {
            preferences.edit().putBoolean("isAgreePatos", true).apply();
            v2Loading();
        });
    }

    private void v2Loading() {
        View vLoading = LayoutInflater.from(this).inflate(R.layout.tpl_loading, viewFlipper, false);
        viewFlipper.addView(vLoading);
        viewFlipper.showNext();
        if (vPatos != null) viewFlipper.removeView(vPatos);
        skipNext();
    }

    private void skipNext() {
        Adser.CORE.get().observe(this, attribution -> {
            if (attribution != null) {
                ScreenAdaptation.quitFullScreen(PatosActivity.this.getWindow());
                if (Adser.CORE.isAdser()) {
                    LazyWebActivity.start(this, "https://baid.com");
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
            }
        });
    }
}