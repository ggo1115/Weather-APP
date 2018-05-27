package com.smu.oss14.bb.weather_app;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class GPSActivity extends Service implements LocationListener
{
    int requestCode = 11;                                           //요청 코드
    boolean usable_FINE_LOCATION = false;                         //ACCESS_FINE_LOCATION 권한 허용 여부 (true : 권한 허용 / false : 권한 거부)
    boolean isGPSEnabled = false;                                  //GPS 사용유무
    boolean isNetworkEnabled = false;                              //NetWork 사용유무
    boolean isGetlc = false;                                        //GPS상태값


    private double lat;                                             //위도
    private double lon;                                             //경도
    private String[] Addr;                                           //
    private String AddrValue;                                       //


    private static final long MINIMUM_DIS = 10;                    //최소 GPS 업데이트 거리 : 10미터
    private static final long MINIMUM_TIME = 60000;                //최소 GPS 업데이트 시간 : 1분(60000ms)

    Activity activity;
    LocationManager locationMng;
    Location location;
    Geocoder geocoder;


    //생성자
    public GPSActivity(Activity activity){
        this.activity = activity;
    }

    //권한 설정 및 위치정보 확인
    public void UseGPS(){

        /*
         1. 버전 확인 및 권한 설정 확인
         */

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 마시멜로 이상 버전

            if(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
                // ACCESS_FINE_LOCATION 권한이 거부되어있는 경우

                final String[] pms = {Manifest.permission.ACCESS_FINE_LOCATION};

                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    // 이전에 거부한 적이 존재하는 경우

                    AlertDialog.Builder aldia = new AlertDialog.Builder(activity);
                    aldia.setTitle("위치 권한 필요")
                            .setMessage("이 기능 이용하기 위해선 \"위치접근\" 권한이 필요합니다.")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        //requestPermissions를 통해 권한 요청( pms - 요청 변수 / 11 - 요청 번호 )
                                        activity.requestPermissions(pms,requestCode);
                                    }
                                }
                            }).create().show();

                    Log.d("check3", "usable_FINE_LOCATION = "+String.valueOf(usable_FINE_LOCATION));
                }
                else {
                    //거부한 적이 없는 즉, 최초 실행인 경우

                    activity.requestPermissions(pms, requestCode);
                    Log.d("check2", "usable_FINE_LOCATION = "+String.valueOf(usable_FINE_LOCATION));
                }
            }else{
                //Toast.makeText(activity, "권한 설정됨", Toast.LENGTH_SHORT).show();
                Log.d("allow","권한 설정 완료");
                usable_FINE_LOCATION = true;
                Log.d("check1", "usable_FINE_LOCATION = "+String.valueOf(usable_FINE_LOCATION));
            }
        }else{
            //Toast.makeText(activity, "마시멜로 이전 버전", Toast.LENGTH_SHORT).show();
            Log.d("deny", "권한 거부");
            usable_FINE_LOCATION = true;
        }

        /* 2. GPS 사용 */


        //ACCESS_FINE_LOCATION이 허용되었을 때만 실행가능
        if(usable_FINE_LOCATION == true){

            try{
                locationMng = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

                //GPS 사용유무 정보 가져옴
                isGPSEnabled = locationMng.isProviderEnabled(LocationManager.GPS_PROVIDER);

                //NetWork 사용유무 정보 가져옴
                isNetworkEnabled = locationMng.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                //GPS 또는 NetWork 사용 가능할 때
                if(isNetworkEnabled || isGPSEnabled) {

                    //GPS 상태값은 true가 됨
                    this.isGetlc = true;

                    //네트워크로부터 위치값 받아옴
                    if (isNetworkEnabled) {
                        locationMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME, MINIMUM_DIS, this);

                        if (locationMng != null) {
                            location = locationMng.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();

                                /* 3. 역지오코딩 */
                                geocoder = new Geocoder(activity);
                                AddrValue = reverseGeocoding();
                            }
                        }

                    }


                    //GPS로부터 위치값 받아옴
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DIS, this);

                            if (locationMng != null) {
                                location = locationMng.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();

                                    /*  3. 역지오코딩 */

                                    geocoder = new Geocoder(activity);
                                    AddrValue = reverseGeocoding();
                                }
                            }
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //GPS 종료
    public void stopGPS(){
        if(locationMng != null){
            locationMng.removeUpdates(GPSActivity.this);
        }
    }

    public double getLat() {
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isGetlc() {
        return this.isGetlc;
    }

    public void setAddrValue(String value) {
        this.AddrValue = value;
    }

    public String getAddrValue(){
        return this.AddrValue;
    }

    //GPS값을 가져오지 못할 때 즉, GPS가 꺼져있을 때 설정창으로 이동할 수 있게 해주는 alert 창 띄우는 method
    public void SettingAlert(){
       AlertDialog.Builder alDialog = new AlertDialog.Builder(activity);

       alDialog.setTitle("GPS 확인")
               .setMessage("GPS가 켜져있지 않음.\n 설정으로 이동")
               .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       activity.startActivity(intent);
                   }
               }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
       }).show();
    }

    // GPS를 통해 얻어온 위도, 경도 값으로 주소값 받아오는 method
    public String reverseGeocoding(){
        List<Address> addrList = null;

        try{
            addrList = geocoder.getFromLocation(lat, lon, 10);              //위도, 경도값에 대한 결과값 받아옴(최대 10개)
        }catch (IOException e){
            e.printStackTrace();
            Log.e("err", "서버->주소변환시 에러");
        }

        if(addrList != null) {
            if (addrList.size() == 0) {
                Toast.makeText(activity, "주소 없음.", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder AddrSB = new StringBuilder();
                Addr = addrList.get(0).toString().split(" ");                       //공백 기준으로 문자열 자름
                for (int i = 1; i < 4; i++) {
                    AddrSB.append(Addr[i] + ".");                                                 //자른 문자열 StringBuilder 객체에 삽입([1] : 시/도,  [2] : 시/군/구.  [3] : 읍/면/동,  [4] : 리?)
                }
                return AddrSB.toString();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
