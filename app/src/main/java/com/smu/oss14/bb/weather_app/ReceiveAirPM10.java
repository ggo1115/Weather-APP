package com.smu.oss14.bb.weather_app;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReceiveAirPM10 extends AsyncTask<String, Void, Void> {

    Air_Data_PM10 airPm10 = new Air_Data_PM10();

    @Override
    protected Void doInBackground(String... strings) {
        String url_Pm10 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM10&dataGubun=HOUR&searchCondition=MONTH&pageNo=1&numOfRows=1&ServiceKey=rvk%2BObqxNvIveKYvDs%2BikDrwJHgTskdxI78YbjCFKzb2hgUh3vvGq1w6e9kf3rYfvMKobvSP9P6xUSsdaiXMcA%3D%3D";
        //String url_Pm25 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM25&dataGubun=HOUR&searchCondition=MONTH&pageNo=1&numOfRows=1&ServiceKey=rvk%2BObqxNvIveKYvDs%2BikDrwJHgTskdxI78YbjCFKzb2hgUh3vvGq1w6e9kf3rYfvMKobvSP9P6xUSsdaiXMcA%3D%3D";

        OkHttpClient client = new OkHttpClient();
        Request req_pm10 = new Request.Builder().url(url_Pm10).build();
        Response res_pm10 = null;

        try{
            res_pm10 = client.newCall(req_pm10).execute();
            airPm10 = parsingPm10(res_pm10.body().string());

            String result = "";
            result += "서울 : " + airPm10.getSeoul_PM10() +"\n";
            result += "부산 : " + airPm10.getBusan_PM10() +"\n";
            result += "대구 : " + airPm10.getDaegu_PM10() +"\n";
            result += "인천 : " + airPm10.getIncheon_PM10() +"\n";
            result += "광주 : " + airPm10.getGwangju_PM10() +"\n";
            result += "대전 : " + airPm10.getDaejeon_PM10() +"\n";
            result += "울산 : " + airPm10.getUlsan_PM10() +"\n";
            result += "경기 : " + airPm10.getGyeonggi_PM10() +"\n";
            result += "강원 : " + airPm10.getGangwon_PM10() +"\n";
            result += "충북 : " + airPm10.getChungbuk_PM10() +"\n";
            result += "충남 : " + airPm10.getChungnam_PM10() +"\n";
            result += "전북 : " + airPm10.getJeonBuk_PM10() +"\n";
            result += "전남 : " + airPm10.getJeonnam_PM10() +"\n";
            result += "경북 : " + airPm10.getGyeongbuk_PM10() +"\n";
            result += "경남 : " + airPm10.getGyeongnam_PM10() +"\n";
            result += "제주 : " + airPm10.getJeju_PM10() +"\n";
            result += "세종 : " + airPm10.getSejong_PM10() +"\n";

            Log.e("Pm10 result : ", result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public Air_Data_PM10 parsingPm10(String xml){
        String tName = "";

        Air_Data_PM10 airPm10 = null;

        try{
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(new StringReader(xml));

            boolean Done = false;
            boolean isTag = false;
            boolean isSeoul = false;
            boolean isBusan = false;
            boolean isDaegu = false;
            boolean isIncheon = false;
            boolean isGwangju = false;
            boolean isDaejeon = false;
            boolean isUlsan = false;
            boolean isGyeongi = false;
            boolean isGangwon = false;
            boolean isChungbuk = false;
            boolean isChungnam = false;
            boolean isJeonbuk = false;
            boolean isJeonnam = false;
            boolean isGyeongbuk = false;
            boolean isGyeongnam = false;
            boolean isJeju = false;
            boolean isSejong = false;

            int eventTp = parser.getEventType();

            while(eventTp != XmlPullParser.END_DOCUMENT && !Done){
                switch (eventTp){
                    case XmlPullParser.START_TAG:
                        tName = parser.getName();
                        if(tName.equals("item")){
                            isTag = true;
                            airPm10 = new Air_Data_PM10();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(isTag == true){
                            if(tName.equals("seoul") && !isSeoul) {
                                airPm10.setSeoul_PM10(parser.getText());
                                isSeoul = true;
                            }
                            else if(tName.equals("busan") && !isBusan) {
                                airPm10.setBusan_PM10(parser.getText());
                                isBusan = true;
                            }
                            else if(tName.equals("daegu") && !isDaegu){
                                airPm10.setDaegu_PM10(parser.getText());
                                isDaegu = true;
                            }
                            else if(tName.equals("incheon") && !isIncheon) {
                                airPm10.setIncheon_PM10(parser.getText());
                                isIncheon = true;
                            }
                            else if(tName.equals("gwangju") && !isGwangju) {
                                airPm10.setGwangju_PM10(parser.getText());
                                isGwangju = true;
                            }
                            else if(tName.equals("daejeon") && !isDaejeon) {
                                airPm10.setDaejeon_PM10(parser.getText());
                                isDaejeon = true;
                            }
                            else if(tName.equals("ulsan") && !isUlsan) {
                                airPm10.setUlsan_PM10(parser.getText());
                                isUlsan = true;
                            }
                            else if(tName.equals("gyeonggi") && !isGyeongi){
                                airPm10.setGyeonggi_PM10(parser.getText());
                                isGyeongi = true;
                            }
                            else if(tName.equals("gangwon") && !isGangwon) {
                                airPm10.setGangwon_PM10(parser.getText());
                                isGangwon = true;
                            }
                            else if(tName.equals("chungbuk") && !isChungbuk) {
                                airPm10.setChungbuk_PM10(parser.getText());
                                isChungbuk = true;
                            }
                            else if(tName.equals("chungnam") && !isChungnam) {
                                airPm10.setChungnam_PM10(parser.getText());
                                isChungnam = true;
                            }
                            else if(tName.equals("jeonbuk") && !isJeonbuk) {
                                airPm10.setJeonBuk_PM10(parser.getText());
                                isJeonbuk = true;
                            }
                            else if(tName.equals("jeonnam") && !isJeonnam) {
                                airPm10.setJeonnam_PM10(parser.getText());
                                isJeonnam = true;
                            }
                            else if(tName.equals("gyeongbuk") && !isGyeongbuk) {
                                airPm10.setGyeongbuk_PM10(parser.getText());
                                isGyeongbuk = true;
                            }
                            else if(tName.equals("gyeongnam") && !isGyeongnam) {
                                airPm10.setGyeongnam_PM10(parser.getText());
                                isGyeongnam = true;
                            }
                            else if(tName.equals("jeju") && !isJeju) {
                                airPm10.setJeju_PM10(parser.getText());
                                isJeju = true;
                            }
                            else if(tName.equals("sejong") && !isSejong) {
                                airPm10.setSejong_PM10(parser.getText());
                                isSejong = true;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tName = parser.getName();
                        if(tName.equals("item")){
                            isTag = false;
                            isSeoul = false;
                            isBusan = false;
                            isDaegu = false;
                            isIncheon = false;
                            isGwangju = false;
                            isDaejeon = false;
                            isUlsan = false;
                            isGyeongi = false;
                            isGangwon = false;
                            isChungbuk = false;
                            isChungnam = false;
                            isJeonbuk = false;
                            isJeonnam = false;
                            isGyeongbuk = false;
                            isGyeongnam = false;
                            isJeju = false;
                            isSejong = false;
                        }
                        break;
                }
                eventTp = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("parsing", "완료");
        return airPm10;
    }

    public Response XMLloading(){
        String urlXML = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM10&dataGubun=HOUR&searchCondition=MONTH&pageNo=1&numOfRows=1&ServiceKey=rvk%2BObqxNvIveKYvDs%2BikDrwJHgTskdxI78YbjCFKzb2hgUh3vvGq1w6e9kf3rYfvMKobvSP9P6xUSsdaiXMcA%3D%3D";

        OkHttpClient client = new OkHttpClient();

        Request req_pm10 = new Request.Builder().url(urlXML).build();

        Response response = null;

        try{
            response = client.newCall(req_pm10).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
