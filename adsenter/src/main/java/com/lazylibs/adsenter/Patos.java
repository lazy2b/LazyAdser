package com.lazylibs.adsenter;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.lazylibs.utils.FragmentUtils;
import com.lazylibs.webviewer.IWebHandler;
import com.lazylibs.webviewer.LazyWebFragment;

public class Patos extends AppCompatActivity {

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewFlipper = new ViewFlipper(this);
        viewFlipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(viewFlipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_left_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_right_out));
        vLoading = LayoutInflater.from(this).inflate(R.layout.tpl_loading, viewFlipper, false);
        if (Enter.isAgreePatos(this)) {
            v2Main();
        } else {
            v2Patos();
        }
    }

    private View vLoading;
    private FrameLayout vPatos;
    private FrameLayout.LayoutParams layoutParams;

    private void v2Patos() {

        viewFlipper.addView(vLoading);
        viewFlipper.showNext();

        vPatos = new FrameLayout(this);
        vPatos.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        Button tvAgree = new Button(this);
        layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        layoutParams.bottomMargin = Enter.dp(this, 20);
        tvAgree.setLayoutParams(layoutParams);
        tvAgree.setGravity(Gravity.CENTER);
        tvAgree.setBackgroundColor(Color.RED);
        tvAgree.setText(R.string.agree);
        tvAgree.setOnClickListener(v -> {
            Enter.setAgreePatos(this);
            v2Main();
        });

        String patos = Enter.skipper().getPatos();
        if (patos.startsWith("http://") || patos.startsWith("https://")) {
            View wbPatos = new FrameLayout(this);
            wbPatos.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            wbPatos.setId(View.generateViewId());
            vPatos.addView(wbPatos);
            FragmentUtils.replaceFragmentToActivity(wbPatos.getId(), getSupportFragmentManager(), LazyWebFragment.newInstance(Enter.skipper().getPatos(), null).setExtraWebHandler(new IWebHandler() {
                @Override
                public void onRealPageFinished(String url, boolean isReceivedError) {
                    vPatos.addView(tvAgree);
                }
            }));
        } else {
            TextView tvPatos = new TextView(this);
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            tvPatos.setLayoutParams(layoutParams);
            tvPatos.setGravity(Gravity.CENTER);
            tvPatos.setText(Html.fromHtml(patos, Html.FROM_HTML_MODE_COMPACT));
            vPatos.addView(tvPatos);
            vPatos.addView(tvAgree);
        }
        viewFlipper.addView(vPatos);
        viewFlipper.showNext();
        viewFlipper.removeView(vLoading);
    }

    private void v2Main() {
        viewFlipper.addView(vLoading);
        viewFlipper.showNext();
        if (vPatos != null) viewFlipper.removeView(vPatos);
        Enter.skipper().skipPatos(this);
    }
}