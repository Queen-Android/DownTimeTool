package com.downtimetool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.downtimetool.view.TimeButton;

public class CustomTimeActivity extends AppCompatActivity {
    private TimeButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_time);
        button = (TimeButton) findViewById(R.id.send_code);
        button.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        button.onDestroy();
    }
}
