package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    Button BtnRe;    //위치확인버튼
    Button BtngtSetting, BtnAreaSetting, BtnAreaComp;
    TextView TxtLo;
    ImageView weatherImage;
    TextView TxtTpC, TxtTpR, TxtTMM, TxtTSn, TxtWind, TxtPer, TxtReh, TxtPm10, TxtPm25;

    private boolean SetTempScale = true;
    private boolean CurLocationOK = true;

    Location_Data LData = new Location_Data();
    Air_Data_PM10 Pm10Data;
    Air_Data_PM25 Pm25Data;



    ArrayList<Weatherinfo_Data> WDataList = new ArrayList<Weatherinfo_Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtngtSetting = (Button) findViewById(R.id.setting);
        BtnAreaSetting = (Button) findViewById(R.id.areaSet);
        BtnAreaComp = (Button) findViewById(R.id.areaComp);
        BtnRe = (Button) findViewById(R.id.Refresh);
        TxtLo = (TextView) findViewById(R.id.Location);
        TxtTpC = (TextView) findViewById(R.id.TempCur);
        TxtTpR = (TextView) findViewById(R.id.TempRange);
        TxtTMM = (TextView) findViewById(R.id.TempMM);
        TxtTSn = (TextView) findViewById(R.id.TempSen);
        TxtWind = (TextView) findViewById(R.id.Wind);
        TxtPer = (TextView) findViewById(R.id.Per);
        TxtReh = (TextView) findViewById(R.id.Reh);
        TxtPm10 = (TextView) findViewById(R.id.AirPM10);
        TxtPm25 = (TextView) findViewById(R.id.AirPM25);
        weatherImage = (ImageView) findViewById(R.id.WeatherImage);

        //날짜
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar yesterday = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        String tday = dateFormat.format(today.getTime());
        String yester = dateFormat.format(yesterday.getTime());

        Log.e("어제/오늘", yester + " / " + tday);


        BtngtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("setTempScale", SetTempScale);
                intent.putExtra("CurLocationOK", CurLocationOK);
                startActivityForResult(intent, 1);
            }
        });

        BtnAreaSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingAreaMenu.class);
                startActivity(intent);
            }
        });
        BtnAreaComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AreaComp.class);
                intent.putExtra("SetTempScale", SetTempScale);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        final Handler handler = new Handler();

        final GPSActivity gpsActivity = new GPSActivity(MainActivity.this);
        LData = gpsActivity.UseGPS();
        if(gpsActivity.usable_FINE_LOCATION) {
            //usable_FINE_LOCATION일때
            if (gpsActivity.isGetlc) {
                //GPS가 켜져있으면
                String[] Adresult = LData.getAddrValue();
                TxtLo.setText(LData.getAddr());
                final DatabaseReference DBR = FirebaseDatabase.getInstance().getReference().child("LocationCode").child(Adresult[0]).child(Adresult[1]);
                DBR.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LData.setLCcode(dataSnapshot.getValue(String.class));

                        new Thread(){
                            public void run(){
                                //ReceiverShortWeather를 통한 날씨파싱시도
                                ReceiveWeather ReceiveWeather = new ReceiveWeather();
                                ReceiveAirPM10 ReceiveAirPm10 = new ReceiveAirPM10();
                                ReceiveAirPM25 ReceiveAirPm25 = new ReceiveAirPM25();

                                //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                                Response response = ReceiveWeather.XMLloading(LData.getLCcode());
                                Response response_10 = ReceiveAirPm10.XMLloading();
                                Response response_25 = ReceiveAirPm25.XMLloading();
                                try {
                                    WDataList = ReceiveWeather.parsing(response.body().string());
                                    Pm10Data = ReceiveAirPm10.parsingPm10(response_10.body().string());
                                    Pm25Data = ReceiveAirPm25.parsingPm25(response_25.body().string());
                                    Double TempRange = Double.parseDouble( WDataList.get(3).getTemp_max())-Double.parseDouble(WDataList.get(3).getTemp_min());
                                    Double TempSens = 13.12 + (0.6215 * Double.parseDouble(WDataList.get(0).getTemp_cur())) - (11.37 * Math.pow(Double.parseDouble(WDataList.get(0).getWs()), 0.16)) + (0.3965 * Math.pow(Double.parseDouble(WDataList.get(0).getWs()), 0.16) * Double.parseDouble(WDataList.get(0).getTemp_cur()));
                                    Double TempCur = Double.parseDouble(WDataList.get(0).getTemp_cur());
                                    Double TempMax = Double.parseDouble(WDataList.get(0).getTemp_max());
                                    Double TempMin = Double.parseDouble(WDataList.get(0).getTemp_min());

                                    if(!SetTempScale){
                                        TempCur = (TempCur * 1.8) + 32;
                                        TempMax = (TempMax * 1.8) + 32;
                                        TempMin = (TempMin * 1.8) + 32;
                                        TempSens = (TempSens * 1.8) + 32;
                                    }
                                    DecimalFormat form = new DecimalFormat("#.#");
                                    Select_Location_Air SLAir = new Select_Location_Air(LData.getAddrValue()[0], Pm10Data, Pm25Data);
                                    String[] AirState = SLAir.ReturnAir();

                                    TxtTpC.setText(TempCur+"º");
                                    TxtTMM.setText(TempMax+"/"+TempMin+"º");
                                    TxtTpR.setText("일교차\n" + (TempMax - TempMin));
                                    TxtTSn.setText("체감\n" + form.format(TempSens)+"º");
                                    TxtWind.setText("풍속\n" + WDataList.get(1).getWs()+"m/s");
                                    TxtReh.setText("습도\n" + WDataList.get(0).getReh()+"%");
                                    TxtPer.setText("강수\n" + WDataList.get(0).getPop()+"%");
                                    TxtPm10.setText("(pm10) " + AirState[0]);
                                    TxtPm25.setText("(pm25) " + AirState[1]);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            switch (WDataList.get(0).getWf()){
                                                case "맑음":
                                                    weatherImage.setImageResource(R.drawable.sunny);
                                                    break;
                                                case "구름 조금":
                                                    weatherImage.setImageResource(R.drawable.little_cloud);
                                                    break;
                                                case "구름 많음":
                                                    weatherImage.setImageResource(R.drawable.many_cloud);
                                                    break;
                                                case "흐림":
                                                    weatherImage.setImageResource(R.drawable.cloudy);
                                                    break;
                                                case "비":
                                                    weatherImage.setImageResource(R.drawable.rainy);
                                                    break;
                                                case "눈/비":
                                                    weatherImage.setImageResource(R.drawable.snowrain);
                                                    break;
                                                case "눈":
                                                    weatherImage.setImageResource(R.drawable.snowing);
                                                    break;
                                                default:
                                                    weatherImage.setImageResource(R.drawable.sunny);
                                            }
                                        }
                                    });



                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            } else {
                // GPS가 꺼져있을 때
                Toast.makeText(MainActivity.this, "GPS꺼짐", Toast.LENGTH_SHORT).show();
                gpsActivity.SettingAlert();
            }
        }
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                SetTempScale = data.getBooleanExtra("setTempScale", true);
                CurLocationOK = data.getBooleanExtra("CurLocationOK", true);
                Log.e("화씨섭씨?","선택결과 - " + SetTempScale);
                Log.e("현재위치사용?", "선택결과 - " + CurLocationOK);
            }
        }
    }

}
