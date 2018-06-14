package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class AreaComp extends Activity{

    Button BtnSelect;
    ImageView Cur1, Cur2, Today1, Today2, Tommorow1, Tommorow2;
    TextView Area1, Area2, CurTemp1, CurTemp2, CurWea1, CurWea2, TodayMM1, TodayMM2, TodayPop1, TodayPop2, TodayWind1, TodayWind2, TomMM1,TomMM2, TomPop1, TomPop2, TomWind1, TomWind2;

    boolean SetArea = false;
    private boolean SetTempScale;
    String Area1_First = null, Area1_Second = null, Area2_First = null, Area2_Second = null;

    Location_Data LData1 = new Location_Data();
    Location_Data LData2 = new Location_Data();

    ArrayList<Weatherinfo_Data> WDataList1 = new ArrayList<Weatherinfo_Data>();
    ArrayList<Weatherinfo_Data> WDataList2 = new ArrayList<Weatherinfo_Data>();

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_c);

        BtnSelect =(Button) findViewById(R.id.BtnSelTA);
        Cur1 = (ImageView) findViewById(R.id.CuriconLo1);
        Cur2 = (ImageView) findViewById(R.id.CuriconLo2);
        Today1 = (ImageView) findViewById(R.id.TodayiconLo1);
        Today2 = (ImageView) findViewById(R.id.TodayiconLo2);
        Tommorow1 = (ImageView) findViewById(R.id.TomoriconLo1);
        Tommorow2 =  (ImageView) findViewById(R.id.TomoriconLo2);
        Area1 = (TextView) findViewById(R.id.Lo1);
        Area2 = (TextView) findViewById(R.id.Lo2);
        CurTemp1 = (TextView) findViewById(R.id.CurTempLo1);
        CurTemp2 = (TextView) findViewById(R.id.CurTempLo2);
        CurWea1 = (TextView) findViewById(R.id.CurWeatherLo1);
        CurWea2 = (TextView) findViewById(R.id.CurWeatherLo2);
        TodayMM1 = (TextView) findViewById(R.id.TodayMM_Lo1);
        TodayMM2 = (TextView) findViewById(R.id.TodayMM_Lo2);
        TodayPop1 = (TextView) findViewById(R.id.TodayPop_Lo1);
        TodayPop2 = (TextView) findViewById(R.id.TodayPop_Lo2);
        TodayWind1 = (TextView) findViewById(R.id.TodayWind_Lo1);
        TodayWind2 = (TextView) findViewById(R.id.TodayWind_Lo2);
        TomMM1 = (TextView) findViewById(R.id.TomorMM_Lo1);
        TomMM2 = (TextView) findViewById(R.id.TomorMM_Lo2);
        TomPop1 = (TextView) findViewById(R.id.TomorPop_Lo1);
        TomPop2 = (TextView) findViewById(R.id.TomorPop_Lo2);
        TomWind1 = (TextView) findViewById(R.id.TomorWind_Lo1);
        TomWind2 = (TextView) findViewById(R.id.TomorWind_Lo2);

        Intent receiveIntent = getIntent();
        SetTempScale = receiveIntent.getBooleanExtra("SetTempScale", false);

        if (!SetArea){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(" ! 지역 선택 ! ")
                    .setMessage("해당 페이지 이용 위해서는 반드시 비교하기 위한 두 지역을 선택해야합니다.")
                    .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
        }

        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Select_ForComp.class);
                startActivityForResult(intent, 1010);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Area1_First != null && Area1_Second != null && Area2_First != null && Area2_Second != null) {

            Area1.setText(Area1_First + " " + Area1_Second);
            Area2.setText(Area2_First + " " + Area2_Second);
            final DatabaseReference DBR1 = FirebaseDatabase.getInstance().getReference().child("LocationCode").child(Area1_First).child(Area1_Second);
            final DatabaseReference DBR2 = FirebaseDatabase.getInstance().getReference().child("LocationCode").child(Area2_First).child(Area2_Second);

            DBR1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    LData1.setLCcode(dataSnapshot.getValue(String.class));

                    new Thread() {
                        public void run() {
                            //ReceiverShortWeather를 통한 날씨파싱시도
                            ReceiveWeather ReceiveWeather = new ReceiveWeather();

                            //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                            Response response = ReceiveWeather.XMLloading(LData1.getLCcode());
                            try {
                                WDataList1 = ReceiveWeather.parsing(response.body().string());

                                DecimalFormat form = new DecimalFormat("#.#");

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Double TempCur = Double.parseDouble(WDataList1.get(0).getTemp_cur());
                                        Double TempMax_Today = Double.parseDouble(WDataList1.get(0).getTemp_max());
                                        Double TempMin_Today = Double.parseDouble(WDataList1.get(0).getTemp_min());
                                        Double TempMax_Tommorow = Double.parseDouble(WDataList1.get(9).getTemp_max());
                                        Double TempMin_Tommorow = Double.parseDouble(WDataList1.get(9).getTemp_min());

                                        if (!SetTempScale) {
                                            TempCur = (TempCur * 1.8) + 32;
                                            TempMax_Today = (TempMax_Today * 1.8) + 32;
                                            TempMin_Today = (TempMin_Today * 1.8) + 32;
                                            TempMax_Tommorow = (TempMax_Tommorow * 1.8) + 32;
                                            TempMin_Tommorow = (TempMin_Tommorow * 1.8) + 32;
                                        }
                                        CurWea1.setText(WDataList1.get(0).getWf());
                                        CurTemp1.setText("현재온도 : " + TempCur + "º");
                                        TodayMM1.setText(TempMax_Today + "/" + TempMin_Today + "º");
                                        TodayPop1.setText("강수확률 : " + WDataList1.get(0).getPop() + "%");
                                        TodayWind1.setText("풍속 : " + WDataList1.get(1).getWs() + "m/s");
                                        TomMM1.setText(TempMax_Tommorow + "/" + TempMin_Tommorow + "º");
                                        TomPop1.setText("강수확률 : " + WDataList1.get(9).getPop());
                                        TomWind1.setText("풍속 : " + WDataList1.get(9).getWs() + "m/s");
                                        switch (WDataList1.get(0).getWf()) {
                                            case "맑음":
                                                Cur1.setImageResource(R.drawable.sunny);
                                                break;
                                            case "구름 조금":
                                                Cur1.setImageResource(R.drawable.little_cloud);
                                                break;
                                            case "구름 많음":
                                                Cur1.setImageResource(R.drawable.many_cloud);
                                                break;
                                            case "흐림":
                                                Cur1.setImageResource(R.drawable.cloudy);
                                                break;
                                            case "비":
                                                Cur1.setImageResource(R.drawable.rainy);
                                                break;
                                            case "눈/비":
                                                Cur1.setImageResource(R.drawable.snowrain);
                                                break;
                                            case "눈":
                                                Cur1.setImageResource(R.drawable.snowing);
                                                break;
                                            default:
                                                Cur1.setImageResource(R.drawable.sunny);
                                        }
                                        switch (WDataList1.get(1).getWf()) {
                                            case "맑음":
                                                Today1.setImageResource(R.drawable.sunny);
                                                break;
                                            case "구름 조금":
                                                Today1.setImageResource(R.drawable.little_cloud);
                                                break;
                                            case "구름 많음":
                                                Today1.setImageResource(R.drawable.many_cloud);
                                                break;
                                            case "흐림":
                                                Today1.setImageResource(R.drawable.cloudy);
                                                break;
                                            case "비":
                                                Today1.setImageResource(R.drawable.rainy);
                                                break;
                                            case "눈/비":
                                                Today1.setImageResource(R.drawable.snowrain);
                                                break;
                                            case "눈":
                                                Today1.setImageResource(R.drawable.snowing);
                                                break;
                                            default:
                                                Today1.setImageResource(R.drawable.sunny);
                                        }
                                        switch (WDataList1.get(9).getWf()) {
                                            case "맑음":
                                                Tommorow1.setImageResource(R.drawable.sunny);
                                                break;
                                            case "구름 조금":
                                                Tommorow1.setImageResource(R.drawable.little_cloud);
                                                break;
                                            case "구름 많음":
                                                Tommorow1.setImageResource(R.drawable.many_cloud);
                                                break;
                                            case "흐림":
                                                Tommorow1.setImageResource(R.drawable.cloudy);
                                                break;
                                            case "비":
                                                Tommorow1.setImageResource(R.drawable.rainy);
                                                break;
                                            case "눈/비":
                                                Tommorow1.setImageResource(R.drawable.snowrain);
                                                break;
                                            case "눈":
                                                Tommorow1.setImageResource(R.drawable.snowing);
                                                break;
                                            default:
                                                Tommorow1.setImageResource(R.drawable.sunny);
                                        }
                                    }
                                });
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

            DBR2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    LData2.setLCcode(dataSnapshot.getValue(String.class));

                    new Thread() {
                        public void run() {
                            //ReceiverShortWeather를 통한 날씨파싱시도
                            ReceiveWeather ReceiveWeather = new ReceiveWeather();

                            //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                            Response response = ReceiveWeather.XMLloading(LData2.getLCcode());
                            try {
                                WDataList2 = ReceiveWeather.parsing(response.body().string());

                                DecimalFormat form = new DecimalFormat("#.#");

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Double TempCur = Double.parseDouble(WDataList2.get(0).getTemp_cur());
                                        Double TempMax_Today = Double.parseDouble(WDataList2.get(0).getTemp_max());
                                        Double TempMin_Today = Double.parseDouble(WDataList2.get(0).getTemp_min());
                                        Double TempMax_Tommorow = Double.parseDouble(WDataList2.get(9).getTemp_max());
                                        Double TempMin_Tommorow = Double.parseDouble(WDataList2.get(9).getTemp_min());

                                        if (!SetTempScale) {
                                            TempCur = (TempCur * 1.8) + 32;
                                            TempMax_Today = (TempMax_Today * 1.8) + 32;
                                            TempMin_Today = (TempMin_Today * 1.8) + 32;
                                            TempMax_Tommorow = (TempMax_Tommorow * 1.8) + 32;
                                            TempMin_Tommorow = (TempMin_Tommorow * 1.8) + 32;
                                        }

                                        CurWea2.setText(WDataList2.get(0).getWf());
                                        CurTemp2.setText("현재온도 : " + TempCur + "º");
                                        TodayMM2.setText(TempMax_Today + "/" + TempMin_Today + "º");
                                        TodayPop2.setText("강수확률 : " + WDataList2.get(0).getPop() + "%");
                                        TodayWind2.setText("풍속 : " + WDataList2.get(1).getWs() + "m/s");
                                        TomMM2.setText(TempMax_Tommorow + "/" + TempMin_Tommorow + "º");
                                        TomPop2.setText("강수확률 : " + WDataList2.get(9).getPop());
                                        TomWind2.setText("풍속 : " + WDataList2.get(9).getWs() + "m/s");



                                        switch (WDataList2.get(0).getWf()) {
                                            case "맑음":
                                                Cur2.setImageResource(R.drawable.sunny);
                                                break;
                                            case "구름 조금":
                                                Cur2.setImageResource(R.drawable.little_cloud);
                                                break;
                                            case "구름 많음":
                                                Cur2.setImageResource(R.drawable.many_cloud);
                                                break;
                                            case "흐림":
                                                Cur2.setImageResource(R.drawable.cloudy);
                                                break;
                                            case "비":
                                                Cur2.setImageResource(R.drawable.rainy);
                                                break;
                                            case "눈/비":
                                                Cur2.setImageResource(R.drawable.snowrain);
                                                break;
                                            case "눈":
                                                Cur2.setImageResource(R.drawable.snowing);
                                                break;
                                            default:
                                                Cur2.setImageResource(R.drawable.sunny);
                                        }
                                        switch (WDataList2.get(1).getWf()) {
                                            case "맑음":
                                                Today2.setImageResource(R.drawable.sunny);
                                                break;
                                            case "구름 조금":
                                                Today2.setImageResource(R.drawable.little_cloud);
                                                break;
                                            case "구름 많음":
                                                Today2.setImageResource(R.drawable.many_cloud);
                                                break;
                                            case "흐림":
                                                Today2.setImageResource(R.drawable.cloudy);
                                                break;
                                            case "비":
                                                Today2.setImageResource(R.drawable.rainy);
                                                break;
                                            case "눈/비":
                                                Today2.setImageResource(R.drawable.snowrain);
                                                break;
                                            case "눈":
                                                Today2.setImageResource(R.drawable.snowing);
                                                break;
                                            default:
                                                Today2.setImageResource(R.drawable.sunny);
                                        }
                                        switch (WDataList2.get(9).getWf()) {
                                            case "맑음":
                                                Tommorow2.setImageResource(R.drawable.sunny);
                                                break;
                                            case "구름 조금":
                                                Tommorow2.setImageResource(R.drawable.little_cloud);
                                                break;
                                            case "구름 많음":
                                                Tommorow2.setImageResource(R.drawable.many_cloud);
                                                break;
                                            case "흐림":
                                                Tommorow2.setImageResource(R.drawable.cloudy);
                                                break;
                                            case "비":
                                                Tommorow2.setImageResource(R.drawable.rainy);
                                                break;
                                            case "눈/비":
                                                Tommorow2.setImageResource(R.drawable.snowrain);
                                                break;
                                            case "눈":
                                                Tommorow2.setImageResource(R.drawable.snowing);
                                                break;
                                            default:
                                                Tommorow2.setImageResource(R.drawable.sunny);
                                        }
                                    }
                                });
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
        }

        BtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Select_ForComp.class);
                startActivityForResult(intent, 1010);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1010){
            if(resultCode == Activity.RESULT_OK){
                SetArea = data.getBooleanExtra("isSelct", false);
                Area1_First = data.getStringExtra("SelectArea1");
                Area1_Second = data.getStringExtra("SelectArea2");
                Area2_First = data.getStringExtra("SelectArea3");
                Area2_Second = data.getStringExtra("SelectArea4");
            }
        }
    }
}
