package com.lazylibs.installer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class Enter extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_enter);
        findViewById(android.R.id.content).postDelayed(() -> {
            Log.e("", "2000200020002000200020002000200020002000200020002000");
            startActivity(getNext());
            finish();
        }, 500);
    }

    protected Intent getNext() {
        return new Intent(this, PatosActivity.class);
    }
}
