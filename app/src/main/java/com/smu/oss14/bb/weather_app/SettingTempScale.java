package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingTempScale extends Activity {

    RadioGroup Rgroup;
    RadioButton rFTS;
    RadioButton rCTS;
    Button OK;

    boolean ischeck;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_temp);
        setTitle("온도 단위 설정");
        Rgroup = (RadioGroup) findViewById(R.id.GroupTemp);
        rFTS = (RadioButton) findViewById(R.id.rBtnFts);
        rCTS = (RadioButton) findViewById(R.id.rBtnCts);
        OK = (Button) findViewById(R.id.btnOK_ST);

        Intent SAintent = getIntent();
        ischeck = SAintent.getBooleanExtra("setTemp", true);

        if(ischeck){
            rCTS.setChecked(true);
        }else{
            rFTS.setChecked(true);
        }


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Rgroup.getCheckedRadioButtonId()){
                    case R.id.rBtnFts:
                        ischeck = false;
                        Log.e("setting", "화씨");
                        break;
                    case R.id.rBtnCts:
                        ischeck = true;
                        Log.e("setting", "섭씨");
                        break;
                }

                Intent backIntent = new Intent();
                backIntent.putExtra("check", ischeck);
                setResult(Activity.RESULT_OK, backIntent);
                finish();
            }
        });


    }
}
