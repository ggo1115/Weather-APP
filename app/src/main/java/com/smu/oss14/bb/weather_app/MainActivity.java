package com.smu.oss14.bb.weather_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    Button CheckWeather; //날씨확인버튼
    Button CheckGPS;    //위치확인버튼
    TextView txtLat;    //위도txt
    TextView txtLon;    //경도txt
    TextView txtAddr;   //주소명txt
    TextView txtWeatherParse;   //날씨파싱정보txt

    String weather_result = ""; //날씨파싱정보 담는 문자열

    Location_Data LData = new Location_Data();
    ArrayList<Weatherinfo_Data> WDataList = new ArrayList<Weatherinfo_Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckWeather = (Button) findViewById(R.id.CheckAD);
        CheckGPS = (Button) findViewById(R.id.gpsBtn);
        txtLat = (TextView) findViewById(R.id.textlat);
        txtLon = (TextView) findViewById(R.id.textLon);
        txtAddr = (TextView) findViewById(R.id.textAddr);
        txtWeatherParse = (TextView) findViewById(R.id.textView);


        CheckGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GPSActivity gpsActivity = new GPSActivity(MainActivity.this);
                LData = gpsActivity.UseGPS();

                if(gpsActivity.usable_FINE_LOCATION) {
                    //usable_FINE_LOCATION일때

                    if (gpsActivity.isGetlc) {
                        //GPS가 켜져있으면
                        //Toast.makeText(MainActivity.this, "GPS켜짐", Toast.LENGTH_SHORT).show();
                        txtLat.setText("위도 : " + LData.getLat());
                        txtLon.setText("경도 : " + LData.getLon());
                        txtAddr.setText("주소 : " + LData.getAddr());

                        String[] Adresult = LData.getAddrValue();

                        final DatabaseReference DBR = FirebaseDatabase.getInstance().getReference().child("LocationCode").child(Adresult[0]).child(Adresult[1]);
                        DBR.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                LData.setLCcode(dataSnapshot.getValue(String.class));
                                Toast.makeText(MainActivity.this, LData.getLCcode(), Toast.LENGTH_SHORT).show();
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


        });

        CheckWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        //ReceiverShortWeather를 통한 날씨파싱시도
                        ReceiveWeather ReceiveWeather = new ReceiveWeather();
                        //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                        Response response = ReceiveWeather.XMLloading("1159068000");
                        try {
                            WDataList = ReceiveWeather.parsing(response.body().string());
                            int i= 0;
                            while(i < WDataList.size()) {
                                weather_result += "날짜코드 : " + WDataList.get(i).getDate() + "\n";
                                weather_result += "시간단위 : " +WDataList.get(i).getHour() + "\n";
                                weather_result += "현재온도 : " + WDataList.get(i).getTemp_cur() + "\n";
                                weather_result += "최고온도 : " + WDataList.get(i).getTemp_max() + "\n";
                                weather_result += "최저온도 : " + WDataList.get(i).getTemp_min() + "\n";
                                weather_result += "하늘상태코드 : " + WDataList.get(i).getSky_state() + "\n";
                                weather_result += "강수상태코드 : " + WDataList.get(i).getPty() + "\n";
                                weather_result += "날씨상태 : " + WDataList.get(i).getWf() + "\n";
                                weather_result += "강수확률 : " + WDataList.get(i).getPop() + "\n";
                                weather_result += "풍속 : " + WDataList.get(i).getWs() + "\n";
                                weather_result += "풍향 : " + WDataList.get(i).getWd() + "\n";
                                weather_result += "습도 : " + WDataList.get(i).getReh() + "\n";
                                weather_result += "==========================================\n";
                                i++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

                txtWeatherParse.setText(weather_result);
            }
        });

    }


}
