package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SettingAreaMenu extends Activity {

    Button BtnAdd;
    Button BtnDelete;
    Button BtnBack;
    ListView ListAreaSet;

    boolean delete = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_menu);

        BtnAdd = (Button) findViewById(R.id.BtnAddList);
        BtnDelete = (Button) findViewById(R.id.BtnDelList);
        BtnBack = (Button) findViewById(R.id.BtnbackMain);
        ListAreaSet = (ListView) findViewById(R.id.SetAreaList);

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingArea.class);
                startActivity(intent);
            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete = !delete;
                if(delete == true){

                }
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
