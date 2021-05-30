package com.hyeeyoung.wishboard.add;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
    TextView item_name, item_price;
    ImageView item_image;

    // @param : 가져온 데이터를 화면에 보여주기 위해 변수 선언
    TextView won;

    // @param : 가져온 데이터 보관
    String site_url = "";

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
            // @breif : 전송데이터 타입이 텍스트인 경우
            if ("text/plain".equals(type)) {
//                handleSendText(intent);
                site_url = intent.getStringExtra(Intent.EXTRA_TEXT); // @params : site url은 String에 보관. DB 저장용
                Log.i("LinkTest", "handleSendText: " + site_url);
                new JsoupAsyncTask(site_url).execute();
            }
        }
    }

    void init() {
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
        item_price = findViewById(R.id.item_price);
        won = findViewById(R.id.won);
    }

    // @FIXME : 알림 날짜 초기화 함수로 실행 시 앱 중단
    void initNumperPicker() {

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

//    // @param : 전송 중인 단일 텍스트 처리
//    void handleSendText(Intent intent) {
//
//    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: // @brief : "위시리스트에 추가하기" 버튼
                Toast.makeText(this, "위시리스트에 추가되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.cancel: // @brief : 우측 상단 취소 버튼
                finish();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class JsoupAsyncTask extends AsyncTask {

        String this_url;
        // @brief : 타이틀 메타태그 내 content 속성값을 상품명으로 사용
        String get_title, get_image, get_price;

        JsoupAsyncTask(String get_url) {
            // @brief : 인텐트로 받아온 url
            this_url = get_url;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Connection con = Jsoup.connect(this_url);
                Document doc = con.get();

                // @brief : HTML의 head부분에 있는 오픈그래프 메타태그 가져오기
                Elements og_tags = doc.select("meta[property^=og:]");
                Elements price_tags = doc.select("meta[property^=product:]");

                if (og_tags.size() <= 0) {
                    return null;
                }

                // @brief : 상품명과 이미지에 해당하는 OGTag를 필터링
                for (int i = 0; i < og_tags.size(); i++) {
                    Element tag = og_tags.get(i);
                    String text = tag.attr("property");

                    if ("og:title".equals(text)) { // @brief : 타이틀 메타태그 내 content 속성값을 상품명으로 사용
                        get_title = tag.attr("content");
                    } else if ("og:image".equals(text)) { // @brief : 이미지 메타태그 내 내 content 속성값을 상품이미지로 사용
                        get_image = tag.attr("content");
                    }
                }

                // @brief : 가격에 해당하는 OGTag를 필터링
                if (price_tags.size() > 0) {
                    for (int i = 0; i < price_tags.size(); i++) {
                        Element price_tag = price_tags.get(i);
                        String text = price_tag.attr("property");

                        // @brief : 정규 표현식을 사용해서 price를 포함하는 속성값인지 검사
                        if (text.matches(".*[pP]rice.*")) {
                            String tmp_price = "";

                            tmp_price = price_tag.attr("content"); // @brief : 가격 메타태그 내 content 속성값을 상품 가격으로 사용

                            // @brief : 숫자로만 구성된 문자열인지 검사
                            if(isNumber(tmp_price)){
                                get_price = tmp_price;
                                break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) { // @brief : doInBackground 작업 후 작업
            super.onPostExecute(o);
            item_name.setText(get_title);
            item_price.setText(get_price);
            // @brief : 피카소로 사진 로딩 속도 개선
            try {
                Picasso.get().load(get_image).into(item_image);
            } catch (IllegalArgumentException i) {
                Log.d("checkings", "사진이 없음");
            }
        }

        public boolean isNumber(String text) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
            }
            return false;
        }
    }
}