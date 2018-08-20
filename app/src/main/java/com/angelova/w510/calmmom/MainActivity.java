package com.angelova.w510.calmmom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.angelova.w510.calmmom.adapters.TipsAdapter;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mExaminationsItem;
    private LinearLayout mHealthStateItem;
    private LinearLayout mTipsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mExaminationsItem = (LinearLayout) findViewById(R.id.examinations_layout);

        mExaminationsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExaminationsActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
                //finish();
            }
        });

        mHealthStateItem = (LinearLayout) findViewById(R.id.health_state_layout);

        mHealthStateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HealthStateActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mTipsItem = (LinearLayout) findViewById(R.id.tip_layout);

        mTipsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TipsActivity.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                //intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });
    }
}
