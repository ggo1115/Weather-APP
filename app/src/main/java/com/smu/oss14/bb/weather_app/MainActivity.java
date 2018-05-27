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


        CheckWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){

                        //ReceiverShortWeather를 통한 날씨파싱시도
                        ReceiveShortWeather ReceiveWeather = new ReceiveShortWeather();
                        //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                        ReceiveWeather.execute("1159068000");
                    }
                }.start();

                txtWeatherParse.setText(getWeather_result());
            }
        });

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

    public String getWeather_result() {
        return weather_result;
    }

    public void setWeather_result(String weather_result) {
        this.weather_result = weather_result;
    }


    //날씨파싱클래스
    public class ReceiveShortWeather extends AsyncTask<String, Void, Void>{

        //Weatherinfo_Data : 날씨 정보 Class
        //날씨정보 클래스를 배열화함(여러시간의 날씨정보 얻기 위함)
        ArrayList<Weatherinfo_Data> weather_Info = new ArrayList<>();


        @Override
        protected Void doInBackground(String... strings) {

            //날씨파싱url + 지역코드
            String urlXML = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=" + strings;

            OkHttpClient client = new OkHttpClient();

            Request req = new Request.Builder().url(urlXML).build();

            Response res = null;

            try{
                res = client.newCall(req).execute();
                weather_Info = parsing(res.body().string());

                String weatherdata = "";
                int i= 0;
                while(i < weather_Info.size()) {
                    weatherdata += "날짜코드 : " + weather_Info.get(i).getDate() + "\n";
                    weatherdata += "시간단위 : " + weather_Info.get(i).getHour() + "\n";
                    weatherdata += "현재온도 : " + weather_Info.get(i).getTemp_cur() + "\n";
                    weatherdata += "최고온도 : " + weather_Info.get(i).getTemp_max() + "\n";
                    weatherdata += "최저온도 : " + weather_Info.get(i).getTemp_min() + "\n";
                    weatherdata += "하늘상태코드 : " + weather_Info.get(i).getSky_state() + "\n";
                    weatherdata += "강수상태코드 : " + weather_Info.get(i).getPty() + "\n";
                    weatherdata += "날씨상태 : " + weather_Info.get(i).getWf() + "\n";
                    weatherdata += "강수확률 : " + weather_Info.get(i).getPop() + "\n";
                    weatherdata += "풍속 : " + weather_Info.get(i).getWs() + "\n";
                    weatherdata += "풍향 : " + weather_Info.get(i).getWd() + "\n";
                    weatherdata += "습도 : " + weather_Info.get(i).getReh() + "\n";
                    weatherdata += "==========================================\n";
                    i++;
                }
                setWeather_result(weatherdata);

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public ArrayList<Weatherinfo_Data> parsing(String xml){
            String tName = "";
            ArrayList<Weatherinfo_Data> entry = new ArrayList<>();

            try{
                XmlPullParserFactory xmlfactory = XmlPullParserFactory.newInstance();
                XmlPullParser parse = xmlfactory.newPullParser();
                parse.setInput(new StringReader(xml));


                //중복값 빼기 위함(중복값 처리시, 데이터의 값들 모두 null이 됨)
                boolean done = false;
                boolean isTag = false;
                boolean isDate = false;
                boolean isHour = false;
                boolean isTmp = false;
                boolean isTmx = false;
                boolean isTmn = false;
                boolean isSky = false;
                boolean isPty = false;
                boolean isWfkor = false;
                boolean isPop = false;
                boolean isWs = false;
                boolean isWd = false;
                boolean isReh = false;

                int eventTp = parse.getEventType();
                int i = 0;

                while(eventTp != XmlPullParser.END_DOCUMENT && !done){
                    switch (eventTp){
                        case XmlPullParser.START_TAG:
                            tName = parse.getName();
                            if(tName.equals("data")){
                                isTag = true;
                                entry.add(new Weatherinfo_Data());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if(isTag == true){
                                if(tName.equals("day") && !isDate) {
                                    entry.get(i).setDate(parse.getText());
                                    isDate = true;
                                }
                                else if(tName.equals("hour") && !isHour) {
                                    entry.get(i).setHour(parse.getText());
                                    isHour = true;
                                }
                                else if(tName.equals("temp") && !isTmp){
                                    entry.get(i).setTemp_cur(parse.getText());
                                    isTmp = true;
                                }
                                else if(tName.equals("tmx") && !isTmx) {
                                    entry.get(i).setTemp_max(parse.getText());
                                    isTmx = true;
                                }
                                else if(tName.equals("tmn") && !isTmn) {
                                    entry.get(i).setTemp_min(parse.getText());
                                    isTmn = true;
                                }
                                else if(tName.equals("sky") && !isSky) {
                                    entry.get(i).setSky_state(parse.getText());
                                    isSky = true;
                                }
                                else if(tName.equals("pty") && !isPty) {
                                    entry.get(i).setPty(parse.getText());
                                    isPty = true;
                                }
                                else if(tName.equals("wfKor") && !isWfkor){
                                    entry.get(i).setWf(parse.getText());
                                    isWfkor = true;
                                }
                                else if(tName.equals("pop") && !isPop) {
                                    entry.get(i).setPop(parse.getText());
                                    isPop = true;
                                }
                                else if(tName.equals("ws") && !isWs) {
                                    entry.get(i).setWs(parse.getText());
                                    isWs = true;
                                }
                                else if(tName.equals("wdKor") && !isWd) {
                                    entry.get(i).setWd(parse.getText());
                                    isWd = true;
                                }
                                else if(tName.equals("reh") && !isReh) {
                                    entry.get(i).setReh(parse.getText());
                                    isReh = true;
                                }

                                //Log.d("result : ", parse.getName() +" - "+parse.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tName = parse.getName();
                            if(tName.equals("data")){
                                i++;
                                isTag = false;
                                isDate = false;
                                isHour = false;
                                isTmp = false;
                                isTmx = false;
                                isTmn = false;
                                isSky = false;
                                isPty = false;
                                isWfkor = false;
                                isPop = false;
                                isWs = false;
                                isWd = false;
                                isReh = false;
                            }
                            break;
                    }
                    eventTp = parse.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("parsing", "완료");
            return entry;
        }
    }

}
