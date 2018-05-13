package com.smu.oss14.bb.weather_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한설정 관련 확인 코드
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //안드로이드 : 마시멜로 이상인 경우
            boolean ckPermission = checkPermission();
            if (ckPermission == true){
                //권한 설정 승인되어 있는 경우
            }else{
                //권한 설정 없는 경우
                String[] pms = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(pms,11);
            }
        }else{
            //안드로이드 : 마시멜로우 하위 버전인 경우
        }

    }

    //위치권한 허용 여부 확인
    private boolean checkPermission(){
        int locationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if(locationPermission == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 11){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
