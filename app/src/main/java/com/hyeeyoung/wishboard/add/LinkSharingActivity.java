package com.hyeeyoung.wishboard.add;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.hyeeyoung.wishboard.adapter.SpinnerAdapter;
import com.hyeeyoung.wishboard.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class LinkSharingActivity extends AppCompatActivity {

    // @param : 알림 날짜 넘버피커 변수
    private NumberPicker date, hour, minute;
    String dates[]; // @param : 디스플레이 될 날짜를 담는 문자열 배열
    Calendar now = Calendar.getInstance();

    // @param : 알림 유형 스피너 변수
    ArrayList<String> noti_types_array;
    Spinner noti_type_spinner;
    SpinnerAdapter spinner_adapter;
    TextView item_name;
    ImageView item_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_sharing);

        // @param : 외부에서 공유를 통해 데이터 받기
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType(); // @param : MIME type 받기

        // @param : 변수 초기화
        init();
        // @FIXME : 알림 날짜 지정 코드로 해당 코드 실행 시 앱 중단
        initNumperPicker();

        // @param : 알림 유형 스피너에서 선택된 아이템 처리 이벤트
        noti_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // @brief : 추후 작성 예정
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // @brief : 추후 작성 예정
            }
        });

        // @breif : 전송 중인 데이터 처리
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) { // @breif : 전송데이터 타입이 텍스트인 경우
                handleSendText(intent);
            } else if (type.startsWith("image/")) { // @breif : 전송데이터 타입이 이미지인 경우
                handleSendImage(intent);
            }
        }

//        Intent shareIntent = Intent.createChooser(intent, null);
//        startActivity(shareIntent);
    }

    void init()
    {
        // @param : 알림 유형 스피너 관련 변수
        noti_types_array = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.noti_types_array)));
        noti_type_spinner = findViewById(R.id.noti_type_spinner);
        spinner_adapter = new SpinnerAdapter(this, noti_types_array);
        noti_type_spinner.setAdapter(spinner_adapter);

        // @param : 알림 날짜 넘버피커 관련 변수
        date = findViewById(R.id.num_picker_date);
        hour = findViewById(R.id.num_picker_hour);
        minute = findViewById(R.id.num_picker_minute);
        dates = getDatesFromCalender();

        item_name = findViewById(R.id.item_name);
        item_image = findViewById(R.id.item_image);
    }

    // @FIXME : 알림 날짜 초기화 함수로 실행 시 앱 중단
    void initNumperPicker(){

        // @brief : 날짜 넘버피커 설정
        date.setMinValue(0);
        date.setMaxValue(dates.length - 1);
        date.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return dates[value];
            }
        });
        date.setDisplayedValues(dates); //@param : 디스플레이될 날짜 값 설정
        date.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        // @brief : 시간 설정, 0~23시 까지 넘버피커 설정
        hour.setMinValue(0); // @param : 범위의 최소값을 설정
        hour.setMaxValue(23); // @param : 범위의 최댓값을 설정
        hour.setValue(now.get(Calendar.HOUR_OF_DAY)); // @param : 초기 설정값 현재 시간으로 설정
        hour.setWrapSelectorWheel(true); // @param : +/- 버튼디자인에서 휠스크롤 디자인으로 변경
        hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // @param : 키보드로 값을 수정하지 못하도록 포커스 막기

        // @brief :  분 설정, 0~59분 까지 넘버피커 설정
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setValue(now.get(Calendar.MINUTE));
        minute.setWrapSelectorWheel(true);
        minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        /**
         * @see : 넘버피커 참고사이트
         * https://stackoverflow.com/questions/35295760/custom-date-time-picker-using-numberpicker-and-fragmentdialog-in-android
         * https://developer.android.com/guide/topics/resources/string-resource?hl=ko#java
         */

    }

    // @param : 날짜 넘버피커 내 디스플레이 될 날짜값 배열 반환
    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        List<String> dates = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd일 EEE"); // @param : 날짜 포맷 지정
        dates.add(dateFormat.format(c1.getTime()));

        for (int i = 0; i < 60; i++) {
            c1.add(Calendar.DATE, 1);
            dates.add(dateFormat.format(c1.getTime()));
        }
        c2.add(Calendar.DATE, -60);

        for (int i = 0; i < 60; i++) {
            c2.add(Calendar.DATE, 1);
            dates.add(dateFormat.format(c2.getTime()));
        }

        return dates.toArray(new String[dates.size() - 1]);
    }

    // @param : 전송 중인 단일 텍스트 처리
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (sharedText != null) {
            // @brief : 데이터 받은 후 UI 업데이트
            item_name.setText(sharedText);
            Log.i("LinkSharing", "handleSendText: "+item_name);
        }
    }

    // @param : 전송 중인 단일 이미지 처리
    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        // @brief : 데이터 받은 후 UI 업데이트
        if (imageUri != null) {
//            try {
//                Log.i("LinkSharing", "handleSendImage: "+imageUri);
//                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                item_image.setImageBitmap(bm);
//            }catch (FileNotFoundException e){
//                e.printStackTrace();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add: // @brief : "위시리스트에 추가하기" 버튼
                Toast.makeText(this, "위시리스트에 추가되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case  R.id.cancel: // @brief : 우측 상단 취소 버튼
                finish();
                break;
        }
    }
}