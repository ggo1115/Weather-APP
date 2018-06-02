package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SettingArea extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_s);
    }
    public void onClick2(View view){
        finish();
    }
}
