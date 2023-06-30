package com.lazylibs.installer;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.lazylibs.adser.Adser;
import com.lazylibs.adsplasher.AdvertModel;
import com.lazylibs.adsplasher.IViewFlipperMainHandler;
import com.lazylibs.adsplasher.ViewFlipperMainHolder;
import com.lazylibs.installer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());
    }

}