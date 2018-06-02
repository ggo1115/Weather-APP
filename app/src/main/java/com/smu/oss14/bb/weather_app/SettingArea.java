package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_s);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

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
        list.add("6v6");
        list.add("ㅎㅅㅎ");
        list.add("'_'*");
        list.add("^▽^");
        list.add("ㅍ_ㅍ");
    }

    public void onClick2(View view){
        finish();
    }


}
