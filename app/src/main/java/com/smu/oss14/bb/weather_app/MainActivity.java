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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한설정 관련 확인 코드(버전 확인)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //안드로이드 : 마시멜로 이상인 경우

            /* 위치접근권한 허용되어있는지 확인*/
            int ckPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (ckPermission == PackageManager.PERMISSION_GRANTED){
                //권한 설정 승인되어 있는 경우

                /*
                위치 이용 코드
                 */

            }else{
                //권한 설정 없는 경우

                final String[] pms = {Manifest.permission.ACCESS_FINE_LOCATION};

                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    //권한 요청 거부한 적 있는 경우

                    AlertDialog.Builder aldia = new AlertDialog.Builder(MainActivity.this);
                    aldia.setTitle("위치 권한 필요")
                            .setMessage("이 기능 이용하기 위해선 \"위치접근\" 권한이 필요합니다.")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        //requestPermissions를 통해 권한 요청( pms - 요청 변수 / 11 - 요청 번호 )
                                        requestPermissions(pms,11);
                                    }
                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "권한을 허용하지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
                }else{
                    //최초 요청

                    //requestPermissions를 통해 권한 요청( pms - 요청 변수 / 11 - 요청 번호 )
                    requestPermissions(pms, 11);
                }


            }
        }else{
            //안드로이드 : 마시멜로우 하위 버전인 경우 -> 즉시 실행
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

    //RequestPermissions에 의하여 호출되어 권한을 요청했을 때의 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //요청번호가 11일 때, 요청결과가 허용인 경우
        if(requestCode == 11){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //실행
            }else{
                Toast.makeText(MainActivity.this, "권한요청을 거부함", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
