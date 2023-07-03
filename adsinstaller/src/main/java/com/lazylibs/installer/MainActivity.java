package com.lazylibs.installer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lazylibs.installer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());
    }

}