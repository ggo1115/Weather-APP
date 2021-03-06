package com.smu.oss14.bb.weather_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingAreaMenu extends Activity {

    Button BtnAdd;
    Button BtnDelete;
    Button BtnBack;


    ArrayList<String> items;
    ListView ListAreaSet;
    ArrayAdapter<String> adapter;

    boolean selectArea = false;
    boolean selectGPS = false;
    boolean gpsOn = true;
    boolean additem = false;
    boolean DeleteMode = false;

    String Location1;
    String selectLocation;

    boolean delete = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_menu);

        BtnAdd = (Button) findViewById(R.id.BtnAddList);
        BtnDelete = (Button) findViewById(R.id.BtnDelList);
        BtnBack = (Button) findViewById(R.id.BtnbackMain);
        ListAreaSet = (ListView) findViewById(R.id.SetAreaList);

        Intent Mainintent = getIntent();
        selectLocation = Mainintent.getStringExtra("SelectArea");
        gpsOn = Mainintent.getBooleanExtra("CurLocationOK", true);
        selectGPS = Mainintent.getBooleanExtra("isSelectCurLocation",true);
        final String[] BookMark = Mainintent.getStringArrayExtra("BookmarkList");

        items = new ArrayList<String>();

        if(gpsOn == true){
            for(int i = 0 ; i < BookMark.length ; i++){
                items.add(BookMark[i]);
            }
        }else {
            for (int i = 1; i < BookMark.length; i++) {
                items.add(BookMark[i]);
            }
        }

        /*if(gpsOn){
            items.add("현재 위치");
        }*/

        if(!DeleteMode){
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        }
        ListAreaSet.setAdapter(adapter);




        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingArea.class);
                startActivityForResult(intent, 1020);
            }
        });

        ListAreaSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!DeleteMode) {
                    String[] Bookmark;
                    //Log.e("check", "=" + ListAreaSet.getCheckedItemPosition());
                    if(!gpsOn){
                        Bookmark = new String[items.size()+1];
                        BookMark[0] = "현재위치";
                        for(int i = 0 ; i < items.size() ; i++){
                            BookMark[i+1] = items.get(i);
                        }
                    }else {
                        Bookmark = new String[items.size()];
                        for (int i = 0; i < items.size(); i++) {
                            Bookmark[i] = items.get(i);
                        }
                    }
                    selectArea = true;

                    if (items.get(position).equals("현재위치")) {
                        selectGPS = true;
                    } else {
                        selectGPS = false;
                    }
                    Intent selectIntent = new Intent();
                    selectIntent.putExtra("BookmarkList", Bookmark);
                    selectIntent.putExtra("isSelect", selectArea);
                    selectIntent.putExtra("isSelectCurLocation", selectGPS);
                    selectIntent.putExtra("selected", items.get(position));
                    setResult(Activity.RESULT_OK, selectIntent);
                    finish();
                }/*else{
                    Toast.makeText(getApplicationContext(), "정보" + position, Toast.LENGTH_SHORT).show();

                }*/
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] Bookmark;
                //Log.e("check", "=" + ListAreaSet.getCheckedItemPosition());
                if(!gpsOn){
                    Bookmark = new String[items.size()+1];
                    BookMark[0] = "현재위치";
                    for(int i = 0 ; i < items.size() ; i++){
                        BookMark[i+1] = items.get(i);
                    }
                }else {
                    Bookmark = new String[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        Bookmark[i] = items.get(i);
                    }
                }
                selectArea = false;
                Intent selectIntent = new Intent();
                selectIntent.putExtra("BookmarkList", Bookmark);
                selectIntent.putExtra("isSelect", selectArea);
                selectIntent.putExtra("isSelectCurLocation", selectGPS);
                selectIntent.putExtra("selected", selectLocation);
                setResult(Activity.RESULT_OK, selectIntent);

                finish();
            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMode = true;
            }
        });

        if (!DeleteMode){
            adapter = new ArrayAdapter<String>(SettingAreaMenu.this, android.R.layout.simple_list_item_single_choice,items);

            final ListView listView = (ListView)findViewById(R.id.SetAreaList);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            Button deleteButton = (Button)findViewById(R.id.Delete);
            deleteButton.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {

                    int count, checked;
                    count = adapter.getCount();

                    if(count > 0){
                        checked = listView.getCheckedItemPosition();

                        if(checked == 0 ){
                            Toast.makeText(getApplicationContext(),  "'" + items.get(checked) +"' 는 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            listView.clearChoices();
                            adapter.notifyDataSetChanged();
                            DeleteMode = false;
                        }
                        if(checked > 0 && checked < count){

                            Toast.makeText(getApplicationContext(),  "'" + items.get(checked) +"' 을(를) 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            items.remove(checked);
                            listView.clearChoices();
                            adapter.notifyDataSetChanged();

                            DeleteMode = false;
                        }

                    }
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        

        if(additem){
            adapter.notifyDataSetChanged();
            additem = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1020){
            if(resultCode == Activity.RESULT_OK){
                Location1 = data.getStringExtra("Location1");
                int count = 0;
                for(int i = 0 ; i < items.size() ; i++){
                    if(Location1.equals(items.get(i))){
                        count++;
                    }
                }
                if(count == 0) {
                    items.add(Location1);
                    additem = true;
                }else{
                    Toast.makeText(getApplicationContext(), "이미 추가된 지역입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
