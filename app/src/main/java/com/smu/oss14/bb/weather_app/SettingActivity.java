package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SettingActivity extends Activity {

    ListView setlist;
    Button btnBack;
    boolean setTempScale;
    boolean setCurLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");

        final String[] menu= {"온도 단위","알림 설정","현재 위치 정보 권한 사용"};

        setlist = (ListView) findViewById(R.id.SettingLV);
        btnBack = (Button) findViewById(R.id.BacktoMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        setlist.setAdapter(adapter);

        Intent mainIntent = getIntent();
        setTempScale = mainIntent.getBooleanExtra("setTempScale", true);
        setCurLocation = mainIntent.getBooleanExtra("CurLocationOK", true);

        setlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent_tmp = new Intent(getApplicationContext(), SettingTempScale.class);
                        intent_tmp.putExtra("setTemp", setTempScale);
                        startActivityForResult(intent_tmp, 2);
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent_cur = new Intent(getApplicationContext(), SettingCurLocation.class);
                        intent_cur.putExtra("CurLocationOK", setCurLocation);
                        startActivityForResult(intent_cur, 3);
                        break;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMain = new Intent();
                backMain.putExtra("setTempScale", setTempScale);
                backMain.putExtra("CurLocationOK", setCurLocation);
                setResult(Activity.RESULT_OK, backMain);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                setTempScale = data.getBooleanExtra("check", true);
                Log.e("온도단위설정종료", "result : " + setTempScale);
            }
        }

        if(requestCode == 3){
            if(resultCode == Activity.RESULT_OK){
                setCurLocation = data.getBooleanExtra("CurLocationOK", true);
                Log.e("현재위치정보사용", "result : " + setCurLocation);
            }
        }
    }
}
