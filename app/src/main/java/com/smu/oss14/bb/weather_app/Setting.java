package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Setting extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
    public void onClick3(View view){
        finish();
    }
}
