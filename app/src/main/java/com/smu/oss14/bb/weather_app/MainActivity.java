package com.smu.oss14.bb.weather_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    Button BtnRe;    //위치확인버튼
    TextView TxtLo;
    ImageView weatherImage;
    TextView TxtTpC;
    TextView TxtTpR;
    TextView TxtTMM;
    TextView TxtTSn;
    TextView TxtWind;
    TextView TxtPer;
    TextView TxtReh;

    Location_Data LData = new Location_Data();
    ArrayList<Weatherinfo_Data> WDataList = new ArrayList<Weatherinfo_Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnRe = (Button) findViewById(R.id.Refresh);
        TxtLo = (TextView) findViewById(R.id.Location);
        TxtTpC = (TextView) findViewById(R.id.TempCur);
        TxtTpR = (TextView) findViewById(R.id.TempRange);
        TxtTMM = (TextView) findViewById(R.id.TempMM);
        TxtTSn = (TextView) findViewById(R.id.TempSen);
        TxtWind = (TextView) findViewById(R.id.Wind);
        TxtPer = (TextView) findViewById(R.id.Per);
        TxtReh = (TextView) findViewById(R.id.Reh);

        GPSActivity gpsActivity = new GPSActivity(MainActivity.this);
        LData = gpsActivity.UseGPS();
        if(gpsActivity.usable_FINE_LOCATION) {
            //usable_FINE_LOCATION일때
            if (gpsActivity.isGetlc) {
                //GPS가 켜져있으면
                String[] Adresult = LData.getAddrValue();
                TxtLo.setText(LData.getAddr());
                final DatabaseReference DBR = FirebaseDatabase.getInstance().getReference().child("LocationCode").child(Adresult[0]).child(Adresult[1]);
                DBR.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LData.setLCcode(dataSnapshot.getValue(String.class));
                        new Thread(){
                            public void run(){
                                //ReceiverShortWeather를 통한 날씨파싱시도
                                ReceiveWeather ReceiveWeather = new ReceiveWeather();
                                //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                                Response response = ReceiveWeather.XMLloading(LData.getLCcode());
                                try {
                                    WDataList = ReceiveWeather.parsing(response.body().string());
                                    TxtTpC.setText(WDataList.get(0).getTemp_cur()+"º");
                                    TxtTMM.setText(WDataList.get(3).getTemp_max()+"/"+WDataList.get(3).getTemp_min()+"º");
                                    Double TempRange = Double.parseDouble( WDataList.get(3).getTemp_max())-Double.parseDouble(WDataList.get(3).getTemp_min());
                                    TxtTpR.setText("일교차\n" + TempRange);
                                    Double TempSens = 13.12 + (0.6215 * Double.parseDouble(WDataList.get(0).getTemp_cur())) - (11.37 * Math.pow(Double.parseDouble(WDataList.get(0).getWs()), 0.16)) + (0.3965 * Math.pow(Double.parseDouble(WDataList.get(0).getWs()), 0.16) * Double.parseDouble(WDataList.get(0).getTemp_cur()));
                                    DecimalFormat form = new DecimalFormat("#.#");
                                    TxtTSn.setText("체감\n" + form.format(TempSens)+"º");
                                    TxtWind.setText("풍속\n" + form.format(WDataList.get(0).getWs())+"m/s");
                                    TxtReh.setText("습도\n" + WDataList.get(0).getReh()+"%");
                                    TxtPer.setText("강수\n" + WDataList.get(0).getPop()+"%");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            } else {
                // GPS가 꺼져있을 때
                Toast.makeText(MainActivity.this, "GPS꺼짐", Toast.LENGTH_SHORT).show();
                gpsActivity.SettingAlert();
            }
        }


    }
    public void onClick1(View view){
        Intent intent = new Intent(this, AreaComp.class);
        startActivity(intent);
    }
    public void  onClick2(View view){
        Intent intent = new Intent(this, SettingArea.class);
        startActivity(intent);
    }
    public void onClick3(View view){
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }


}
