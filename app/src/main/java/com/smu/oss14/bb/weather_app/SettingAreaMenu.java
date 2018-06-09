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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingAreaMenu extends Activity {

    Button BtnAdd;
    Button BtnDelete;
    Button BtnBack;


    ArrayList<String> items;
    ListView ListAreaSet;
    ArrayAdapter<String> adapter;

    boolean selectArea = false;
    boolean selectGPS = false;
    boolean gpsOn = true;
    boolean additem = false;

    String Location1;
    String selectLocation;

    boolean delete = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_menu);

        BtnAdd = (Button) findViewById(R.id.BtnAddList);
        BtnDelete = (Button) findViewById(R.id.BtnDelList);
        BtnBack = (Button) findViewById(R.id.BtnbackMain);
        ListAreaSet = (ListView) findViewById(R.id.SetAreaList);

        Intent Mainintent = getIntent();
        selectLocation = Mainintent.getStringExtra("SelectArea");
        gpsOn = Mainintent.getBooleanExtra("CurLocationOK", true);
        selectGPS = Mainintent.getBooleanExtra("isSelectCurLocation",true);
        String[] BookMark = Mainintent.getStringArrayExtra("BookmarkList");

        items = new ArrayList<String>();

        for(int i = 0 ; i < BookMark.length ; i++){
            items.add(BookMark[i]);
        }
        /*if(gpsOn){
            items.add("현재 위치");
        }*/

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        ListAreaSet.setAdapter(adapter);

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingArea.class);
                startActivityForResult(intent, 1020);
            }
        });

        ListAreaSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] Bookmark = new String[items.size()];
                for(int i = 0 ; i < items.size() ; i++){
                    Bookmark[i] = items.get(i);
                }
                selectArea = true;

                if(items.get(position).equals("현재위치")){
                    selectGPS = true;
                }else{
                    selectGPS = false;
                }
                Intent selectIntent = new Intent();
                selectIntent.putExtra("BookmarkList", Bookmark);
                selectIntent.putExtra("isSelect", selectArea);
                selectIntent.putExtra("isSelectCurLocation", selectGPS);
                selectIntent.putExtra("selected", items.get(position));
                setResult(Activity.RESULT_OK, selectIntent);
                finish();
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] Bookmark = new String[items.size()];
                for(int i = 0 ; i < items.size() ; i++){
                    Bookmark[i] = items.get(i);
                }
                selectArea = false;
                Intent selectIntent = new Intent();
                selectIntent.putExtra("BookmarkList", Bookmark);
                selectIntent.putExtra("isSelect", selectArea);
                selectIntent.putExtra("isSelectCurLocation", selectGPS);
                selectIntent.putExtra("selected", selectLocation);
                setResult(Activity.RESULT_OK, selectIntent);

                finish();
            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        for(int i = 0; i < items.size() ; i++){
            Log.e("items("+i+")", items.get(i));
        }

        if(additem){
            adapter.notifyDataSetChanged();
            additem = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1020){
            if(resultCode == Activity.RESULT_OK){
                Location1 = data.getStringExtra("Location1");
                int count = 0;
                for(int i = 0 ; i < items.size() ; i++){
                    if(Location1.equals(items.get(i))){
                        count++;
                    }
                }
                if(count == 0) {
                    items.add(Location1);
                    additem = true;
                }else{
                    Toast.makeText(getApplicationContext(), "이미 추가된 지역입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
