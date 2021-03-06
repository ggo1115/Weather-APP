package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    public static final String ASSET_PATH = "file:///android_asset/";

    Button BtnRe;    //위치확인버튼
    Button BtngtSetting, BtnAreaSetting, BtnAreaComp;
    TextView TxtLo;
    ImageView weatherImage;
    TextView TxtTpC, TxtTpR, TxtTMM, TxtTSn, TxtWind, TxtPer, TxtReh, TxtPm10, TxtPm25;
    ProgressBar progressBar;
    WebView webView;
    ImageView foodImg;

    private boolean SetTempScale = true;//화씨섭씨설정
    private int SetTempScale_int = 1;
    private boolean CurLocationOK = true;//현재위치정보제공허용여부
    private int CurLocationOK_int = 1;
    boolean isSelectCurLocation = true;//현재위치날씨선택여부
    private int isSelectCurLocation_int = 1;
    boolean isSelect = false;//지역선택여부
    private int isSelect_int = 0;
    String[] BookMark = new String[]{"현재위치"};//즐겨찾기목록
    private String BookMark_String = "";
    String selectArea = "현재위치";//선택한지역주소
    private boolean isSetAlarm = true;//알림설정여부
    private int isSetAlarm_int = 1;
    private String SetAlarmWay = "소리"; //알림방법
    private String[] SetAlarmContent = {"비/눈", "미세먼지", "일교차"};//알림내용
    private String SetAlarmContent_String = "";
    private int AlarmHour = 7;//알림설정시간(시)
    private int AlarmMinute = 0;//알림설정시간(분)

    private int i = 0;
    private long time = 0;

    public SharedPreferences preferences;

    Location_Data LData = new Location_Data();
    Air_Data_PM10 Pm10Data;
    Air_Data_PM25 Pm25Data;

    ReadNWriteinDBforTemp RnWTemp;

    ArrayList<Weatherinfo_Data> WDataList = new ArrayList<Weatherinfo_Data>();

    Handler handler;

    DBHelper myDBHelper;

    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("Pref", MODE_PRIVATE);

        BtngtSetting = (Button) findViewById(R.id.setting);
        BtnAreaSetting = (Button) findViewById(R.id.areaSet);
        BtnAreaComp = (Button) findViewById(R.id.areaComp);
        BtnRe = (Button) findViewById(R.id.Refresh);
        TxtLo = (TextView) findViewById(R.id.Location);
        TxtTpC = (TextView) findViewById(R.id.TempCur);
        TxtTpR = (TextView) findViewById(R.id.TempRange);
        TxtTMM = (TextView) findViewById(R.id.TempMM);
        TxtTSn = (TextView) findViewById(R.id.TempSen);
        TxtWind = (TextView) findViewById(R.id.Wind);
        TxtPer = (TextView) findViewById(R.id.Per);
        TxtReh = (TextView) findViewById(R.id.Reh);
        TxtPm10 = (TextView) findViewById(R.id.AirPM10);
        TxtPm25 = (TextView) findViewById(R.id.AirPM25);
        weatherImage = (ImageView) findViewById(R.id.WeatherImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.category_web_view);
        foodImg = (ImageView) findViewById(R.id.foodImg);

        Log.e("pref", "="+preferences.getBoolean("isFirstRun", true));

        myDBHelper = new DBHelper(this);
        checkFirst();

        RnWTemp = new ReadNWriteinDBforTemp();
        RnWTemp.ReadDB();


        BtngtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("setTempScale", SetTempScale);
                intent.putExtra("CurLocationOK", CurLocationOK);
                intent.putExtra("isSetAlarm", isSetAlarm);
                intent.putExtra("SetAlarmWay", SetAlarmWay);
                intent.putExtra("SetAlarmContent", SetAlarmContent);
                intent.putExtra("AlarmHour", AlarmHour);
                intent.putExtra("AlarmMinute", AlarmMinute);
                startActivityForResult(intent, 1);
            }
        });

        BtnAreaSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingAreaMenu.class);
                intent.putExtra("SelectArea", selectArea);
                intent.putExtra("CurLocationOK", CurLocationOK);
                intent.putExtra("isSelectCurLocation", isSelectCurLocation);
                intent.putExtra("BookmarkList",BookMark);
                startActivityForResult(intent, 1030);
            }
        });
        BtnAreaComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AreaComp.class);
                intent.putExtra("SetTempScale", SetTempScale);
                startActivity(intent);
            }
        });
        BtnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                loadChart();
                return true;
            };

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            };

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            };
        });
        webView.loadUrl("file:///android_asset/Dual-Y.html");
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();

        setfoodImage();

        if(selectArea.equals("현재위치")) {
            final GPSActivity gpsActivity = new GPSActivity(MainActivity.this);
            LData = gpsActivity.UseGPS();
            if (gpsActivity.usable_FINE_LOCATION) {
                //usable_FINE_LOCATION일때
                if (gpsActivity.isGetlc) {
                    //GPS가 켜져있으면
                    String[] Adresult = LData.getAddrValue();
                    selectArea = LData.getAddr();
                    TxtLo.setText(LData.getAddr());
                    SetWeather(Adresult);
                } else {
                    // GPS가 꺼져있을 때
                    Toast.makeText(MainActivity.this, "GPS꺼짐", Toast.LENGTH_SHORT).show();
                    gpsActivity.SettingAlert();
                }
            }
        }
        else {
            String[] Adresult = selectArea.split(" ");
            TxtLo.setText(selectArea);
            SetWeather(Adresult);

        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - time >= 1500){
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
            if(SetTempScale) SetTempScale_int = 1;
            else SetTempScale_int = 0;
            if(CurLocationOK) CurLocationOK_int = 1;
            else CurLocationOK_int = 0;
            if(isSelectCurLocation) isSelectCurLocation_int = 1;
            else isSelectCurLocation_int = 0;
            if(isSelect) isSelect_int = 1;
            else isSelect_int = 0;
            BookMark_String = "";
            for(i = 0 ; i < BookMark.length - 1 ; i++){
                BookMark_String += BookMark[i] + ",";
            }
            BookMark_String += BookMark[i];
            if(isSetAlarm) isSetAlarm_int = 1;
            else isSetAlarm_int = 0;
            SetAlarmContent_String = "";
            for(i = 0 ; i < SetAlarmContent.length - 1; i++){
                SetAlarmContent_String += SetAlarmContent[i] + ",";
            }
            SetAlarmContent_String += SetAlarmContent[i];

            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("DELETE FROM SettingDataTBL WHERE TvNum = 1");
            sqlDB.execSQL("INSERT INTO SettingDataTBL VALUES( 1," + SetTempScale_int + "," + CurLocationOK_int + "," + isSelectCurLocation_int + "," + isSelect_int + ",'" + BookMark_String + "','" + selectArea + "'," + isSetAlarm_int + ",'" + SetAlarmWay + "','" + SetAlarmContent_String + "'," + AlarmHour + "," + AlarmMinute + ");");
            sqlDB.close();

            Log.e("SetTempScale", "=" + SetTempScale);
            Log.e("CurLocationOk","="+CurLocationOK);
            Log.e("isSelectCurLocation","="+isSelectCurLocation);
            Log.e("isSelect", "="+isSelect);
            Log.e("BookMark","="+BookMark_String);
            Log.e("selectArea", "="+selectArea);
            Log.e("isSetAlarm", "="+isSetAlarm);
            Log.e("AlarmWay", "="+SetAlarmWay);
            Log.e("AlarmContent","="+SetAlarmContent_String);
            Log.e("AlarmHour/AlarmMinute", AlarmHour+"시"+AlarmMinute+"분");

        }else if(System.currentTimeMillis() - time < 1500){
            finish();
        }
    }

    public void SetWeather(final String[] Adresult){
        final DatabaseReference DBR = FirebaseDatabase.getInstance().getReference().child("LocationCode").child(Adresult[0]).child(Adresult[1]);
        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LData.setLCcode(dataSnapshot.getValue(String.class));
                new Thread() {
                    public void run() {
                        //ReceiverShortWeather를 통한 날씨파싱시도
                        ReceiveWeather ReceiveWeather = new ReceiveWeather();
                        ReceiveAirPM10 ReceiveAirPm10 = new ReceiveAirPM10();
                        ReceiveAirPM25 ReceiveAirPm25 = new ReceiveAirPM25();

                        //지역코드값 넘겨줘서 실행(추후 DB통해 넣을 예정)
                        Response response = ReceiveWeather.XMLloading(LData.getLCcode());
                        Response response_10 = ReceiveAirPm10.XMLloading();
                        Response response_25 = ReceiveAirPm25.XMLloading();
                        try {
                            WDataList = ReceiveWeather.parsing(response.body().string());
                            Pm10Data = ReceiveAirPm10.parsingPm10(response_10.body().string());
                            Pm25Data = ReceiveAirPm25.parsingPm25(response_25.body().string());
                            Double TempRange = Double.parseDouble(WDataList.get(3).getTemp_max()) - Double.parseDouble(WDataList.get(3).getTemp_min());
                            Double TempSens = 13.12 + (0.6215 * Double.parseDouble(WDataList.get(0).getTemp_cur())) - (11.37 * Math.pow(Double.parseDouble(WDataList.get(0).getWs()), 0.16)) + (0.3965 * Math.pow(Double.parseDouble(WDataList.get(0).getWs()), 0.16) * Double.parseDouble(WDataList.get(0).getTemp_cur()));
                            Double TempCur = Double.parseDouble(WDataList.get(0).getTemp_cur());
                            Double TempMax = Double.parseDouble(WDataList.get(0).getTemp_max());
                            Double TempMin = Double.parseDouble(WDataList.get(0).getTemp_min());

                            if (!SetTempScale) {
                                TempCur = (TempCur * 1.8) + 32;
                                TempMax = (TempMax * 1.8) + 32;
                                TempMin = (TempMin * 1.8) + 32;
                                TempSens = (TempSens * 1.8) + 32;
                            }
                            DecimalFormat form = new DecimalFormat("#.#");
                            Select_Location_Air SLAir = new Select_Location_Air(Adresult[0], Pm10Data, Pm25Data);
                            String[] AirState = SLAir.ReturnAir();

                            TxtTpC.setText(TempCur + "º");
                            TxtTMM.setText(TempMax + "/" + TempMin + "º");
                            TxtTpR.setText("일교차\n" + (TempMax - TempMin));
                            TxtTSn.setText("체감\n" + form.format(TempSens) + "º");
                            TxtWind.setText("풍속\n" + WDataList.get(1).getWs() + "m/s");
                            TxtReh.setText("습도\n" + WDataList.get(0).getReh() + "%");
                            TxtPer.setText("강수\n" + WDataList.get(0).getPop() + "%");
                            TxtPm10.setText("미세먼지 " + AirState[0]);
                            TxtPm25.setText("초미세먼지 " + AirState[1]);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    switch (WDataList.get(0).getWf()) {
                                        case "맑음":
                                            weatherImage.setImageResource(R.drawable.sunny);
                                            break;
                                        case "구름 조금":
                                            weatherImage.setImageResource(R.drawable.little_cloud);
                                            break;
                                        case "구름 많음":
                                            weatherImage.setImageResource(R.drawable.many_cloud);
                                            break;
                                        case "흐림":
                                            weatherImage.setImageResource(R.drawable.cloudy);
                                            break;
                                        case "비":
                                            weatherImage.setImageResource(R.drawable.rainy);
                                            break;
                                        case "눈/비":
                                            weatherImage.setImageResource(R.drawable.snowrain);
                                            break;
                                        case "눈":
                                            weatherImage.setImageResource(R.drawable.snowing);
                                            break;
                                        default:
                                            weatherImage.setImageResource(R.drawable.sunny);
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

    public void checkFirst(){
        boolean isFirst = preferences.getBoolean("isFirstRun", true);
        if(isFirst){
            sqlDB = myDBHelper.getWritableDatabase();
            myDBHelper.onCreate(sqlDB);
            sqlDB.close();
            preferences.edit().putBoolean("isFirstRun", false).apply();
        }else{
            sqlDB = myDBHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM SettingDataTBL WHERE TvNum = 1;", null);
            while(cursor.moveToNext()){
                SetTempScale_int = cursor.getInt(1);
                if(SetTempScale_int == 1) SetTempScale = true;
                else if(SetTempScale_int == 0) SetTempScale = false;
                CurLocationOK_int = cursor.getInt(2);
                if(CurLocationOK_int == 1)CurLocationOK = true;
                else if(CurLocationOK_int == 0) CurLocationOK = false;
                isSelectCurLocation_int = cursor.getInt(3);
                if(isSelectCurLocation_int == 1) isSelectCurLocation = true;
                else if(isSelectCurLocation_int == 0) isSelectCurLocation = false;
                isSelect_int = cursor.getInt(4);
                if(isSelect_int == 1) isSelect = true;
                else if(isSelect_int == 0) isSelect = false;
                BookMark_String = cursor.getString(5);
                BookMark = BookMark_String.split(",");
                selectArea = cursor.getString(6);
                isSetAlarm_int = cursor.getInt(7);
                if(isSetAlarm_int == 1) isSetAlarm = true;
                else if(isSetAlarm_int == 0) isSetAlarm = false;
                SetAlarmWay = cursor.getString(8);
                SetAlarmContent_String = cursor.getString(9);
                SetAlarmContent = SetAlarmContent_String.split(",");
                AlarmHour = cursor.getInt(10);
                AlarmMinute = cursor.getInt(11);
                Log.e("SetTempScale", "=" + SetTempScale);
                Log.e("CurLocationOk","="+CurLocationOK);
                Log.e("isSelectCurLocation","="+isSelectCurLocation);
                Log.e("isSelect", "="+isSelect);
                Log.e("BookMark","="+BookMark_String);
                Log.e("selectArea", "="+selectArea);
                Log.e("isSetAlarm", "="+isSetAlarm);
                Log.e("AlarmWay", "="+SetAlarmWay);
                Log.e("AlarmContent","="+SetAlarmContent_String);
                Log.e("AlarmHour/AlarmMinute", AlarmHour+"시"+AlarmMinute+"분");
            }
            cursor.close();
            sqlDB.close();
        }
    }

    public void setfoodImage(){
        String[] summer = {"/summer/bingsu.jpg" ,"/summer/cold-champon.jpg" ,"/summer/coldbeansoupnoodle.jpg", "/summer/icecream.jpg" ,"/summer/naengmyeon.jpg",
                "/summer/samgyetang.jpg", "/summer/watermelon.jpg", "/summer/yeolmuguksu.jpg","/anytime/beer.jpg" ,"/anytime/bibimguksu.jpg", "/anytime/bossam.jpg",
                "/anytime/broiledeels.jpg", "/anytime/buckwheatnoodles.jpg","/anytime/chicken.jpg","/anytime/chickenfoot.jpg","/anytime/corn.png","/anytime/fruitSmoothie.jpg"
                ,"/anytime/gamjatang.jpg","/anytime/haemulpajeon.jpg","/anytime/haemultang.jpg","/anytime/jjolmyeon.jpg","/anytime/muksabal.jpg","/anytime/noodlesoup.jpg"
                ,"/anytime/pigsfeet.jpg","/anytime/smokedDuck.jpg","/anytime/sushi.jpg","/anytime/tiramisu.jpg","/anytime/udon.jpg"};
        String[] winter = {"/winter/beansproutandricesoup.jpg","/anytime/beer.jpg" ,"/anytime/bibimguksu.jpg", "/anytime/bossam.jpg",
                "/anytime/broiledeels.jpg", "/anytime/buckwheatnoodles.jpg","/anytime/chicken.jpg","/anytime/chickenfoot.jpg","/anytime/corn.png","/anytime/fruitSmoothie.jpg"
                ,"/anytime/gamjatang.jpg","/anytime/haemulpajeon.jpg","/anytime/haemultang.jpg","/anytime/jjolmyeon.jpg","/anytime/muksabal.jpg","/anytime/noodlesoup.jpg"
                ,"/anytime/pigsfeet.jpg","/anytime/smokedDuck.jpg","/anytime/sushi.jpg","/anytime/tiramisu.jpg","/anytime/udon.jpg"};
        String[] anytime = {"/anytime/beer.jpg" ,"/anytime/bibimguksu.jpg", "/anytime/bossam.jpg", "/anytime/broiledeels.jpg", "/anytime/buckwheatnoodles.jpg","/anytime/chicken.jpg",
                "/anytime/chickenfoot.jpg","/anytime/corn.png","/anytime/fruitSmoothie.jpg","/anytime/gamjatang.jpg","/anytime/haemulpajeon.jpg","/anytime/haemultang.jpg","/anytime/jjolmyeon.jpg","/anytime/muksabal.jpg"
                ,"/anytime/noodlesoup.jpg","/anytime/pigsfeet.jpg","/anytime/smokedDuck.jpg","/anytime/sushi.jpg","/anytime/tiramisu.jpg","/anytime/udon.jpg"};

        FirebaseStorage fireStorage = FirebaseStorage.getInstance();
        StorageReference reference;
        String ImageUrl = "Foodphoto";

        GregorianCalendar Cur_Month = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        String month = format.format(Cur_Month.getTime());

        if(month.equals("01") || month.equals("02") || month.equals("12")){
            int num = (int)(Math.random() * 10000) % 21;
            ImageUrl += winter[num];
        }else if(month.equals("06") || month.equals("07") || month.equals("08") || month.equals("09")){
            int num = (int)(Math.random() * 10000) % 28;
            ImageUrl += summer[num];
        }else if(month.equals("03") || month.equals("04") || month.equals("05") || month.equals("10") || month.equals("11")){
            int num = (int)(Math.random() * 10000) % 20;
            ImageUrl += anytime[num];
        }

        reference = fireStorage.getReference().child(ImageUrl);
        Glide.with(getApplicationContext()).load(reference).into(foodImg);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                SetTempScale = data.getBooleanExtra("setTempScale", true);
                CurLocationOK = data.getBooleanExtra("CurLocationOK", true);
                isSetAlarm = data.getBooleanExtra("isSetAlarm", true);
                SetAlarmWay = data.getStringExtra("SetAlarmWay");
                SetAlarmContent = data.getStringArrayExtra("SetAlarmContent");
                AlarmHour = data.getIntExtra("AlarmHour", 7);
                AlarmMinute = data.getIntExtra("AlarmMinute", 0);
            }
        }

        else if(requestCode == 1030){
            if(resultCode == Activity.RESULT_OK) {
                BookMark = data.getStringArrayExtra("BookmarkList");
                isSelect = data.getBooleanExtra("isSelect", false);
                isSelectCurLocation = data.getBooleanExtra("isSelectCurLocation", false);
                selectArea = data.getStringExtra("selected");
            }
        }
    }

    public class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context){
            super(context, "Settingdb", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS SettingDataTBL");
            db.execSQL("CREATE TABLE SettingDataTBL ( TvNum INTEGER PRIMARY KEY, TempScale INTEGER, CurLoAccept INTEGER, isSelCur INTEGER, isSelAREA INTEGER, BookMark VARCHAR(1000), SelectAreaLocation VARCHAR(50), isSetAlarm INTEGER, alarmWay VARCHAR(30), alarmContent VARCHAR(50), alarmHour INTEGER, alarmMinute INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS SettingDataTBL");
            onCreate(db);
        }
    }

    private void loadChart() {

        String content = null;
        try {
            AssetManager assetManager = getAssets();
            InputStream in = assetManager.open("Dual-Y.html");
            byte[] bytes = readFully(in);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
        }

        String formattedContent = String.format(content);
        webView.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "utf-8", null);
        webView.requestFocusFromTouch();
    }

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1;) {
            out.write(buffer, 0, count);
        }

        return out.toByteArray();
    }
}