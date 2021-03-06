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
    boolean isSetAlarm;
    String SetAlarmWay;
    String[] SetAlarmContent;
    int AlarmHour;
    int AlarmMinute;

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
        isSetAlarm = mainIntent.getBooleanExtra("isSetAlarm", true);
        SetAlarmWay = mainIntent.getStringExtra("SetAlarmWay");
        SetAlarmContent = mainIntent.getStringArrayExtra("SetAlarmContent");
        AlarmHour = mainIntent.getIntExtra("AlarmHour", 7);
        AlarmMinute = mainIntent.getIntExtra("AlarmMinute", 0);

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
                        Intent intent_alarm = new Intent(getApplicationContext(), SettingAlarm.class);
                        intent_alarm.putExtra("isSetAlarm", isSetAlarm);
                        intent_alarm.putExtra("SetAlarmWay", SetAlarmWay);
                        intent_alarm.putExtra("SetAlarmContent", SetAlarmContent);
                        intent_alarm.putExtra("AlarmHour", AlarmHour);
                        intent_alarm.putExtra("AlarmMinute", AlarmMinute);
                        startActivityForResult(intent_alarm, 1040);
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
                backMain.putExtra("isSetAlarm", isSetAlarm);
                backMain.putExtra("SetAlarmWay", SetAlarmWay);
                backMain.putExtra("SetAlarmContent", SetAlarmContent);
                backMain.putExtra("AlarmHour", AlarmHour);
                backMain.putExtra("AlarmMinute", AlarmMinute);
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

        if(requestCode == 1040){
            if(resultCode == Activity.RESULT_OK){
                isSetAlarm = data.getBooleanExtra("isSetAlarm", true);
                SetAlarmWay = data.getStringExtra("SetAlarmWay");
                SetAlarmContent = data.getStringArrayExtra("SetAlarmContent");
                AlarmHour = data.getIntExtra("AlarmHour", 7);
                AlarmMinute = data.getIntExtra("AlarmMinute", 0);
            }
        }
    }
}
