package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingCurLocation extends Activity {

    Switch checkCurrenLocation;
    boolean isCurLocation;
    Button BtnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_currentlocation);
        checkCurrenLocation = (Switch) findViewById(R.id.SelCurLocation);
        BtnOK = (Button) findViewById(R.id.btnOK_SCL);

        Intent SAintent = getIntent();
        isCurLocation = SAintent.getBooleanExtra("CurLocationOK", true);

        if(isCurLocation){
            checkCurrenLocation.setChecked(true);
        }else{
            checkCurrenLocation.setChecked(false);
        }

        BtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCurrenLocation.isChecked()){
                    Toast.makeText(getApplicationContext(), "사용", Toast.LENGTH_SHORT).show();
                    isCurLocation = true;
                }else{
                    Toast.makeText(getApplicationContext(), "사용거부", Toast.LENGTH_SHORT).show();
                    isCurLocation = false;
                }

                Intent backintent = new Intent();
                backintent.putExtra("CurLocationOK", isCurLocation);
                setResult(Activity.RESULT_OK, backintent);
                finish();
            }
        });



    }
}
