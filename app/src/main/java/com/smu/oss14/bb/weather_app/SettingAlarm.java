package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingAlarm extends Activity {

    Switch setAlarm;
    TextView alarmWay, alarmContent, alarmTime;
    View divide1,divide2,divide3,divide4,divide5,divide6,divide7,divide8,divide9,divide10;
    RadioGroup RGroup;
    RadioButton sound, vibe, nomode;
    CheckBox CKRain, CKPM, CKTPR;
    TimePicker timePicker;
    Button BTNOK;


    boolean isSetAlarm;
    String SetAlarmWay;
    String[] SetAlarmContent;
    int AlarmHour;
    int AlarmMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        setAlarm = (Switch) findViewById(R.id.AlarmSetting);
        alarmWay = (TextView) findViewById(R.id.AlarmWay);
        alarmContent = (TextView) findViewById(R.id.AlarmContent);
        alarmTime = (TextView) findViewById(R.id.AlarmTime);
        divide1 = (View) findViewById(R.id.AlarmDivide1);
        divide2 = (View) findViewById(R.id.AlarmDivide2);
        divide3 = (View) findViewById(R.id.AlarmDivide3);
        divide4 = (View) findViewById(R.id.AlarmDivide4);
        divide5 = (View) findViewById(R.id.AlarmDivide5);
        divide6 = (View) findViewById(R.id.AlarmDivide6);
        divide7 = (View) findViewById(R.id.AlarmDivide7);
        divide8 = (View) findViewById(R.id.AlarmDivide8);
        divide9 = (View) findViewById(R.id.AlarmDivide9);
        divide10 = (View) findViewById(R.id.AlarmDivide10);
        RGroup = (RadioGroup) findViewById(R.id.WayGroup);
        sound = (RadioButton) findViewById(R.id.Way1);
        vibe = (RadioButton) findViewById(R.id.Way2);
        nomode = (RadioButton) findViewById(R.id.Way3);
        CKRain = (CheckBox) findViewById(R.id.checkRain);
        CKPM = (CheckBox) findViewById(R.id.checkPM);
        CKTPR = (CheckBox) findViewById(R.id.checkTPR);
        timePicker = (TimePicker) findViewById(R.id.AlarmTimePicker);
        BTNOK = (Button) findViewById(R.id.BtnOK_SALARM);

        Intent intent = getIntent();
        isSetAlarm = intent.getBooleanExtra("isSetAlarm", true);
        SetAlarmWay = intent.getStringExtra("SetAlarmWay");
        SetAlarmContent = intent.getStringArrayExtra("SetAlarmContent");
        AlarmHour = intent.getIntExtra("AlarmHour", 7);
        AlarmMinute = intent.getIntExtra("AlarmMinute", 0);

        setAlarm.setChecked(isSetAlarm);

        if(SetAlarmWay.equals("소리")){
            sound.setChecked(true);
        }else if(SetAlarmWay.equals("진동")){
            vibe.setChecked(true);
        }else if(SetAlarmWay.equals("무음")){
            nomode.setChecked(true);
        }

        for(int i = 0 ; i < SetAlarmContent.length ; i++){
            switch (SetAlarmContent[i]){
                case "비/눈":
                    CKRain.setChecked(true);
                    break;
                case "미세먼지":
                    CKPM.setChecked(true);
                    break;
                case "일교차":
                    CKTPR.setChecked(true);
                    break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(AlarmHour);
            timePicker.setMinute(AlarmMinute);
        }


        setAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alarmWay.setVisibility(View.VISIBLE);alarmContent.setVisibility(View.VISIBLE);alarmTime.setVisibility(View.VISIBLE);
                    divide1.setVisibility(View.VISIBLE);divide2.setVisibility(View.VISIBLE);divide3.setVisibility(View.VISIBLE);divide4.setVisibility(View.VISIBLE);divide5.setVisibility(View.VISIBLE);divide6.setVisibility(View.VISIBLE);divide7.setVisibility(View.VISIBLE);divide8.setVisibility(View.VISIBLE);divide9.setVisibility(View.VISIBLE);divide10.setVisibility(View.VISIBLE);
                    sound.setVisibility(View.VISIBLE);vibe.setVisibility(View.VISIBLE);nomode.setVisibility(View.VISIBLE);
                    CKRain.setVisibility(View.VISIBLE);CKPM.setVisibility(View.VISIBLE);CKTPR.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                }else{
                    alarmWay.setVisibility(View.INVISIBLE);alarmContent.setVisibility(View.INVISIBLE);alarmTime.setVisibility(View.INVISIBLE);
                    divide1.setVisibility(View.INVISIBLE);divide2.setVisibility(View.INVISIBLE);divide3.setVisibility(View.INVISIBLE);divide4.setVisibility(View.INVISIBLE);divide5.setVisibility(View.INVISIBLE);divide6.setVisibility(View.INVISIBLE);divide7.setVisibility(View.INVISIBLE);divide8.setVisibility(View.INVISIBLE);divide9.setVisibility(View.INVISIBLE);divide10.setVisibility(View.INVISIBLE);
                    sound.setVisibility(View.INVISIBLE);vibe.setVisibility(View.INVISIBLE);nomode.setVisibility(View.INVISIBLE);
                    CKRain.setVisibility(View.INVISIBLE);CKPM.setVisibility(View.INVISIBLE);CKTPR.setVisibility(View.INVISIBLE);
                    timePicker.setVisibility(View.INVISIBLE);
                }
            }
        });

        BTNOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSetAlarm = setAlarm.isChecked();
                if(sound.isChecked()) SetAlarmWay = "소리";
                else if(vibe.isChecked()) SetAlarmWay = "진동";
                else if(nomode.isChecked()) SetAlarmWay = "무음";

                if(CKRain.isChecked()&&CKPM.isChecked()&&CKTPR.isChecked()) SetAlarmContent = new String[]{"비/눈", "미세먼지", "일교차"};
                else if(CKRain.isChecked()&&CKPM.isChecked()&&!CKTPR.isChecked()) SetAlarmContent = new String[]{"비/눈","미세먼지"};
                else if(CKRain.isChecked()&&!CKPM.isChecked()&&CKTPR.isChecked()) SetAlarmContent = new String[]{"비/눈","일교차"};
                else if(CKRain.isChecked()&&!CKPM.isChecked()&&!CKTPR.isChecked()) SetAlarmContent = new String[]{"비/눈"};
                else if(!CKRain.isChecked()&&CKPM.isChecked()&&CKTPR.isChecked()) SetAlarmContent = new String[]{"미세먼지", "일교차"};
                else if(!CKRain.isChecked()&&CKPM.isChecked()&&!CKTPR.isChecked()) SetAlarmContent = new String[]{"미세먼지"};
                else if(!CKRain.isChecked()&&!CKPM.isChecked()&&CKTPR.isChecked()) SetAlarmContent = new String[]{"일교차"};
                else SetAlarmContent = new String[]{};

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    AlarmHour = timePicker.getHour();
                    AlarmMinute = timePicker.getMinute();
                }

                Intent BackIntent = new Intent();
                BackIntent.putExtra("isSetAlarm", isSetAlarm);
                BackIntent.putExtra("SetAlarmWay", SetAlarmWay);
                BackIntent.putExtra("SetAlarmContent", SetAlarmContent);
                BackIntent.putExtra("AlarmHour", AlarmHour);
                BackIntent.putExtra("AlarmMinute", AlarmMinute);
                setResult(Activity.RESULT_OK, BackIntent);
                finish();

            }
        });




    }
}
