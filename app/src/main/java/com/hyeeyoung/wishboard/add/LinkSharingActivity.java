package com.hyeeyoung.wishboard.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import com.hyeeyoung.wishboard.adapter.SpinnerAdapter;
import com.hyeeyoung.wishboard.R;

import java.util.ArrayList;
import java.util.Arrays;

public class LinkSharingActivity extends AppCompatActivity {

    private NumberPicker date;
    private NumberPicker hour;
    private NumberPicker minute;
    ArrayList<String> noti_types_array;
    Spinner noti_type_spinner;
    SpinnerAdapter spinner_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_sharing);
        init();

        spinner_adapter = new SpinnerAdapter(this, noti_types_array);
        noti_type_spinner.setAdapter(spinner_adapter);


        noti_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            } else if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        }

        // @FIXME : 실행 시 앱 중단
//        initNumperPicker();
//        Intent shareIntent = Intent.createChooser(intent, null);
//        startActivity(shareIntent);

        /**
         * @see : 넘버피커 참고사이트
         * https://stackoverflow.com/questions/35295760/custom-date-time-picker-using-numberpicker-and-fragmentdialog-in-android
         * https://developer.android.com/guide/topics/resources/string-resource?hl=ko#java
         */
    }

    void init()
    {
        noti_types_array = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.noti_types_array)));
        noti_type_spinner = findViewById(R.id.noti_type_spinner);

//        noti_types_array = getResources().getStringArray(R.array.noti_types_array);
//        date = findViewById(R.id.num_picker_date);
//        hour = findViewById(R.id.num_picker_hour);
//        minute = findViewById(R.id.num_picker_minute);
    }

    // @FIXME : 실행 시 앱 중단
    void initNumperPicker(){
        // @brief : 시간 설정
        hour.setMinValue(0);
        hour.setMinValue(23);
        String[] displayed_value_hour = new String[24];

        for(int i = 0; i < 24; i++)
            displayed_value_hour[i] = String.format("%02d", i); // @brief : 1 -> 01로 표시

        hour.setWrapSelectorWheel(true);
        hour.setDisplayedValues(displayed_value_hour);

        // @brief :  분 설정
        minute.setMinValue(0);
        minute.setMinValue(59);
        String[] displayed_value_minute = new String[60];

        for(int i = 0; i < 59; i++)
            displayed_value_minute[i] = String.format("%02d", i); // @brief : 1 -> 01로 표시

        minute.setWrapSelectorWheel(true);
        minute.setDisplayedValues(displayed_value_minute);

        // @brief : 초기 설정값 00 : 30
        hour.setValue(0);
        minute.setValue(30);
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                Toast.makeText(this, "위시리스트에 추가되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case  R.id.cancel:
                finish();
                break;
        }
    }
}