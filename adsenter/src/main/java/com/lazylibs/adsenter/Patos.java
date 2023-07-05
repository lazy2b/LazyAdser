package com.lazylibs.adsenter;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
//        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_left_in));
//        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.push_right_out));
        vLoading = LayoutInflater.from(this).inflate(R.layout.tpl_loading, viewFlipper, false);
        if (Enter.isAgreePatos(this)) {
            v2Main();
        } else {
            v2Patos();
        }
    }

    private View vPatos, vLoading;

    private void v2Patos() {

        viewFlipper.addView(vLoading);
        viewFlipper.showNext();

        vPatos = LayoutInflater.from(this).inflate(R.layout.tpl_patos, viewFlipper, false);
        TextView tvAgree = vPatos.findViewById(R.id.tv_agree);
        TextView tvPatos = vPatos.findViewById(R.id.tv_patos);
        tvAgree.setOnClickListener(v -> {
            Enter.setAgreePatos(this);
            v2Main();
        });

        String patos = Enter.skipper().getPatos();
        if (patos.startsWith("http://") || patos.startsWith("https://")) {
            vPatos.findViewById(R.id.wb_patos).setVisibility(View.VISIBLE);
            FragmentUtils.replaceFragmentToActivity(R.id.wb_patos, getSupportFragmentManager(), LazyWebFragment.newInstance(patos, null).setExtraWebHandler(new IWebHandler() {
                @Override
                public void onRealPageFinished(String url, boolean isReceivedError) {
                    tvAgree.setVisibility(View.VISIBLE);
                }
            }));
        } else {
            tvPatos.setText(Html.fromHtml(patos, Html.FROM_HTML_MODE_COMPACT));
            tvPatos.setVisibility(View.VISIBLE);
            tvAgree.setVisibility(View.VISIBLE);
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