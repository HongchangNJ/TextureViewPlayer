package com.han.videodemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.han.videodemo.R;

/**
 * created by HanHongchang
 * 2018/6/28
 */
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.demand_tv).setOnClickListener(view -> startDemandActivity());
        findViewById(R.id.live_tv).setOnClickListener(view -> startLiveActivity());
    }

    private void startDemandActivity() {
        DemandActivity.Companion.startActivity(this);
    }

    private void startLiveActivity() {
        LiveActivity.Companion.startActivity(this);
    }
}
