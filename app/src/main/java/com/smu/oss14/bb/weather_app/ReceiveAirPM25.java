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

public class ReceiveAirPM25  extends AsyncTask<String, Void, Void> {

    Air_Data_PM25 airPm25 = new Air_Data_PM25();

    @Override
    protected Void doInBackground(String... strings) {
        String url_Pm25 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM25&dataGubun=HOUR&searchCondition=MONTH&pageNo=1&numOfRows=1&ServiceKey=rvk%2BObqxNvIveKYvDs%2BikDrwJHgTskdxI78YbjCFKzb2hgUh3vvGq1w6e9kf3rYfvMKobvSP9P6xUSsdaiXMcA%3D%3D";

        OkHttpClient client = new OkHttpClient();
        Request req_pm25 = new Request.Builder().url(url_Pm25).build();
        Response res_pm25 = null;

        try{
            res_pm25 = client.newCall(req_pm25).execute();
            airPm25 = parsingPm25(res_pm25.body().string());

            String result = "";
            result += "서울 : " + airPm25.getSeoul_PM25() +"\n";
            result += "부산 : " + airPm25.getBusan_PM25() +"\n";
            result += "대구 : " + airPm25.getDaegu_PM25() +"\n";
            result += "인천 : " + airPm25.getIncheon_PM25() +"\n";
            result += "광주 : " + airPm25.getGwangju_PM25() +"\n";
            result += "대전 : " + airPm25.getDaejeon_PM25() +"\n";
            result += "울산 : " + airPm25.getUlsan_PM25() +"\n";
            result += "경기 : " + airPm25.getGyeonggi_PM25() +"\n";
            result += "강원 : " + airPm25.getGangwon_PM25() +"\n";
            result += "충북 : " + airPm25.getChungbuk_PM25() +"\n";
            result += "충남 : " + airPm25.getChungnam_PM25() +"\n";
            result += "전북 : " + airPm25.getJeonBuk_PM25() +"\n";
            result += "전남 : " + airPm25.getJeonnam_PM25() +"\n";
            result += "경북 : " + airPm25.getGyeongbuk_PM25() +"\n";
            result += "경남 : " + airPm25.getGyeongnam_PM25() +"\n";
            result += "제주 : " + airPm25.getJeju_PM25() +"\n";
            result += "세종 : " + airPm25.getSejong_PM25() +"\n";

            Log.e("Pm25 result : ", result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public Air_Data_PM25 parsingPm25(String xml){
        String tName = "";

        Air_Data_PM25 airPm25 = null;

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
                            airPm25 = new Air_Data_PM25();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(isTag == true){
                            if(tName.equals("seoul") && !isSeoul) {
                                airPm25.setSeoul_PM25(parser.getText());
                                isSeoul = true;
                            }
                            else if(tName.equals("busan") && !isBusan) {
                                airPm25.setBusan_PM25(parser.getText());
                                isBusan = true;
                            }
                            else if(tName.equals("daegu") && !isDaegu){
                                airPm25.setDaegu_PM25(parser.getText());
                                isDaegu = true;
                            }
                            else if(tName.equals("incheon") && !isIncheon) {
                                airPm25.setIncheon_PM25(parser.getText());
                                isIncheon = true;
                            }
                            else if(tName.equals("gwangju") && !isGwangju) {
                                airPm25.setGwangju_PM25(parser.getText());
                                isGwangju = true;
                            }
                            else if(tName.equals("daejeon") && !isDaejeon) {
                                airPm25.setDaejeon_PM25(parser.getText());
                                isDaejeon = true;
                            }
                            else if(tName.equals("ulsan") && !isUlsan) {
                                airPm25.setUlsan_PM25(parser.getText());
                                isUlsan = true;
                            }
                            else if(tName.equals("gyeonggi") && !isGyeongi){
                                airPm25.setGyeonggi_PM25(parser.getText());
                                isGyeongi = true;
                            }
                            else if(tName.equals("gangwon") && !isGangwon) {
                                airPm25.setGangwon_PM25(parser.getText());
                                isGangwon = true;
                            }
                            else if(tName.equals("chungbuk") && !isChungbuk) {
                                airPm25.setChungbuk_PM25(parser.getText());
                                isChungbuk = true;
                            }
                            else if(tName.equals("chungnam") && !isChungnam) {
                                airPm25.setChungnam_PM25(parser.getText());
                                isChungnam = true;
                            }
                            else if(tName.equals("jeonbuk") && !isJeonbuk) {
                                airPm25.setJeonBuk_PM25(parser.getText());
                                isJeonbuk = true;
                            }
                            else if(tName.equals("jeonnam") && !isJeonnam) {
                                airPm25.setJeonnam_PM25(parser.getText());
                                isJeonnam = true;
                            }
                            else if(tName.equals("gyeongbuk") && !isGyeongbuk) {
                                airPm25.setGyeongbuk_PM25(parser.getText());
                                isGyeongbuk = true;
                            }
                            else if(tName.equals("gyeongnam") && !isGyeongnam) {
                                airPm25.setGyeongnam_PM25(parser.getText());
                                isGyeongnam = true;
                            }
                            else if(tName.equals("jeju") && !isJeju) {
                                airPm25.setJeju_PM25(parser.getText());
                                isJeju = true;
                            }
                            else if(tName.equals("sejong") && !isSejong) {
                                airPm25.setSejong_PM25(parser.getText());
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
        return airPm25;
    }

    public Response XMLloading(){
        String urlXML = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM25&dataGubun=HOUR&searchCondition=MONTH&pageNo=1&numOfRows=1&ServiceKey=rvk%2BObqxNvIveKYvDs%2BikDrwJHgTskdxI78YbjCFKzb2hgUh3vvGq1w6e9kf3rYfvMKobvSP9P6xUSsdaiXMcA%3D%3D";

        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder().url(urlXML).build();

        Response response = null;

        try{
            response = client.newCall(req).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
