package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SettingArea extends Activity {

    private List<String> list;           //데이터를 넣은 리스트변수
    private ListView listView;          //검색을 보여줄 리스트변수
    private EditText editSearch;        //검색어를 입력할 Input창
    private SearchAdapter adapter;      //리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    private Button BtnOk;
    private Button BtnBack;

    private String AddLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_s);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);
        BtnOk = (Button) findViewById(R.id.BtnOk_SA);
        BtnBack = (Button) findViewById(R.id.BtnBack_SA);

        Intent intent = getIntent();


        //리스트 생성
        list = new ArrayList<String>();

        //검색에 사용할 데이터를 미리 저장
        settingList();

        //리스트의 모든 데이터를 arraylist에 복사/ list 복사본을 만듬
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        //리스트에 연동될 어답터를 생성
        adapter = new SearchAdapter(list, this);

        listView.setAdapter(adapter);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //input창에 문자를 입력할 때마다 호출
                //search 메소드를 호출
                String text = editSearch.getText().toString();
                search(text);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editSearch.setText(list.get(position));
                AddLocation = editSearch.getText().toString();
            }
        });

        BtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                backIntent.putExtra("Location1", AddLocation);
                setResult(Activity.RESULT_OK, backIntent);
                finish();
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //검색을 수행한는 메소드
    private void search(String charText) {
        list.clear();                         //문자 입력시마다 리스트 지우고 새로
        if(charText.length() == 0){           //문자 입력이 없을때는 모든 데이터를 보여줌
            list.addAll(arraylist);
        }else{                               //문자 입력할때
            for (int i = 0; i < arraylist.size(); i++){
                if(arraylist.get(i).toLowerCase().contains(charText)){
                    list.add(arraylist.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    //검색에 사용될 데이터를 리스트에 추가.(일단은 임의로 추가했음..나중에 지역으로)
    private void settingList() {
        list.add("강원도 강릉시");    list.add("강원도 고성군");    list.add("강원도 동해시");    list.add("강원도 삼척시");    list.add("강원도 속초시");    list.add("강원도 양구군");
        list.add("강원도 양양군");    list.add("강원도 영월군");    list.add("강원도 원주시");    list.add("강원도 인제군");    list.add("강원도 정선군");    list.add("강원도 철원군");
        list.add("강원도 춘천시");    list.add("강원도 태백시");    list.add("강원도 평창군");    list.add("강원도 홍천군");    list.add("강원도 화천군");    list.add("강원도 횡성군");
        list.add("경기도 가평군");    list.add("경기도 고양시");    list.add("경기도 과천시");    list.add("경기도 광명시");    list.add("경기도 광주시");    list.add("경기도 구리시");
        list.add("경기도 군포시");    list.add("경기도 김포시");    list.add("경기도 남양주시");  list.add("경기도 동두천시");  list.add("경기도 부천시");    list.add("경기도 성남시");
        list.add("경기도 수원시");    list.add("경기도 시흥시");    list.add("경기도 안산시");    list.add("경기도 안성시");    list.add("경기도 안양시");    list.add("경기도 양주시");
        list.add("경기도 양평군");    list.add("경기도 여주시");    list.add("경기도 연천군");    list.add("경기도 오산시");    list.add("경기도 용인시");    list.add("경기도 의왕시");
        list.add("경기도 의정부시");  list.add("경기도 이천시");    list.add("경기도 파주시");    list.add("경기도 평택시");    list.add("경기도 포천시");    list.add("경기도 하남시");
        list.add("경기도 화성시");
        list.add("경상남도 거제시");   list.add("경상남도 거창군");   list.add("경상남도 고성군");   list.add("경상남도 김해시");   list.add("경상남도 남해군");   list.add("경상남도 밀양시");
        list.add("경상남도 사천시");   list.add("경상남도 산청군");   list.add("경상남도 양산시");   list.add("경상남도 의령군");   list.add("경상남도 진주시");   list.add("경상남도 창녕군");
        list.add("경상남도 창원시");   list.add("경상남도 통영시");   list.add("경상남도 하동군");   list.add("경상남도 함안군");   list.add("경상남도 함양군");   list.add("경상남도 합천군");
        list.add("경상북도 경산시");   list.add("경상북도 경주시");   list.add("경상북도 고령군");   list.add("경상북도 구미시");   list.add("경상북도 군위군");   list.add("경상북도 김천시");
        list.add("경상북도 문경시");   list.add("경상북도 봉화군");   list.add("경상북도 상주시");   list.add("경상북도 성주군");   list.add("경상북도 안동시");   list.add("경상북도 영덕군");
        list.add("경상북도 영양군");   list.add("경상북도 영주시");   list.add("경상북도 영천시");   list.add("경상북도 예천군");   list.add("경상북도 울릉군");   list.add("경상북도 의성군");
        list.add("경상북도 청도군");   list.add("경상북도 청송군");   list.add("경상북도 칠곡군");   list.add("경상북도 포항시");
        list.add("광주광역시 광산구"); list.add("광주광역시 남구");   list.add("광주광역시 동구");   list.add("광주광역시 북구");   list.add("광주광역시 서구");
        list.add("대구광역시 남구");   list.add("대구광역시 달서구"); list.add("대구광역시 달성군"); list.add("대구광역시 동구");   list.add("대구광역시 북구");   list.add("대구광역시 서구");
        list.add("대구광역시 수성구"); list.add("대구광역시 중구");
        list.add("대전광역시 대덕구"); list.add("대전광역시 동구");   list.add("대전광역시 서구");   list.add("대전광역시 유성구");  list.add("대전광역시 중구");
        list.add("부산광역시 강서구"); list.add("부산광역시 금정구"); list.add("부산광역시 기장군"); list.add("부산광역시 남구");    list.add("부산광역시 동구");   list.add("부산광역시 동래구");
        list.add("부산광역시 부산진구"); list.add("부산광역시 북구");   list.add("부산광역시 사상구");  list.add("부산광역시 사하구");  list.add("부산광역시 서구");   list.add("부산광역시 수영구");
        list.add("부산광역시 연제구");  list.add("부산광역시 영도구");  list.add("부산광역시 중구");   list.add("부산광역시 해운대구");
        list.add("서울특별시 강남구");  list.add("서울특별시 강동구");  list.add("서울특별시 강북구");  list.add("서울특별시 강서구");  list.add("서울특별시 관악구");  list.add("서울특별시 광진구");
        list.add("서울특별시 구로구");  list.add("서울특별시 금천구");  list.add("서울특별시 노원구");  list.add("서울특별시 도봉구");  list.add("서울특별시 동대문구"); list.add("서울특별시 동작구");
        list.add("서울특별시 마포구");  list.add("서울특별시 서대문구"); list.add("서울특별시 서초구");  list.add("서울특별시 성동구");  list.add("서울특별시 성북구");  list.add("서울특별시 송파구");
        list.add("서울특별시 양천구");  list.add("서울특별시 영등포구"); list.add("서울특별시 용산구");  list.add("서울특별시 은평구");  list.add("서울특별시 종로구");  list.add("서울특별시 중구");
        list.add("서울특별시 중랑구");
        list.add("세종특별자치시");
        list.add("울산광역시 남구");   list.add("울산광역시 동구");    list.add("울산광역시 북구");    list.add("울산광역시 울주군");  list.add("울산광역시 중구");
        list.add("인천광역시 강화군"); list.add("인천광역시 계양구");  list.add("인천광역시 남구");    list.add("인천광역시 남동구");  list.add("인천광역시 동구");  list.add("인천광역시 부평구");
        list.add("인천광역시 서구");   list.add("인천광역시 연수구");  list.add("인천광역시 옹진군");  list.add("인천광역시 중구");
        list.add("전라남도 강진군");   list.add("전라남도 고흥군");    list.add("전라남도 곡성군");    list.add("전라남도 광양시");   list.add("전라남도 구례군");   list.add("전라남도 나주시");
        list.add("전라남도 담양군");   list.add("전라남도 목포시");    list.add("전라남도 무안군");    list.add("전라남도 보성군");   list.add("전라남도 순천시");   list.add("전라남도 신안군");
        list.add("전라남도 여수시");   list.add("전라남도 영광군");    list.add("전라남도 영암군");    list.add("전라남도 완도군");   list.add("전라남도 장성군");   list.add("전라남도 장흥군");
        list.add("전라남도 진도군");   list.add("전라남도 함평군");    list.add("전라남도 해남군");    list.add("전라남도 화순군");
        list.add("전라북도 고창군");   list.add("전라북도 군산시");    list.add("전라북도 김제시");    list.add("전라북도 남원시");   list.add("전라북도 무주군");   list.add("전라북도 부안군");
        list.add("전라북도 순창군");   list.add("전라북도 완주군");    list.add("전라북도 익산시");    list.add("전라북도 임실군");   list.add("전라북도 장수군");   list.add("전라북도 전주시");
        list.add("전라북도 정읍시");   list.add("전라북도 진안군");
        list.add("제주특별자치도 서귀포시");   list.add("제주특별자치도 이어도");    list.add("제주특별자치도 제주시");
        list.add("충청남도 계룡시");   list.add("충청남도 공주시");   list.add("충청남도 금산군");   list.add("충청남도 논산시");   list.add("충청남도 당진시");   list.add("충청남도 보령시");
        list.add("충청남도 부여군");   list.add("충청남도 서산시");   list.add("충청남도 서천군");   list.add("충청남도 아산시");   list.add("충청남도 예산군");   list.add("충청남도 천안시");
        list.add("충청남도 청양군");   list.add("충청남도 태안군");   list.add("충청남도 홍성군");
        list.add("충청북도 괴산군");   list.add("충청북도 단양군");   list.add("충청북도 보은군");   list.add("충청북도 영동군");   list.add("충청북도 옥천군");   list.add("충청북도 음성군");
        list.add("충청북도 제천시");   list.add("충청북도 증평군");   list.add("충청북도 진천군");   list.add("충청북도 청주시");   list.add("충청북도 충주시");
    }

}
