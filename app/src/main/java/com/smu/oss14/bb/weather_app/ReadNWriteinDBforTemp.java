package com.smu.oss14.bb.weather_app;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class ReadNWriteinDBforTemp {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference LoCdRef = db.getReference("LocationCode");
    DatabaseReference TempRef = db.getReference("Test");

    ArrayList<Weatherinfo_Data> WD = new ArrayList<Weatherinfo_Data>();

    //날짜
    GregorianCalendar today = new GregorianCalendar();
    GregorianCalendar yesterday = new GregorianCalendar();
    GregorianCalendar tommorow = new GregorianCalendar();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String tday, yester,tommor;

    int count;

    public ReadNWriteinDBforTemp(){
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        tommorow.add(Calendar.DAY_OF_MONTH, 1);
        tday = dateFormat.format(today.getTime());
        yester = dateFormat.format(yesterday.getTime());
        tommor = dateFormat.format(tommorow.getTime());
        count = 0;
        Log.e("어제/오늘/내일", yester + " / " + tday + " / " + tommor);
    }
    public void ReadDB(){
        LoCdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String SD = snapshot.getKey();
                    if(SD.equals("세종특별자치시")){
                        String LCode = snapshot.getValue(String.class);
                        WriteDB(SD,null, LCode);
                    }else {
                        for (DataSnapshot sshot : snapshot.getChildren()) {
                            String SGG = sshot.getKey();
                            String LCode = sshot.getValue(String.class);
                            WriteDB(SD, SGG, LCode);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void WriteDB(final String Addr1, final String Addr2, final String LCode){

        final String[] Temp = new String[8];
        TempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String Date = snapshot.getKey();
                    if(Date.equals(tommor)){
                        count++;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(count == 0) {
            new Thread() {
                public void run() {
                    //ReceiverShortWeather를 통한 날씨파싱시도
                    ReceiveWeather ReceiveWeather = new ReceiveWeather();

                    //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                    Response response = ReceiveWeather.XMLloading(LCode);
                    try {
                        WD = ReceiveWeather.parsing(response.body().string());
                        for (int i = 0; i < WD.size(); i++) {
                            if (WD.get(i).getDate().equals("1")) {
                                if (WD.get(i).getHour().equals("3")) {
                                    Temp[0] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("6")) {
                                    Temp[1] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("9")) {
                                    Temp[2] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("12")) {
                                    Temp[3] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("15")) {
                                    Temp[4] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("18")) {
                                    Temp[5] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("21")) {
                                    Temp[6] = WD.get(i).getTemp_cur();
                                } else if (WD.get(i).getHour().equals("24")) {
                                    Temp[7] = WD.get(i).getTemp_cur();
                                }
                            }
                        }

                        Map<String, String> postValues = new HashMap<>();
                        postValues.put("3", Temp[0]);
                        postValues.put("6", Temp[1]);
                        postValues.put("9", Temp[2]);
                        postValues.put("12", Temp[3]);
                        postValues.put("15", Temp[4]);
                        postValues.put("18", Temp[5]);
                        postValues.put("21", Temp[6]);
                        postValues.put("24", Temp[7]);

                        if (Addr1.equals("세종특별자치시")) {
                            TempRef.child(tommor).child(Addr1).setValue(postValues);
                        } else {
                            TempRef.child(tommor).child(Addr1).child(Addr2).setValue(postValues);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }
}
