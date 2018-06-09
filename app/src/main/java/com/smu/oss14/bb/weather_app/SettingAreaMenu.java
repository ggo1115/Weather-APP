package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SettingAreaMenu extends Activity {

    private List<String> list_Area;
    Button BtnAdd;
    Button BtnDelete;
    Button BtnBack;
    ListView ListAreaSet;
    BookMarkAdapter bookMarkAdapter;

    String Location1 = "";

    boolean delete = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_menu);

        BtnAdd = (Button) findViewById(R.id.BtnAddList);
        BtnDelete = (Button) findViewById(R.id.BtnDelList);
        BtnBack = (Button) findViewById(R.id.BtnbackMain);
        ListAreaSet = (ListView) findViewById(R.id.SetAreaList);

        list_Area = new ArrayList<>();

        list_Area.add("ab");
        list_Area.add("bb");

        bookMarkAdapter = new BookMarkAdapter(list_Area, this);

        ListAreaSet.setAdapter(bookMarkAdapter);

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingArea.class);
                startActivityForResult(intent, 1020);
            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("Location1", Location1);

        list_Area.add(Location1);
        for(int i = 0 ; i < list_Area.size() ; i++ ){
            if(list_Area.get(i) == ""){
                list_Area.remove(i);
            }
        }

        for (int i = 0 ; i < list_Area.size() ; i++){
            Log.e("list_Area[" + i + "]", list_Area.get(i));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1020){
            if(resultCode == Activity.RESULT_OK){
                Location1 = data.getStringExtra("Location1");
            }
        }
    }
}
