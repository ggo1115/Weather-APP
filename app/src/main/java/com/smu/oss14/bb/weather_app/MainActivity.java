package com.smu.oss14.bb.weather_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button CheckGPS;
    TextView txtLat;
    TextView txtLon;
    TextView txtAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckGPS = (Button) findViewById(R.id.gpsBtn);
        txtLat = (TextView) findViewById(R.id.textlat);
        txtLon = (TextView) findViewById(R.id.textLon);
        txtAddr = (TextView) findViewById(R.id.textAddr);

        CheckGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GPSActivity gpsActivity = new GPSActivity(MainActivity.this);
                gpsActivity.UseGPS();

                if(gpsActivity.usable_FINE_LOCATION) {
                    //usable_FINE_LOCATION일때

                    if (gpsActivity.isGetlc) {
                        //GPS가 켜져있으면

                        Toast.makeText(MainActivity.this, "GPS켜짐", Toast.LENGTH_SHORT).show();
                        txtLat.setText("위도 : " + gpsActivity.getLat());
                        txtLon.setText("경도 : " + gpsActivity.getLon());
                        txtAddr.setText("주소 : " + gpsActivity.getAddrValue());

                    } else {
                        // GPS가 꺼져있을 때
                        Toast.makeText(MainActivity.this, "GPS꺼짐", Toast.LENGTH_SHORT).show();
                        gpsActivity.SettingAlert();
                    }
                }
            }
        });

    }
}
