package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Select_ForComp extends Activity {

    Spinner spinner1, spinner2, spinner3, spinner4;
    Button BtnSelect;
    boolean SetArea = false;
    String[] select = new String[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_comp);


        final String[] Area_First = {"시/도 선택","서울특별시", "인천광역시", "부산광역시", "대구광역시", "울산광역시", "대전광역시", "광주광역시", "세종특별자치시", "경기도", "강원도", "경상북도", "경상남도", "전라남도", "전라북도", "충청북도", "충청남도", "제주특별자치도"};
        final String[] Area_Select = {"시/군/구 선택"};
        final String[] Area_GangWon = {"시/군/구 선택","강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군", "양양군", "영월군", "원주시", "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군", "홍천군", "화천군", "횡성군"};
        final String[] Area_Gyeongi = {"시/군/구 선택","가평군", "고양시", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시", "성남시", "수원시", "시흥시", "안산시", "안성시", "안양시", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시", "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시", "하남시", "화성시"};
        final String[] Area_Gyeonnam = {"시/군/구 선택","거제시", "거창군", "고성군", "김해시", "남해군", "밀양시", "사천시", "산청군", "양산시", "의령군", "진주시", "창년군", "창원시", "통영시", "하동군", "함안군", "함양군", "합천군"};
        final String[] Area_Gyeongbuk = {"시/군/구 선택","경산시", "경주시", "고령군", "구미시", "군위군", "김천시", "문경시", "봉화군", "상주시", "성주군", "안동시", "영덕군", "영양군", "영주시", "영천시", "예천군", "울릉군", "의성군", "청도군", "청송군", "칠곡군", "포항시"};
        final String[] Area_Gwangju = {"시/군/구 선택","광산구", "남구", "동구", "북구", "서구"};
        final String[] Area_Daegu = {"시/군/구 선택","남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구"};
        final String[] Area_Daejeon = {"시/군/구 선택","대덕구", "동구", "서구", "유성구", "중구"};
        final String[] Area_Busan = {"시/군/구 선택","강서구", "금정구", "기장군", "남구", "동구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구"};
        final String[] Area_Seoul = {"시/군/구 선택","강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
        final String[] Area_Sejong = {""};
        final String[] Area_Ulsan = {"시/군/구 선택","남구", "동구", "북구", "울주군", "중구"};
        final String[] Area_Incheon = {"시/군/구 선택","강화군", "계양구", "남구", "남동구", "동구", "부평구", "서구", "연수구", "옹진군", "중구"};
        final String[] Area_Jeonnam = {"시/군/구 선택","강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시", "담양군", "목포시", "무안군", "보성군", "순천시", "신안군", "여수시", "영광군", "영암군", "완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군"};
        final String[] Area_Jeonbuk = {"시/군/구 선택","고창군", "군산시", "김제시", "남원시", "무주군", "부안군", "순창군", "완주군", "익산시", "임실군", "장수군", "전주시", "정읍시", "진안군"};
        final String[] Area_Jeju = {"시/군/구 선택","서귀포시", "이어도", "제주시"};
        final String[] Area_Chungnam = {"시/군/구 선택","계룡시", "공주시", "금산군", "논산시", "당진시", "보령시", "부여군", "서산시", "서천군", "아산시", "예산군", "천안시", "청양군", "태안군", "홍성군"};
        final String[] Area_Chungbuk = {"시/군/구 선택","괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "제천시", "증편군", "진천군", "청주시", "충주시"};

        Intent ACintent = getIntent();

        spinner1 = (Spinner) findViewById(R.id.FirstArea_1);
        spinner2 = (Spinner) findViewById(R.id.SecondArea_1);
        spinner3 = (Spinner) findViewById(R.id.FirstArea_2);
        spinner4 = (Spinner) findViewById(R.id.SecondArea_2);
        BtnSelect = (Button) findViewById(R.id.Btnselect);

        final ArrayAdapter<String> adapterF,adapterSelect,adapterGW,adapterGG,adapterGN,adapterGB,adapterGJ,adapterDG,adapterDJ,adapterBS,adapterSU,adapterSJ,adapterUS,adapterIC,adapterJN,adapterJB,adapterJJ,adapterCN,adapterCB;
        adapterF = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,Area_First);
        adapterSelect = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, Area_Select);
        adapterGW = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_GangWon);
        adapterGG = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Gyeongi);
        adapterGN = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Gyeonnam);
        adapterGB = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Gyeongbuk);
        adapterGJ = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Gwangju);
        adapterDG = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Daegu);
        adapterDJ = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Daejeon);
        adapterBS = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Busan);
        adapterSU = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Seoul);
        adapterSJ = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Sejong);
        adapterUS = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Ulsan);
        adapterIC = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Incheon);
        adapterJN = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Jeonnam);
        adapterJB = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Jeonbuk);
        adapterJJ = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Jeju);
        adapterCN = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Chungnam);
        adapterCB = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Area_Chungbuk);

        spinner1.setAdapter(adapterF);
        spinner3.setAdapter(adapterF);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    select[0] = null;
                }else{
                    select[0] = Area_First[position];
                }
                if(position == 0) spinner2.setAdapter(adapterSelect);
                else if(position == 1) spinner2.setAdapter(adapterSU);
                else if(position == 2) spinner2.setAdapter(adapterIC);
                else if(position == 3) spinner2.setAdapter(adapterBS);
                else if(position == 4) spinner2.setAdapter(adapterDG);
                else if(position == 5) spinner2.setAdapter(adapterUS);
                else if(position == 6) spinner2.setAdapter(adapterDJ);
                else if(position == 7) spinner2.setAdapter(adapterGJ);
                else if(position == 8) spinner2.setAdapter(adapterSJ);
                else if(position == 9) spinner2.setAdapter(adapterGG);
                else if(position == 10) spinner2.setAdapter(adapterGW);
                else if(position == 11) spinner2.setAdapter(adapterGB);
                else if(position == 12) spinner2.setAdapter(adapterGN);
                else if(position == 13) spinner2.setAdapter(adapterJN);
                else if(position == 14) spinner2.setAdapter(adapterJB);
                else if(position == 15) spinner2.setAdapter(adapterCB);
                else if(position == 16) spinner2.setAdapter(adapterCN);
                else if(position == 17) spinner2.setAdapter(adapterJJ);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(select[0] == Area_First[0]) select[1] = null;
                else if(select[0] == Area_First[1]) select[1] = Area_Seoul[position];
                else if(select[0] == Area_First[2]) select[1] = Area_Incheon[position];
                else if(select[0] == Area_First[3]) select[1] = Area_Busan[position];
                else if(select[0] == Area_First[4]) select[1] = Area_Daegu[position];
                else if(select[0] == Area_First[5]) select[1] = Area_Ulsan[position];
                else if(select[0] == Area_First[6]) select[1] = Area_Daejeon[position];
                else if(select[0] == Area_First[7]) select[1] = Area_Gwangju[position];
                else if(select[0] == Area_First[8]) select[1] = Area_Sejong[position];
                else if(select[0] == Area_First[9]) select[1] = Area_Gyeongi[position];
                else if(select[0] == Area_First[10]) select[1] = Area_GangWon[position];
                else if(select[0] == Area_First[11]) select[1] = Area_Gyeongbuk[position];
                else if(select[0] == Area_First[12]) select[1] = Area_Gyeonnam[position];
                else if(select[0] == Area_First[13]) select[1] = Area_Jeonnam[position];
                else if(select[0] == Area_First[14]) select[1] = Area_Jeonbuk[position];
                else if(select[0] == Area_First[15]) select[1] = Area_Chungbuk[position];
                else if(select[0] == Area_First[16]) select[1] = Area_Chungnam[position];
                else if(select[0] == Area_First[17]) select[1] = Area_Jeju[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    select[2] = null;
                }else{
                    select[2] = Area_First[position];
                }
                if(position == 0) spinner4.setAdapter(adapterSelect);
                else if(position == 1) spinner4.setAdapter(adapterSU);
                else if(position == 2) spinner4.setAdapter(adapterIC);
                else if(position == 3) spinner4.setAdapter(adapterBS);
                else if(position == 4) spinner4.setAdapter(adapterDG);
                else if(position == 5) spinner4.setAdapter(adapterUS);
                else if(position == 6) spinner4.setAdapter(adapterDJ);
                else if(position == 7) spinner4.setAdapter(adapterGJ);
                else if(position == 8) spinner4.setAdapter(adapterSJ);
                else if(position == 9) spinner4.setAdapter(adapterGG);
                else if(position == 10) spinner4.setAdapter(adapterGW);
                else if(position == 11) spinner4.setAdapter(adapterGB);
                else if(position == 12) spinner4.setAdapter(adapterGN);
                else if(position == 13) spinner4.setAdapter(adapterJN);
                else if(position == 14) spinner4.setAdapter(adapterJB);
                else if(position == 15) spinner4.setAdapter(adapterCB);
                else if(position == 16) spinner4.setAdapter(adapterCN);
                else if(position == 17) spinner4.setAdapter(adapterJJ);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(select[2] == Area_First[0]) select[3] = null;
                else if(select[2] == Area_First[1]) select[3] = Area_Seoul[position];
                else if(select[2] == Area_First[2]) select[3] = Area_Incheon[position];
                else if(select[2] == Area_First[3]) select[3] = Area_Busan[position];
                else if(select[2] == Area_First[4]) select[3] = Area_Daegu[position];
                else if(select[2] == Area_First[5]) select[3] = Area_Ulsan[position];
                else if(select[2] == Area_First[6]) select[3] = Area_Daejeon[position];
                else if(select[2] == Area_First[7]) select[3] = Area_Gwangju[position];
                else if(select[2] == Area_First[8]) select[3] = Area_Sejong[position];
                else if(select[2] == Area_First[9]) select[3] = Area_Gyeongi[position];
                else if(select[2] == Area_First[10]) select[3] = Area_GangWon[position];
                else if(select[2] == Area_First[11]) select[3] = Area_Gyeongbuk[position];
                else if(select[2] == Area_First[12]) select[3] = Area_Gyeonnam[position];
                else if(select[2] == Area_First[13]) select[3] = Area_Jeonnam[position];
                else if(select[2] == Area_First[14]) select[3] = Area_Jeonbuk[position];
                else if(select[2] == Area_First[15]) select[3] = Area_Chungbuk[position];
                else if(select[2] == Area_First[16]) select[3] = Area_Chungnam[position];
                else if(select[2] == Area_First[17]) select[3] = Area_Jeju[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                select[3] = null;
            }
        });


        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select[0] == null || select[1] == null || select[2] == null || select[3] == null) {
                    Toast.makeText(getApplicationContext(),"모든 지역을 선택해주십시오",Toast.LENGTH_SHORT).show();
                    spinner1.setAdapter(adapterF);
                    spinner2.setAdapter(adapterSelect);
                    spinner3.setAdapter(adapterF);
                    spinner4.setAdapter(adapterSelect);
                }else{
                    SetArea = true;
                   Intent back = new Intent();
                   back.putExtra("SelectArea1", select[0]);
                   back.putExtra("SelectArea2",select[1]);
                   back.putExtra("SelectArea3",select[2]);
                   back.putExtra("SelectArea4",select[3]);
                   back.putExtra("isSelct", SetArea);
                   setResult(Activity.RESULT_OK, back);
                   finish();
                }
            }
        });

    }
}
