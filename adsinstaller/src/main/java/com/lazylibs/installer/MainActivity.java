package com.lazylibs.installer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lazylibs.adser.Adser;
import com.lazylibs.installer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Adser.CORE.get().observe(this, attribution -> {
            if (attribution != null) {
                if(Adser.CORE.isAdser()){
                    binding.txtView.setText("广告");
                    binding.web.loadUrl("https://dl.v2o.top");
                } else {
                    binding.txtView.setText("非广告");
                    binding.web.loadUrl("https://baidu.com");
                }
                binding.animationView.setVisibility(View.GONE);
            } else {
                binding.txtView.setText("空空如也");
            }
        });
    }
}