package com.example.aaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ExplainActivity extends AppCompatActivity {

    private static final String TAG = "ExplainActivity";

    private TextView explane;
    private ConstraintLayout cl;
    private Animation animation;

    public BluetoothService bluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explane);

        explane = findViewById(R.id.textView);
        cl = findViewById(R.id.cl);
        animation = AnimationUtils.loadAnimation(this, R.anim.btnfade);

        explane.setAnimation(animation);
        explane.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(ExplainActivity.this, ListActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        cl.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(ExplainActivity.this, ListActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        bluetoothService = ((MainActivity) MainActivity.context).bluetoothService;
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
