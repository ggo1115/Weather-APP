package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AreaComp extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_c);
    }
    public void onClick1(View view){
        finish();
    }
}
