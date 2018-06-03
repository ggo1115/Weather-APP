package com.smu.oss14.bb.weather_app;

import java.io.IOException;

import okhttp3.Response;

//선택된 지역 or 위치하고 있는 지역에 대한 미세먼지 정보 불러오기
public class Select_Location_Air {

    String CNP; //시/도 값 저장
    String PM10;
    String PM25;
    Air_Data_PM10 AD10;
    Air_Data_PM25 AD25;

    public Select_Location_Air(String CNP, Air_Data_PM10 airDataPm10, Air_Data_PM25 airDataPm25){
        this.CNP = CNP;
        this.AD10 = airDataPm10;
        this.AD25 = airDataPm25;

    }

    public String[] ReturnAir(){

        switch (CNP){
            case "서울특별시" :
                PM10 = AD10.getSeoul_PM10();
                PM25 = AD25.getSeoul_PM25();
                break;
            case "부산광역시" :
                PM10 = AD10.getBusan_PM10();
                PM25 = AD25.getBusan_PM25();
                break;
            case "대구광역시" :
                PM10 = AD10.getDaegu_PM10();
                PM25 = AD25.getDaegu_PM25();
                break;
            case "광주광역시" :
                PM10 = AD10.getGwangju_PM10();
                PM25 = AD25.getGwangju_PM25();
                break;
            case "대전광역시" :
                PM10 = AD10.getDaejeon_PM10();
                PM25 = AD25.getDaejeon_PM25();
                break;
            case "울산광역시" :
                PM10 = AD10.getUlsan_PM10();
                PM25 = AD25.getUlsan_PM25();
                break;
            case "경기도" :
                PM10 = AD10.getGyeonggi_PM10();
                PM25 = AD25.getGyeonggi_PM25();
                break;
            case "강원도" :
                PM10 = AD10.getGangwon_PM10();
                PM25 = AD25.getGangwon_PM25();
                break;
            case "충청북도" :
                PM10 = AD10.getChungbuk_PM10();
                PM25 = AD25.getChungbuk_PM25();
                break;
            case "충청남도" :
                PM10 = AD10.getChungnam_PM10();
                PM25 = AD25.getChungnam_PM25();
                break;
            case "전라북도" :
                PM10 = AD10.getJeonBuk_PM10();
                PM25 = AD25.getJeonnam_PM25();
                break;
            case "전라남도" :
                PM10 = AD10.getJeonnam_PM10();
                PM25 = AD25.getJeonnam_PM25();
                break;
            case "경상북도" :
                PM10 = AD10.getGyeongbuk_PM10();
                PM25 = AD25.getGyeongbuk_PM25();
                break;
            case "경상남도" :
                PM10 = AD10.getGyeongnam_PM10();
                PM25 = AD25.getGyeongnam_PM25();
                break;
            case "제주도" :
                PM10 = AD10.getJeju_PM10();
                PM25 = AD25.getJeju_PM25();
                break;
            case "세종특별자치시" :
                PM10 = AD10.getSejong_PM10();
                PM25 = AD25.getSejong_PM25();
                break;
        }

        String[] Air10N25 = new String[]{PM10, PM25};

        return Air10N25;
    }
}
